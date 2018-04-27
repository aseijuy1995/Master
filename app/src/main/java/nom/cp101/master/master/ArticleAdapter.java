package nom.cp101.master.master;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//ArticleAdapter繼承RecyclerView.Adapter顯示文章首頁樣式
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入RecyclerView其顯現樣式為GridLayouy
    static final int TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW = 0;
    //取得存有課程文章之所有數據
    List<ArticleCourseData> articleCourseDataList;

    public ArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        articleCourseDataList = ArticleData.takeArticleCourseDataList();
    }

    //依靠position置入相對的ViewType
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
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
        //position=0,置入gridView,數據為專案類別名稱與圖
        if (viewType == TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW) {
            view = LayoutInflater.from(context).inflate(R.layout.article_recyclerview_gridview, parent, false);
            holder = new ViewHolder(view);
            holder.gv_article.setAdapter(new ArticleGridViewAdapter(context));

            //剩下設置為課程文章顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.article_experience_item, parent, false);
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) != TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW) {
            //將數據倒序排列
            //排序過後有數據亂掉的問題

//            Collections.sort(articleCourseDataList);
//            Collections.reverse(articleCourseDataList);

            Iterator iterator=((LinkedList)articleCourseDataList).descendingIterator();

            //因position=0時,因有置入gridView所以position需-1來帶入,否則會導致IndexOutOfBoundsException超出index的例外
            ArticleCourseData articleCourseData = articleCourseDataList.get(position - 1);

            //將list存放各ArticleCourseData物件內的各文章資料取出顯示
            holder.ivHeadAE.setImageResource(articleCourseData.getArticleHeadImg());
            holder.tvHeadAE.setText(articleCourseData.getArticleHeadName());
            holder.ivPictureAE.setImageResource(articleCourseData.getArticleImg());
            holder.tvTime.setVisibility(View.GONE);
            holder.tvContentAE.setText(articleCourseData.getArticleContent());
            //先將各篇文章的讚涉違false,當被按下有事件需處理時,再進行運算
            //
            holder.cbAE.setChecked(false);
        }
    }


    //回傳次數須為所有課程文章總數以及第一position放置的gridView
    @Override
    public int getItemCount() {
        return ArticleData.takeArticleCourseDataList().size() + 1;
    }


    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView gv_article;

        CardView cvAE;
        ImageView ivHeadAE, ivPictureAE;
        TextView tvHeadAE, tvTime, tvContentAE;
        CheckBox cbAE;

        public ViewHolder(View itemView) {
            super(itemView);
            gv_article = (GridView) itemView.findViewById(R.id.gv_article);

            cvAE = (CardView) itemView.findViewById(R.id.cvAE);
            ivHeadAE = (ImageView) itemView.findViewById(R.id.ivHeadAE);
            ivPictureAE = (ImageView) itemView.findViewById(R.id.ivPictureAE);
            tvHeadAE = (TextView) itemView.findViewById(R.id.tvHeadAE);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvContentAE = (TextView) itemView.findViewById(R.id.tvContentAE);
            cbAE = (CheckBox) itemView.findViewById(R.id.cbAE);
        }
    }
}
