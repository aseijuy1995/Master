package nom.cp101.master.master;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入ViewPager其輪播照片
    static final int TYPE_VIEWPAGER = 0;
    List<ExperienceArticleData> experienceArticleDataList;

    public ExperienceAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        experienceArticleDataList = ExperienceData.takeExperienceArticleDataList();
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
    public ExperienceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        ViewHolder holder;
        //當種類為對等時,載入相對應的xml檔,轉為view使用
        //position=0,置入viewPager,數據為文章圖片
        if (viewType == TYPE_VIEWPAGER) {
            view = LayoutInflater.from(context).inflate(R.layout.experience_recyclerview_viewpager_item, parent, false);
            holder = new ViewHolder(view);
            holder.vp_experience.setAdapter(new ExperienceViewPagerFragmentStatePagerAdapter(fragmentManager));

            //剩下設置為心得文章顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.article_experience_item, parent, false);

        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExperienceAdapter.ViewHolder holder, int position) {

        if (getItemViewType(position) != TYPE_VIEWPAGER) {
            //將數據倒序排列
            //排序過後有數據亂掉的問題

//            Collections.sort(articleCourseDataList);
//            Collections.reverse(articleCourseDataList);

//            Iterator iterator=((LinkedList)articleCourseDataList).descendingIterator();

            //因position=0時,因有置入gridView所以position需-1來帶入,否則會導致IndexOutOfBoundsException超出index的例外
            ExperienceArticleData experienceArticleData = experienceArticleDataList.get(position-1);

            //將list存放各ArticleCourseData物件內的各文章資料取出顯示
            holder.ivHeadAE.setImageResource(experienceArticleData.getExperienceHeadImg());
            holder.tvHeadAE.setText(experienceArticleData.getExperienceHeadName());
            holder.tvProject.setText(experienceArticleData.getExperienceproject());
            holder.ivPictureAE.setImageResource(experienceArticleData.getExperienceImg());
            //Date轉成String?
            //
            holder.tvTime.setText(experienceArticleData.getExperienceTime().toString());
            holder.tvContentAE.setText(experienceArticleData.getExperienceContent());
            //先將各篇文章的讚涉違false,當被按下有事件需處理時,再進行運算
            //
            holder.cbAE.setChecked(false);
        }
    }


    @Override
    public int getItemCount() {
        return 1+experienceArticleDataList.size();
    }


    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager vp_experience;

        CardView cvAE;
        ImageView ivHeadAE, ivPictureAE;
        TextView tvHeadAE, tvProject, tvTime, tvContentAE;
        CheckBox cbAE;

        public ViewHolder(View itemView) {
            super(itemView);
            vp_experience = (ViewPager) itemView.findViewById(R.id.vp_experience);

            cvAE = (CardView) itemView.findViewById(R.id.cvAE);
            ivHeadAE = (ImageView) itemView.findViewById(R.id.ivHeadAE);
            tvHeadAE = (TextView) itemView.findViewById(R.id.tvHeadAE);
            tvProject = (TextView) itemView.findViewById(R.id.tvProject);

            ivPictureAE = (ImageView) itemView.findViewById(R.id.ivPictureAE);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContentAE = (TextView) itemView.findViewById(R.id.tvContentAE);
            cbAE = (CheckBox) itemView.findViewById(R.id.cbAE);
        }
    }
}
