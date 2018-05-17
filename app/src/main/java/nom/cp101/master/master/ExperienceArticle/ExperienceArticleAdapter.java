package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.ExperienceArticleActivity.ExperienceArticleActivity;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceArticleAdapter extends RecyclerView.Adapter<ExperienceArticleAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入ViewPager其輪播照片
    static final int TYPE_VIEWPAGER = 0;
    List<ExperienceArticleData> experienceArticleDataTextList;
    ExperienceArticleViewPagerAdapter experienceArticleViewPagerAdapter = null;


    public ExperienceArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    //依靠position置入相對的ViewType
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_VIEWPAGER;
        else
            return position;
    }

    @NonNull
    @Override
    public ExperienceArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        final ViewHolder holder;
        //當種類為對等時,載入相對應的xml檔,轉為view使用
        //position=0,置入viewPager,數據為文章圖片
        if (viewType == TYPE_VIEWPAGER) {
            view = LayoutInflater.from(context).inflate(R.layout.experience_article_recyclerview_viewpager_item, parent, false);
            holder = new ViewHolder(view);

            //設置其viewPager相關設置
            setViewPager(holder);

            //剩下設置為心得文章顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.experience_article_item, parent, false);
        }

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ExperienceArticleAdapter.ViewHolder holder, int position) {

        if (getItemViewType(position) != TYPE_VIEWPAGER) {

            //因position=0時,因有置入gridView所以position需-1來帶入,否則會導致IndexOutOfBoundsException超出index的例外
            final ExperienceArticleData experienceArticleTextData = experienceArticleDataTextList.get(position - 1);
            //傳入文章ID,欲塞入之控件,以及裁減大小,其內跑頭貼圖片
            ExperienceArticleAllData.takeExperienceArticleImgData(experienceArticleTextData.getPostId(),
                    holder.ivHead,
                    context.getResources().getDisplayMetrics().widthPixels / 5,
                    0);
            //傳入文章ID,欲塞入之控件,以及裁減大小,其內跑文章圖片
            ExperienceArticleAllData.takeExperienceArticleImgData(experienceArticleTextData.getPostId(),
                    holder.ivPicture,
                    context.getResources().getDisplayMetrics().widthPixels,
                    1);

            //判斷使用者是否有對當前文章做出按讚動作
            boolean postLikeCheck = ExperienceArticleAllData.takeExperienceArticlePostLikeCheck(Common.getUserName(context),
                    experienceArticleTextData.getPostId());


            //取得當前文章讚數
            int postLikes = ExperienceArticleAllData.takeExperienceArticlePostLisk(experienceArticleTextData.getPostId());

            //將list存放各ArticleCourseData物件內的各文章資料取出顯示
            holder.tvHead.setText(experienceArticleTextData.getUserName());
//            holder.tvCategory.setText(experienceArticleTextData.getPostCategoryName());

//            holder.tvName.setText(experienceArticleTextData.getPostCategoryName());
            //Date轉成String?
            holder.tvTime.setText(experienceArticleTextData.getPostTime());
            holder.tvContent.setText(experienceArticleTextData.getPostContent());
            //判斷使用者是否有對此篇文章有過按讚
            holder.cbLaud.setChecked(postLikeCheck);
            //將checkbox上的文字改為個文章該顯示之讚數
            holder.cbLaud.setText(String.valueOf(postLikes));

            //判斷案是否有變化,已通知server端做對應
            holder.cbLaud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int postLikes = ExperienceArticleAllData.takeExperienceArticlePostLikeRefresh(Common.getUserName(context),
                            experienceArticleTextData.getPostId(),
                            isChecked);
                    holder.cbLaud.setText(String.valueOf(postLikes));

                }
            });


            //先將各篇文章的讚涉違false,當被按下有事件需處理時,再進行運算
            //
//            if(experienceArticleData.getExperienceArticleLaud()==0){
//                holder.cbLaud.setChecked(false);
//            }else if(experienceArticleData.getExperienceArticleLaud()==1){
//                holder.cbLaud.setChecked(true);
//            }
//            holder.cbLaud.setText();


            holder.ivPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //將裝有文字內容的ExperienceArticleData包裝成Serializable(實作序列化)才可轉移Object
                    Intent intent = new Intent(context, ExperienceArticleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("experienceArticlePostId", experienceArticleTextData.getPostId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return experienceArticleDataTextList.size() + 1;
    }

    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager vpExperienceArticle;

        CardView cvExperienceArticle;
        ImageView ivHead, ivPicture;
        TextView tvHead, tvCategory, tvName, tvTime, tvContent;
        CheckBox cbLaud;

        public ViewHolder(View itemView) {
            super(itemView);
            vpExperienceArticle = (ViewPager) itemView.findViewById(R.id.vpExperienceArticle);

            cvExperienceArticle = (CardView) itemView.findViewById(R.id.cvExperienceArticle);
            ivHead = (ImageView) itemView.findViewById(R.id.ivHead);
            tvHead = (TextView) itemView.findViewById(R.id.tvHead);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            cbLaud = (CheckBox) itemView.findViewById(R.id.cbLaud);
        }
    }

    //設置其viewPager相關設置
    private void setViewPager(ViewHolder holder) {
        //設置緩存數量,參數不列入當前預載數量範圍
        holder.vpExperienceArticle.setOffscreenPageLimit(5);
        //取得屏幕當前寬高度,對比出viewPager想呈現的大小
        int pagerWidth = (int) (context.getResources().getDisplayMetrics().widthPixels * 4.0f / 5.0f);
        int pagerHeight = (int) (context.getResources().getDisplayMetrics().heightPixels / 3.0f);
        //取得viewPager相關佈局訊息
        ViewGroup.LayoutParams layoutParams = holder.vpExperienceArticle.getLayoutParams();
        //當layoutParams為空,將取得欲設置之寬高度導入viewPager,否則設置寬度即可
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(pagerWidth, pagerHeight);
        } else {
            layoutParams.width = pagerWidth;
            layoutParams.height = pagerHeight;
        }

        //設置各pager間距
        holder.vpExperienceArticle.setPageMargin(5);
        //提供pager轉頁時的動畫
        holder.vpExperienceArticle.setPageTransformer(true, new ExperienceArticleViewPagerTransformer());
        //連接adapter
        experienceArticleViewPagerAdapter = new ExperienceArticleViewPagerAdapter(context);
        experienceArticleViewPagerAdapter.setData();
        holder.vpExperienceArticle.setAdapter(experienceArticleViewPagerAdapter);

        ExperienceArticleViewPagerScroller experienceArticleViewPagerScroller = new ExperienceArticleViewPagerScroller(context);
        experienceArticleViewPagerScroller.setScrollerDuration(3300);
        experienceArticleViewPagerScroller.initViewPagerScroll(holder.vpExperienceArticle);

        //另外開執行緒做page切換
        new ExperienceArticleViewPagerThread(holder).start();
    }

    //抓取在recyclerView呈現的心得文章所有文章
    public void setData() {
        this.experienceArticleDataTextList = ExperienceArticleAllData.takeExperienceArticleDataList();

    }

    //viewPager呈現的心得文章所有文章
    public void setViewPagerData() {
        experienceArticleViewPagerAdapter.setData();
        experienceArticleViewPagerAdapter.notifyDataSetChanged();
    }

}
