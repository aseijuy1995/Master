package nom.cp101.master.master;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

//ArticleAdapter繼承RecyclerView.Adapter顯示文章首頁樣式
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入ViewPager其輪播照片
    static final int TYPE_VIEWPAGER = 0;
    //position為1時,帶入RecyclerView其顯現樣式為GridLayouy
    static final int TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW = 1;

//    //position=0置入的圖陣列
//    List<ArticleImg> articleImg;
//    //position=1置入專案類別陣列
//    List<ProjectData> projectList;

    public ArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    //依靠position置入相對的ViewType
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_VIEWPAGER;
        else if (position == 1)
            return TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW;
        else
            return position;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        ViewHolder holder;
        //當種類為對等時,載入相對應的xml檔,轉為view使用
        //position=0,置入viewPager,數據為文章圖片
        if (viewType == TYPE_VIEWPAGER) {
            view = LayoutInflater.from(context).inflate(R.layout.article_recyclerview_viewpager_item, parent, false);
            holder = new ViewHolder(view);
            holder.vp_article0.setAdapter(new ArticleViewPagerFragmentStatePagerAdapter(fragmentManager));

            //position=1,置入gridView,數據為專案類別名稱與圖
        } else if (viewType == TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW) {
            view = LayoutInflater.from(context).inflate(R.layout.article_recyclerview_gridview, parent, false);
            holder = new ViewHolder(view);
            holder.gv_article1.setAdapter(new ArticleGridViewAdapter(context));

            //剩下設置為所有課程文章,心得文章之顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.text_item, parent, false);
            holder = new ViewHolder(view);
            holder.tv.setText("!!!!!!!!!!!!!!!!!!");
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 6;
    }


    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager vp_article0;
        GridView gv_article1;

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            vp_article0 = (ViewPager) itemView.findViewById(R.id.vp_article0);
            gv_article1 = (GridView) itemView.findViewById(R.id.gv_article1);

            tv = (TextView) itemView.findViewById(R.id.tv);

        }
    }
}
