package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.lid.lib.LabelTextView;

import java.util.List;

import nom.cp101.master.master.Master.Master;
import nom.cp101.master.master.R;

//CourseArticleAdapter繼承RecyclerView.Adapter顯示文章首頁樣式
public class CourseArticleAdapter extends RecyclerView.Adapter<CourseArticleAdapter.ViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    //position為0時,帶入RecyclerView其顯現樣式為GridLayout
    static final int TYPE_GRIDLAYOUT = 0;
    //取得存有課程文章之所有數據
    List<CourseArticleData> courseArticleDataList;

    public CourseArticleAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    //自訂setData-method以便刷新再次給予數據
    public void setData() {
        this.courseArticleDataList = CourseArticleAllData.takeCourseArticleAllList();
    }

    //依靠position置入相對的ViewType
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_GRIDLAYOUT;
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
        if (viewType == TYPE_GRIDLAYOUT) {
            view = LayoutInflater.from(context).inflate(R.layout.course_article_recyclerview_gridview, parent, false);
            holder = new ViewHolder(view);
            holder.gvCourseArticle.setAdapter(new CourseArticleGridViewAdapter(context));

            //剩下設置為課程顯示區
        } else {

            view = LayoutInflater.from(context).inflate(R.layout.course_article_item, parent, false);
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) != TYPE_GRIDLAYOUT) {

            //因position=0時,因有置入gridView所以position需-1來帶入,否則會導致IndexOutOfBoundsException超出index的例外
            CourseArticleData courseArticleData = courseArticleDataList.get(position - 1);

            //給予課程文章編號對server端db發出請求,回傳每筆課程之參加人數
            String courseArticleJoin = CourseArticleAllData.takeCourseArticleJoin(courseArticleData.getCourseArticleId());


            //將list存放各ArticleCourseData物件內的各資料取出顯示
            holder.tvName.setText(courseArticleData.getCourseArticleName());

            holder.tvNumber.setText(
                    courseArticleJoin + "/" + courseArticleData.getCourseArticleNumber());

            holder.tvAddress.setText(courseArticleData.getCourseArticleAddress());
            holder.tvTime.setText(courseArticleData.getCourseArticleTime());
            //點擊課程文章內之各篇文章,切換至教練開課之詳細內容中
            holder.cvCourseArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Master.class);
                    context.startActivity(intent);
                }
            });

            //取得當前課程文章參加人數做不同標籤分類
            int i = Integer.parseInt(courseArticleJoin);

            if(i>2 && i<5){
                holder.tvLabel.setLabelBackgroundColor(context.getResources().getColor(android.R.color.holo_purple));
            }else if(i>=5 && i<10){
                holder.tvLabel.setLabelBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            }

        }
    }


    //回傳次數須為所有課程總數以及第一position放置的gridView
    @Override
    public int getItemCount() {
        return courseArticleDataList.size() + 1;
    }

    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView gvCourseArticle;

        CardView cvCourseArticle;
        TextView tvName, tvNumber, tvAddress, tvTime;
        LabelTextView tvLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            gvCourseArticle = (GridView) itemView.findViewById(R.id.gvCourseArticle);

            cvCourseArticle = (CardView) itemView.findViewById(R.id.cvCourseArticle);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLabel = (LabelTextView) itemView.findViewById(R.id.tvLabel);
        }
    }


}
