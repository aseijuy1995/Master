package nom.cp101.master.master.ExperienceArticle.ExperienceArticleSimple;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.Account.MyPhoto.MyPhotoShowActivity;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.ExperienceArticle.Experience;
import nom.cp101.master.master.ExperienceArticle.ExperienceComment;
import nom.cp101.master.master.ExperienceArticle.ExperienceCommentAdapter;
import nom.cp101.master.master.ExperienceArticle.ExperienceDialogFragment;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

public class ExperienceSimpleActivity extends AppCompatActivity implements View.OnClickListener, ShineButton.OnCheckedChangeListener {
    private Toolbar tbExperience;
    private ImageView ivBack, ivPhoto, ivComment;
    private CircleImageView civHead;
    private TextView tvTitle, tvName, tvTime, tvContent, tvLikes, tvComments, tvEmpty, tvLaud;
    private EditText etComment;
    private RecyclerView rvComment;
    private View item;
    private ExperienceCommentAdapter experienceCommentAdapter;
    private LinearLayoutManager llmExperience;
    private ShineButton sbLaud;

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

    private final String COLOR_PRIMARY = "#64aae7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_simple_activity);
        findViews();
        userId = Common.getUserName(this);
        setSupportActionBar(tbExperience);

        if (getIntent().getExtras() != null) {
            postId = getIntent().getExtras().getInt("postId");
            //取得關於點擊的心得文章之所有內容
            experience = ConnectionServer.getExperience(userId, postId);
            commentList = ConnectionServer.getExperienceComment(experience.getPostId());

            initRecyclerView();
        }
        ivComment.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        civHead.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        sbLaud.setOnCheckStateChangeListener(this);

    }

    private void findViews() {
        tbExperience = (Toolbar) findViewById(R.id.tbExperience);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etComment = (EditText) findViewById(R.id.etComment);
        ivComment = (ImageView) findViewById(R.id.ivComment);
        item = (View) findViewById(R.id.item);
        civHead = (CircleImageView) item.findViewById(R.id.civHead);
        tvName = (TextView) item.findViewById(R.id.tvName);
        tvTime = (TextView) item.findViewById(R.id.tvTime);
        ivPhoto = (ImageView) item.findViewById(R.id.ivPhoto);
        tvContent = (TextView) item.findViewById(R.id.tvContent);
        tvLikes = (TextView) item.findViewById(R.id.tvLikes);
        tvComments = (TextView) item.findViewById(R.id.tvComments);
        tvEmpty = (TextView) item.findViewById(R.id.tvEmpty);
        rvComment = (RecyclerView) item.findViewById(R.id.rvComment);
        sbLaud = (ShineButton) item.findViewById(R.id.sbLaud);
        tvLaud = (TextView) item.findViewById(R.id.tvLaud);
    }

    private void initRecyclerView() {
        experienceCommentAdapter = new ExperienceCommentAdapter(this,
                getSupportFragmentManager(),
                loadExperienceDatas(0, PAGE_COUNT),
                loadExperienceDatas(0, PAGE_COUNT).size() > 0 ? true : false,
                1);

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

        tvName.setText(experience.getUserName());
        ivPhoto.setImageBitmap(photoImgBitmap);
        tvTitle.setText(experience.getUserName() + getResources().getString(R.string.experience_post_title));
        tvTime.setText(dateStr);
        tvContent.setText(experience.getPostContent());
        tvLikes.setText(String.valueOf(experience.getPostLikes()));
        tvComments.setText(ConnectionServer.getExperienceCommentCount(experience.postId)
                + getResources().getString(R.string.comment_count));
        sbLaud.setChecked(experience.isPostLike());
        tvLaud.setTextColor((sbLaud.isChecked() ? Color.parseColor(COLOR_PRIMARY) : Color.GRAY));

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
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new ExperienceDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", experience.getUserId());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(ft, "dialog");
                break;

            case R.id.ivComment:
                if (Common.checkUserName(this, userId)) {
                    String commentStr = etComment.getText().toString().trim();
                    if (commentStr != null && !commentStr.equals("")) {
                        int insertOK = ConnectionServer.setExperienceComment(userId, experience.getPostId(), commentStr);
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

            case R.id.ivBack:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;

            case R.id.ivPhoto:
                Intent intent = new Intent(this, MyPhotoShowActivity.class);
                bundle = new Bundle();
                ArrayList<String> postList = new ArrayList<>();
                postList.add(String.valueOf(experience.getPostId()));
                bundle.putStringArrayList("userAllPostId", postList);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    @Override
    public void onCheckedChanged(View view, boolean checked) {
        switch (view.getId()) {
            case R.id.sbLaud:
                //判斷按讚動作是否有變化,並通知server端做對應
                if (Common.checkUserName(this, userId)) {
                    int postLikes = ConnectionServer.getExperiencePostLikeRefresh(userId, experience.getPostId(), checked);
                    tvLikes.setText(String.valueOf(postLikes));
                    if (checked) {
                        tvLaud.setTextColor(Color.parseColor(COLOR_PRIMARY));
                    } else {
                        tvLaud.setTextColor(Color.GRAY);
                    }
                }
                break;
        }
    }
}