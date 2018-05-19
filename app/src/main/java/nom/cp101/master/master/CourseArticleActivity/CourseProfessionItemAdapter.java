package nom.cp101.master.master.CourseArticleActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lid.lib.LabelTextView;

import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.CourseArticle.CourseArticleAllData;
import nom.cp101.master.master.Master.Master;
import nom.cp101.master.master.R;

public class CourseProfessionItemAdapter extends RecyclerView.Adapter<CourseProfessionItemAdapter.ViewHolder> {
    Context context;
    List<Course> courseList;

    public CourseProfessionItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CourseProfessionItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_article_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseProfessionItemAdapter.ViewHolder holder, int position) {
        Course course = courseList.get(position);

        //給予課程文章編號對server端db發出請求,回傳每筆課程之參加人數
        String courseArticleJoin = CourseArticleAllData.takeCourseArticleJoin(course.getCourse_id());

        holder.tvName.setText(course.getCourse_name());
        holder.tvNumber.setText(courseArticleJoin + "/" + course.getCourse_people_number());
        holder.tvAddress.setText(course.getCourse_location());
        holder.tvTime.setText(course.getCourse_date().toString());
        //點擊課程文章內之各篇文章,切換至教練開課之詳細內容中
        holder.cvCourseArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Master.class);
                //各篇課程文章id
//                courseArticleData.getCourseArticleId();
                context.startActivity(intent);
            }
        });
        holder.cvCourseArticle.setEnabled(false);


        //取得當前課程文章參加人數做不同標籤分類
        int i = Integer.parseInt(courseArticleJoin);

        if (i > 2 && i < 5) {
            holder.tvLabel.setLabelBackgroundColor(context.getResources().getColor(android.R.color.holo_purple));
        } else if (i >= 5 && i < 10) {
            holder.tvLabel.setLabelBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvCourseArticle;
        TextView tvName, tvNumber, tvAddress, tvTime;
        LabelTextView tvLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            cvCourseArticle = (CardView) itemView.findViewById(R.id.cvCourseArticle);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLabel = (LabelTextView) itemView.findViewById(R.id.tvLabel);
        }
    }


    //匯入數據
    public void setData(List<Course> courseList) {
        this.courseList = courseList;
    }
}
