package nom.cp101.master.master.CourseArticleActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/29.
 */
//rvCategory搭載在CourseArticleCategoryFragment上與之橋接器
class CourseArticleCategoryAdapter extends RecyclerView.Adapter<CourseArticleCategoryAdapter.ViewHolder> {
    Context context;
    //載入為點選之特定專業類別中,所有的專業項目
    List<CourseArticleCategoryData> courseArticleCategoryDataList;

    public CourseArticleCategoryAdapter(Context context, List<CourseArticleCategoryData> courseArticleCategoryDataList) {
        this.context = context;
        this.courseArticleCategoryDataList = courseArticleCategoryDataList;
    }

    @NonNull
    @Override
    public CourseArticleCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //將R.layout.course_article_category_cardview_item轉為view
        View view = LayoutInflater.from(context).inflate(R.layout.course_article_category_cardview_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CourseArticleCategoryAdapter.ViewHolder holder, int position) {
        //取得點選之專業項目
        final CourseArticleCategoryData courseArticleCategoryData = courseArticleCategoryDataList.get(position);

        holder.layoutCategory.setBackgroundResource(courseArticleCategoryData.getProjectImg());
        holder.tvCategory.setText(courseArticleCategoryData.getProjectName());
        //取得一半的螢幕寬度,並將數據給予搭載的cardView設定
        int w = (context.getResources().getDisplayMetrics().widthPixels / 2);
        ViewGroup.LayoutParams params = holder.cvCategory.getLayoutParams();
        params.height = w;
        params.width = w;

        //點擊指定之專業項目,顯示關於項目之所有課程
        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???
//                Intent intent = new Intent(context, CourseArticleActivity.class);
//                context.startActivity(intent);
                Toast.makeText(context, "successed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //實作該類別需顯示隻項目筆數
    @Override
    public int getItemCount() {
        return courseArticleCategoryDataList.size();
    }

    //使用到之view包裝成ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutCategory;
        CardView cvCategory;
        TextView tvCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutCategory = (LinearLayout) itemView.findViewById(R.id.layoutCategory);
            cvCategory = (CardView) itemView.findViewById(R.id.cvCategory);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);


        }
    }
}
