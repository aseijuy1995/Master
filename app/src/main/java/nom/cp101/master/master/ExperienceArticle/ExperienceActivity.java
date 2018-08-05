package nom.cp101.master.master.ExperienceArticle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Master.MasterActivity;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;
import static nom.cp101.master.master.Master.MasterActivity.SEND_USER;

public class ExperienceActivity extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private AppBarLayout appBar;
    private Toolbar tbExperience;
    private ImageView ivPhoto, ivComment;
    private TextView tvName, tvTitle, tvTime, tvContent, tvLikes, tvComments, tvEmpty;
    private CircleImageView civHead;
    private EditText etComment;
    private RecyclerView rvComment;
    private View item;
    private ExperienceCommentAdapter experienceCommentAdapter;
    private LinearLayoutManager llmExperience;

    private int postId;
    private Experience experience;
    private List<ExperienceComment> commentList;
    private LruCache<String, Bitmap> lruCache;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int lastVisibleItemPosition = 0;
    private final int PAGE_COUNT = 10;
    private final String EXPERIENCE_HEAD_CACHE = "experience_head_cache";
    private final String EXPERIENCE_PHOTO_CACHE = "experience_photo_cache";
    private final String TAG = "ExperienceActivity";
    private String userId;

    private boolean isNameVisible = true;
    private boolean isTitleVisible = false;
    private final String LIKE = "like";
    private final String UNLIKE = "unlike";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_activity);
        findViews();
        userId = Common.getUserName(this);
        setSupportActionBar(tbExperience);

        if (getIntent().getExtras() != null) {
            postId = getIntent().getExtras().getInt("postId");
            //取得關於點擊的心得文章之所有內容
            experience = ConnectionServer.getExperience(Common.getUserName(this), postId);
            commentList = ConnectionServer.getExperienceComment(experience.getPostId());

            initRecyclerView();
        }
        appBar.addOnOffsetChangedListener(this);
        civHead.setOnClickListener(this);
        ivComment.setOnClickListener(this);

        //自訂mothed,隱藏tvTitle
        startAlphaAnimation(tvTitle, 0, View.INVISIBLE);
    }

    private void findViews() {
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        tbExperience = (Toolbar) findViewById(R.id.tbExperience);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        ivComment = (ImageView) findViewById(R.id.ivComment);
        tvName = (TextView) findViewById(R.id.tvName);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        civHead = (CircleImageView) findViewById(R.id.civHead);
        etComment = (EditText) findViewById(R.id.etComment);
        item = (View) findViewById(R.id.item);
        tvTime = (TextView) item.findViewById(R.id.tvTime);
        tvContent = (TextView) item.findViewById(R.id.tvContent);
        tvLikes = (TextView) item.findViewById(R.id.tvLikes);
        tvComments = (TextView) item.findViewById(R.id.tvComments);
        rvComment = (RecyclerView) item.findViewById(R.id.rvComment);
    }

    private void initRecyclerView() {
        experienceCommentAdapter = new ExperienceCommentAdapter(this,
                getSupportFragmentManager(),
                loadExperienceDatas(0, PAGE_COUNT),
                loadExperienceDatas(0, PAGE_COUNT).size() > 0 ? true : false,
                0);

        llmExperience = new LinearLayoutManager(this);
        rvComment.setLayoutManager(llmExperience);
        rvComment.setAdapter(experienceCommentAdapter);
        rvComment.setItemAnimator(new DefaultItemAnimator());
        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //若滑已置底
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //若加載條沒有隱藏&&最後position的位置為總數量-1
                    //position位置為總數量-1!!
                    if (experienceCommentAdapter.isHiddenHint() == false && lastVisibleItemPosition + 1 == experienceCommentAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(experienceCommentAdapter.getLastPosition(), experienceCommentAdapter.getLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    //若加載條隱藏&&最後position的位置為總數量-2,因加載條被隱藏,表示與顯示數量少1
                    if (experienceCommentAdapter.isHiddenHint() == true && lastVisibleItemPosition + 2 == experienceCommentAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView(experienceCommentAdapter.getLastPosition(), experienceCommentAdapter.getLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //取得最後position
                lastVisibleItemPosition = llmExperience.findLastVisibleItemPosition();
            }
        });

        if (loadExperienceDatas(0, PAGE_COUNT).size() == 0) {
            rvComment.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvComment.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMemory();
        if (experience != null) {
            setExperience(experience);
        }
    }

    private void setExperience(Experience experience) {
        Date date = experience.getPostTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        Bitmap headImgBitmap = lruCache.get(experience.getUserId() + EXPERIENCE_HEAD_CACHE);
        Bitmap photoImgBitmap = lruCache.get(experience.getPhotoId() + EXPERIENCE_PHOTO_CACHE);

        if (headImgBitmap == null) {
            headImgBitmap = ConnectionServer.getPhotoByUserId(experience.getUserId());
            if (headImgBitmap != null) {
                lruCache.put(experience.getUserId() + EXPERIENCE_HEAD_CACHE, headImgBitmap);
                civHead.setImageBitmap(headImgBitmap);
                Log.d(TAG, "load head_img by server");
            }
        }
        if (photoImgBitmap == null) {
            photoImgBitmap = ConnectionServer.getPhotoByPostId(String.valueOf(experience.getPostId()));
            if (photoImgBitmap != null) {
                lruCache.put(experience.getPhotoId() + EXPERIENCE_PHOTO_CACHE, photoImgBitmap);
                Log.d(TAG, "load photo_img by server");
            }
        }

        ivPhoto.setImageBitmap(photoImgBitmap);
        tvTitle.setText(experience.getUserName() + getResources().getString(R.string.experience_post_title));
        tvName.setText(experience.getUserName());
        tvTime.setText(dateStr);
        tvContent.setText(experience.getPostContent());
        tvLikes.setText(String.valueOf(experience.getPostLikes()));
        tvComments.setText(ConnectionServer.getExperienceCommentCount(experience.postId)
                + getResources().getString(R.string.comment_count));
    }

    private void initMemory() {
        //緩存記憶體空間
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        //override存於緩存中的圖片大小
        lruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civHead:
                if (ConnectionServer.getPhotoByUserId(experience.getUserId()) != null) {
                    Intent intent = new Intent(getApplicationContext(), ExperienceUserPortraitActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", experience.getUserId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    showToast(this, getResources().getString(R.string.empty_portrait));
                }
                break;

            case R.id.ivComment:
                if (Common.checkUserName(this, Common.getUserName(this))) {
                    String commentStr = etComment.getText().toString().trim();
                    if (commentStr != null && !commentStr.equals("")) {
                        int insertOK = ConnectionServer.setExperienceComment(Common.getUserName(this), experience.getPostId(), commentStr);
                        if (insertOK == 1) {
                            showToast(this, getResources().getString(R.string.successed));
                            tvComments.setText(ConnectionServer.getExperienceCommentCount(experience.postId)
                                    + getResources().getString(R.string.comment_count));

                            commentList = ConnectionServer.getExperienceComment(experience.getPostId());
                            if (commentList.size() > 0) {
                                rvComment.setVisibility(View.VISIBLE);
                                tvEmpty.setVisibility(View.GONE);
                            }
                            experienceCommentAdapter = new ExperienceCommentAdapter(this,
                                    getSupportFragmentManager(),
                                    commentList,
                                    false,
                                    0);
                            rvComment.setAdapter(experienceCommentAdapter);

                            lastVisibleItemPosition = llmExperience.findLastCompletelyVisibleItemPosition();
                            rvComment.scrollToPosition(lastVisibleItemPosition);
                            rvComment.scrollToPosition(experienceCommentAdapter.getItemCount());
                        }
                        //close_keyboard
                        InputMethodManager imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    } else {
                        showToast(this, getResources().getString(R.string.etMsg_empty));
                    }
                    etComment.setText("");
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.experience_menu, menu);

        if (experience.isPostLike()) {
            menu.getItem(0).setTitle(LIKE);
            menu.getItem(0).setIcon(R.drawable.master_favorite);

        } else {
            menu.getItem(0).setTitle(UNLIKE);
            menu.getItem(0).setIcon(R.drawable.master_unfavorite);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        Bundle bundle = null;
        switch (item.getItemId()) {
            case R.id.menu_like:
                if (Common.checkUserName(this, userId)) {
                    int postLikes;
                    if (item.getTitle() == LIKE) {
                        item.setTitle(UNLIKE);
                        item.setIcon(R.drawable.master_unfavorite);
                        postLikes = ConnectionServer.getExperiencePostLikeRefresh(userId, experience.getPostId(), false);

                    } else {
                        item.setTitle(LIKE);
                        item.setIcon(R.drawable.master_favorite);
                        postLikes = ConnectionServer.getExperiencePostLikeRefresh(userId, experience.getPostId(), true);
                    }
                    tvLikes.setText(String.valueOf(postLikes));
                }
                break;

            case R.id.menu_go:
                if (experience.getUserId().equals(userId)) {
                    intent = new Intent(this, MasterActivity.class);
                    intent.putExtra(SEND_USER, SEND_USER);
                    startActivity(intent);
                    finish();

                } else {
                    intent = new Intent(this, ExperienceUserOtherActivity.class);
                    bundle = new Bundle();
                    bundle.putString("userId", experience.getUserId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
        return true;
    }

    //自訂method,加載數據
    private void updateRecyclerView(int fromIndex, int toIndex) {
        // 获取从fromIndex到toIndex的数据
        List<ExperienceComment> updateList = loadExperienceDatas(fromIndex, toIndex);
        if (updateList.size() > 0) {
            experienceCommentAdapter.updateList(updateList, true);

        } else {
            experienceCommentAdapter.updateList(null, false);
        }
    }

    //自訂method,分頁加載
    private List<ExperienceComment> loadExperienceDatas(final int firstIndex, final int lastIndex) {
        List<ExperienceComment> loadList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < commentList.size()) {
                loadList.add(commentList.get(i));
            }
        }
        return loadList;
    }

    //addOnOffsetChangedListener()
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //取得appBarLayout滑動百分比
        int maxScall = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScall;

        alphaOnTvName(percentage);
        alphaOnTvTitle(percentage);
    }

    //自訂method,控制tvName
    private void alphaOnTvName(float percentage) {
        //當百分比大於0.3時,亦為下拉,將tvName設為不可見
        if (percentage >= 0.2f) {
            if (isNameVisible) {
                startAlphaAnimation(tvName, 200, View.INVISIBLE);
                isNameVisible = false;
            }
            //否則設為不可視
        } else {
            if (!isNameVisible) {
                startAlphaAnimation(tvName, 200, View.VISIBLE);
                isNameVisible = true;
            }
        }
    }

    //自訂method,控制tvTitle
    private void alphaOnTvTitle(float percentage) {
        //當百分比大於0.8時,亦為上縮,將tvTitle設為可見
        if (percentage >= 0.8f) {
            if (!isTitleVisible) {
                startAlphaAnimation(tvTitle, 200, View.VISIBLE);
                isTitleVisible = true;
            }
            //否則設為不可視
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(tvTitle, 200, View.INVISIBLE);
                isTitleVisible = false;
            }
        }
    }

    //自訂method,控制控件隱藏顯示
    private static void startAlphaAnimation(View v, long duration, int visibility) {
        //若visibilityd可見,則透明度設0>>1,不可見則由1>>0
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE) ? new AlphaAnimation(0f, 1f) : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

}