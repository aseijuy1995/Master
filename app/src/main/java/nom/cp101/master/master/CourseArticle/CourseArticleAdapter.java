package nom.cp101.master.master.CourseArticle;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.R;

//CourseArticleAdapter繼承RecyclerView.Adapter顯示文章首頁樣式
public class CourseArticleAdapter extends RecyclerView.Adapter<CourseArticleAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入RecyclerView其顯現樣式為GridLayouy
    static final int TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW = 0;
    //取得存有課程文章之所有數據
    List<CourseArticleData> courseArticleDataList;

    public CourseArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        courseArticleDataList = CourseArticleAllData.takeArticleCourseDataList();
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
            view = LayoutInflater.from(context).inflate(R.layout.course_article_recyclerview_gridview, parent, false);
            holder = new ViewHolder(view);
            holder.gvCourseArticle.setAdapter(new CourseArticleGridViewAdapter(context));

            //剩下設置為課程文章顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.course_experience_article_item, parent, false);
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) != TYPE_GRIDLAYOUT_FOR_RECYCLERVIEW) {

            //因position=0時,因有置入gridView所以position需-1來帶入,否則會導致IndexOutOfBoundsException超出index的例外
            CourseArticleData courseArticleData = courseArticleDataList.get(position - 1);

            //將list存放各ArticleCourseData物件內的各文章資料取出顯示
            holder.ivHeadCEA.setImageResource(courseArticleData.getCourseArticleHeadImg());
            holder.tvHeadCEA.setText(courseArticleData.getCourseArticleHeadName());
            holder.tvProjectCEA.setText(courseArticleData.getCourseArticleProject());

            holder.ivPictureCEA.setImageResource(courseArticleData.getCourseArticleImg());
            holder.tvTimeCEA.setVisibility(View.GONE);
            holder.tvContentCEA.setText(courseArticleData.getCourseArticleContent());
            //先將各篇文章的讚涉違false,當被按下有事件需處理時,再進行運算
            //
            holder.cbCEA.setChecked(false);
            holder.cbCEA.setText(String.valueOf(courseArticleData.getCourseArticleLaud()));
        }
    }


    //回傳次數須為所有課程文章總數以及第一position放置的gridView
    @Override
    public int getItemCount() {
        return CourseArticleAllData.takeArticleCourseDataList().size() + 1;
    }


    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView gvCourseArticle;

        CardView cvCEA;
        ImageView ivHeadCEA, ivPictureCEA;
        TextView tvHeadCEA, tvProjectCEA, tvTimeCEA, tvContentCEA;
        CheckBox cbCEA;
        ImageButton ibLeaveMessageCEA;

        public ViewHolder(View itemView) {
            super(itemView);
            gvCourseArticle = (GridView) itemView.findViewById(R.id.gvCourseArticle);

            cvCEA = (CardView) itemView.findViewById(R.id.cvCEA);
            ivHeadCEA = (ImageView) itemView.findViewById(R.id.ivHeadCEA);
            tvHeadCEA = (TextView) itemView.findViewById(R.id.tvHeadCEA);
            tvProjectCEA = (TextView) itemView.findViewById(R.id.tvProjectCEA);

            ivPictureCEA = (ImageView) itemView.findViewById(R.id.ivPictureCEA);
            tvTimeCEA = (TextView) itemView.findViewById(R.id.tvTimeCEA);
            tvContentCEA = (TextView) itemView.findViewById(R.id.tvContentCEA);
            cbCEA = (CheckBox) itemView.findViewById(R.id.cbCEA);
            ibLeaveMessageCEA=(ImageButton)itemView.findViewById(R.id.ibLeaveMessageCEA);
        }
    }
}
