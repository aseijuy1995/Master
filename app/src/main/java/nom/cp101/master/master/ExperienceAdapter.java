package nom.cp101.master.master;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入ViewPager其輪播照片
    static final int TYPE_VIEWPAGER = 0;

    public ExperienceAdapter(Context context, FragmentManager fragmentManager) {
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
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.text_item, parent, false);
            holder = new ViewHolder(view);
            holder.tv.setText("??????????????????");
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExperienceAdapter.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 6;
    }


    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager vp_experience;

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            vp_experience = (ViewPager) itemView.findViewById(R.id.vp_experience);

            tv = (TextView) itemView.findViewById(R.id.tv);

        }
    }
}
