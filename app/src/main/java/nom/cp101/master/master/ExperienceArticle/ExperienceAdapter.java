package nom.cp101.master.master.ExperienceArticle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.Account.MyPhoto.MyPhotoShowActivity;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.ExperienceArticle.ExperienceArticleSimple.ExperienceSimpleActivity;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

public class ExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private FragmentManager fm;
    private List<Experience> experienceList;

    private final int normalType = 0;
    private final int footType = 1;
    //是否有更多數據
    private boolean hasMore = true;
    //是否隱藏footView提示
    private boolean hiddenHint = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private LruCache<String, Bitmap> lruCache;
    private final String EXPERIENCE_HEAD_KEY_CACHE = "experience_head_cache";
    private final String EXPERIENCE_PHOTO_KEY_CACHE = "experience_photo_cache";
    private final String TAG = "ExperienceAdapter";

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final String DIALOG_TAG = "dialog";
    private final String COLOR_PRIMARY = "#64aae7";
    //收起 extendStr
    //展開 collapseStr
    private SpannableString extendStr;
    private SpannableString collapseStr;

    public ExperienceAdapter(Activity activity, FragmentManager fm, List<Experience> experienceList, boolean hasMore) {
        this.activity = activity;
        this.fm = fm;
        this.experienceList = experienceList;
        this.hasMore = hasMore;
        initMemoryCache();
    }

    //LruCache機制
    public void initMemoryCache() {
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
    public int getItemCount() {
        return experienceList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    //將會使用到的view包崇一個viewHolder,便於使用
    class ExperienceViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivHead;
        private TextView tvHead, tvTime, tvContent, tvLikes, tvComment, tvLaud;
        private ImageView ivPicture;
        private ShineButton sbLaud;
        private LinearLayout llComment;

        public ExperienceViewHolder(View itemView) {
            super(itemView);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead);
            tvHead = (TextView) itemView.findViewById(R.id.tvHead);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvLaud = (TextView) itemView.findViewById(R.id.tvLaud);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            sbLaud = (ShineButton) itemView.findViewById(R.id.sbLaud);
            llComment = (LinearLayout) itemView.findViewById(R.id.llComment);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar pbExperience;
        private TextView tvExperience;

        public FootViewHolder(View itemView) {
            super(itemView);
            pbExperience = (ProgressBar) itemView.findViewById(R.id.pbExperience);
            tvExperience = (TextView) itemView.findViewById(R.id.tvExperience);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == normalType) {
            view = LayoutInflater.from(activity).inflate(R.layout.experience_item, parent, false);
            return new ExperienceViewHolder(view);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.experience_foot_view_item, parent, false);
            return new FootViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExperienceViewHolder) {
            final Experience experience = experienceList.get(position);
            //dateFormat
            Date date = experience.getPostTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String dateStr = dateFormat.format(date);

            String headKey = experience.getUserId() + EXPERIENCE_HEAD_KEY_CACHE;
            String photoKey = experience.getPhotoId() + EXPERIENCE_PHOTO_KEY_CACHE;
            //緩存機制
            Bitmap headImgBitmap = lruCache.get(headKey);
            Bitmap photoImgBitmap = lruCache.get(photoKey);

            if (headImgBitmap == null) {
                headImgBitmap = ConnectionServer.getPhotoByUserId(String.valueOf(experience.userId));
                if (headImgBitmap != null) {
                    lruCache.put(headKey, headImgBitmap);
                    ((ExperienceViewHolder) holder).ivHead.setImageBitmap(headImgBitmap);
                }
                Log.d(TAG, "Load head_img by server:" + position);
            } else {
                ((ExperienceViewHolder) holder).ivHead.setImageBitmap(headImgBitmap);
                Log.d(TAG, "Load head_img by lruCache:" + position);
            }

            if (photoImgBitmap == null) {
                photoImgBitmap = ConnectionServer.getPhotoByPostId(String.valueOf(experience.postId));
                if (photoImgBitmap != null) {
                    lruCache.put(photoKey, photoImgBitmap);
                    ((ExperienceViewHolder) holder).ivPicture.setImageBitmap(photoImgBitmap);
                    Log.d(TAG, "Load photo_img by server:" + position);
                }
            } else {
                ((ExperienceViewHolder) holder).ivPicture.setImageBitmap(photoImgBitmap);
                Log.d(TAG, "Load photo_img by lruCache:" + position);
            }

            ((ExperienceViewHolder) holder).tvHead.setText(experience.getUserName());
            ((ExperienceViewHolder) holder).tvTime.setText(dateStr);
            ((ExperienceViewHolder) holder).sbLaud.setChecked(experience.isPostLike());
            ((ExperienceViewHolder) holder).tvLaud.setTextColor(((ExperienceViewHolder) holder).sbLaud.isChecked() ? Color.parseColor(COLOR_PRIMARY) : Color.GRAY);
            ((ExperienceViewHolder) holder).tvLikes.setText(String.valueOf(experience.getPostLikes()));
            ((ExperienceViewHolder) holder).tvComment.setText(ConnectionServer.getExperienceCommentCount(experience.postId)
                    + activity.getResources().getString(R.string.comment_count));

//            getLastIndexForLimit(activity
//                    , ((ExperienceViewHolder) holder).tvContent
//                    , experience.getPostContent()
//                    , (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.9)
//                    , 3);

            ((ExperienceViewHolder) holder).tvContent.setText(experience.getPostContent());

            ((ExperienceViewHolder) holder).ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment fragment = fm.findFragmentByTag(DIALOG_TAG);
                    if (fragment != null) {
                        ft.remove(fragment);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new ExperienceDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", experience.getUserId());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(ft, DIALOG_TAG);
                }
            });

            ((ExperienceViewHolder) holder).ivPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, MyPhotoShowActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList<String> postList = new ArrayList<>();
                    postList.add(String.valueOf(experience.getPostId()));
                    bundle.putStringArrayList("userAllPostId", postList);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });

            //判斷按讚動作是否有變化,並通知server端做對應
            ((ExperienceViewHolder) holder).sbLaud.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View view, boolean checked) {
                    if (Common.checkUserName(activity, Common.getUserName(activity))) {
                        int postLikes = ConnectionServer.getExperiencePostLikeRefresh(Common.getUserName(activity), experience.getPostId(), checked);
                        ((ExperienceViewHolder) holder).tvLikes.setText(String.valueOf(postLikes));
                        if (checked) {
                            ((ExperienceViewHolder) holder).tvLaud.setTextColor(Color.parseColor(COLOR_PRIMARY));
                        } else {
                            ((ExperienceViewHolder) holder).tvLaud.setTextColor(Color.GRAY);
                        }
                    }
                }
            });

            //展開留言區
            ((ExperienceViewHolder) holder).llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //將裝有文字內容的ExperienceArticleData包裝成Serializable(實作序列化)才可轉移Object
//                    Intent intent = new Intent(activity, ExperienceActivity.class);
                    Intent intent = new Intent(activity, ExperienceSimpleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", experience.getPostId());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            });

            //footView
        } else {
            ((FootViewHolder) holder).pbExperience.setVisibility(View.VISIBLE);
            ((FootViewHolder) holder).tvExperience.setVisibility(View.VISIBLE);

            if (hasMore == true) {
                hiddenHint = false;
                if (experienceList.size() > 0) {
                    ((FootViewHolder) holder).tvExperience.setText(activity.getResources().getString(R.string.refreshing));
                }

            } else {
                if (experienceList.size() > 0) {
                    ((FootViewHolder) holder).tvExperience.setText(activity.getResources().getString(R.string.refreshing_finish));

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            ((FootViewHolder) holder).pbExperience.setVisibility(View.GONE);
                            ((FootViewHolder) holder).tvExperience.setVisibility(View.GONE);
                            hiddenHint = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }
    }

    //自訂method,取得數據最後的位置,footView不算上
    public int getLastPosition() {
        return experienceList.size();
    }

    //自訂method,回傳footView有無隱藏狀態
    public boolean isHiddenHint() {
        return hiddenHint;
    }

    //自訂method,刷新所需
    public void resetDatas() {
        experienceList = new ArrayList<>();
    }

    //自訂method,更新recyclerView並改變hasMore值
    public void updateList(List<Experience> experienceDataList, boolean hasMore) {
        if (experienceDataList != null) {
            this.experienceList.addAll(experienceDataList);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    //取得TextView中最長字串的字元index
    @TargetApi(Build.VERSION_CODES.M)
    private void getLastIndexForLimit(Context context, final TextView tv, String content, int width, int maxLine) {
        //繪製文字的class
        TextPaint textPaint = tv.getPaint();
        //用於TextView拆行處理的class
        StaticLayout staticLayout = StaticLayout.Builder.obtain(content, 0, content.length(), textPaint, width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0, 1)
                .setMaxLines(3)
                .build();

        if (staticLayout.getLineCount() > maxLine) {
            String contentStr = content + "  " + context.getResources().getString(R.string.extend);

            //Spannable.SPAN_EXCLUSIVE_EXCLUSIVE-參數用於改變文字樣式,(前後不包括)
            collapseStr = new SpannableString(contentStr);
            collapseStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff"))
                    , contentStr.length() - 2
                    , contentStr.length()
                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            int index = staticLayout.getLineStart(maxLine) - 1;
            String subString = content.substring(0, index - 2) + "..." + context.getResources().getString(R.string.collapse);
            extendStr = new SpannableString(subString);
            extendStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff"))
                    , subString.length() - 2
                    , subString.length()
                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(extendStr);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        tv.setText(collapseStr);
                        tv.setSelected(false);

                    } else {
                        tv.setText(extendStr);
                        tv.setSelected(true);
                    }
                }
            });
            tv.setSelected(true);

        } else {
            tv.setText(content);
            tv.setSelected(false);
            tv.setOnClickListener(null);
        }
    }
}
