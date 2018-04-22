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

//ArticleAdapter繼承RecyclerView.Adapter顯示首頁個樣式
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>  {
    Context context;
    FragmentManager fragmentManager;

    public ArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType==0) {
            view = LayoutInflater.from(context).inflate(R.layout.article_recyclerview_viewpager_item, parent, false);
            ViewHolder holder=new ViewHolder(view);
            holder.vp_article.setAdapter(new ArticleViewPagerFragmentStatePagerAdapter(fragmentManager));
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.text_item, parent, false);
            ViewHolder holder=new ViewHolder(view);
            holder.tv.setText("!!!!!!!!!!!!!!!!!!");

        }
        return new ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return 0;
        else
            return 1;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 20;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager vp_article;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            vp_article = (ViewPager) itemView.findViewById(R.id.vp_article);

            tv=(TextView)itemView.findViewById(R.id.tv);

        }
    }
}
