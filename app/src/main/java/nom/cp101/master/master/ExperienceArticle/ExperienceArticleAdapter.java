package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.ExperienceArticleActivity.ExperienceArticleActivity;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceArticleAdapter extends RecyclerView.Adapter<ExperienceArticleAdapter.ViewHolder> implements ViewPager.OnPageChangeListener{
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入ViewPager其輪播照片
    static final int TYPE_VIEWPAGER = 0;
    List<ExperienceArticleData> experienceArticleDataTextList;
    int num=300;
    Handler mHandler;
    Runnable mRunnable;
    Handler viewHandler;


    public ExperienceArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        experienceArticleDataTextList = ExperienceArticleAllData.takeExperienceArticleDataList();
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
            holder.vpExperienceArticle.setAdapter(new ExperienceArticleViewPagerFragmentStatePagerAdapter(context));

            mHandler = new Handler();
            mRunnable = new Runnable() {
                public void run() {
                    // 每隔多长时间执行一次
                    mHandler.postDelayed(this, 1000 * 5);
                    num++;
                    viewHandler.sendEmptyMessage(num);
                }
            };
            viewHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    holder.vpExperienceArticle.setCurrentItem(msg.what);
                    super.handleMessage(msg);
                }
            };

            holder.vpExperienceArticle.setOnPageChangeListener(this);
            holder.vpExperienceArticle.setCurrentItem(num);
            mHandler.postDelayed(mRunnable, 200);



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
            ExperienceArticleData experienceArticleTextData = experienceArticleDataTextList.get(position-1);



            ExperienceArticleAllData.takeExperienceArticleImgData(experienceArticleTextData.getPostId(),
                    holder.ivHead,
                    context.getResources().getDisplayMetrics().widthPixels/5,
                    0);
            ExperienceArticleAllData.takeExperienceArticleImgData(experienceArticleTextData.getPostId(),
                    holder.ivPicture,
                    context.getResources().getDisplayMetrics().widthPixels,
                    1);

            //將list存放各ArticleCourseData物件內的各文章資料取出顯示
            holder.tvHead.setText(experienceArticleTextData.getUserName());
            holder.tvCategory.setText(experienceArticleTextData.getPostCategoryName());

            holder.tvName.setText(experienceArticleTextData.getPostCategoryName());
            //Date轉成String?
            holder.tvTime.setText(experienceArticleTextData.getPostTime());
            holder.tvContent.setText(experienceArticleTextData.getPostContent());
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
                    Intent intent=new Intent(context, ExperienceArticleActivity.class);
                    context.startActivity(intent);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return 1+ experienceArticleDataTextList.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            cbLaud = (CheckBox) itemView.findViewById(R.id.cbLaud);
        }
    }


}
