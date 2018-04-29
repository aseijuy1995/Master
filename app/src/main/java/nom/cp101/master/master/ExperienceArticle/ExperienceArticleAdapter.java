package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
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

import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceArticleAdapter extends RecyclerView.Adapter<ExperienceArticleAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入ViewPager其輪播照片
    static final int TYPE_VIEWPAGER = 0;
    List<ExperienceArticleData> experienceArticleDataList;

    public ExperienceArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        experienceArticleDataList = ExperienceArticleAllData.takeExperienceArticleDataList();
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
        ViewHolder holder;
        //當種類為對等時,載入相對應的xml檔,轉為view使用
        //position=0,置入viewPager,數據為文章圖片
        if (viewType == TYPE_VIEWPAGER) {
            view = LayoutInflater.from(context).inflate(R.layout.experience_article_recyclerview_viewpager_item, parent, false);
            holder = new ViewHolder(view);
            holder.vpExperienceArticle.setAdapter(new ExperienceArticleViewPagerFragmentStatePagerAdapter(fragmentManager));

            //剩下設置為心得文章顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.experience_article_item, parent, false);

        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExperienceArticleAdapter.ViewHolder holder, int position) {

        if (getItemViewType(position) != TYPE_VIEWPAGER) {

            //因position=0時,因有置入gridView所以position需-1來帶入,否則會導致IndexOutOfBoundsException超出index的例外
            ExperienceArticleData experienceArticleData = experienceArticleDataList.get(position-1);

            //將list存放各ArticleCourseData物件內的各文章資料取出顯示
            holder.ivHead.setImageResource(experienceArticleData.getExperienceArticleHeadImg());
            holder.tvHead.setText(experienceArticleData.getExperienceArticleHeadName());
            holder.tvCategory.setText(experienceArticleData.getExperienceArticleCategory());
            holder.ivPicture.setImageResource(experienceArticleData.getExperienceArticleImg());
            holder.tvName.setText(experienceArticleData.getExperienceArticleName());
            //Date轉成String?
            //
            holder.tvTime.setText(experienceArticleData.getExperienceArticleTime().toString());
            holder.tvContent.setText(experienceArticleData.getExperienceArticleContent());
            //先將各篇文章的讚涉違false,當被按下有事件需處理時,再進行運算
            //
            holder.cbLaud.setChecked(false);
            holder.cbLaud.setText(String.valueOf(experienceArticleData.getExperienceArticleLaud()));
        }
    }


    @Override
    public int getItemCount() {
        return 1+experienceArticleDataList.size();
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
