package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lid.lib.LabelTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Master.Master.bnvMaster;

public class CourseProfessionItemAdapter extends RecyclerView.Adapter<CourseProfessionItemAdapter.ViewHolder> {
    Context context;
    List<Course> courseList;

    public CourseProfessionItemAdapter(Context context) {
        this.context = context;
    }

    //為刷新數據而分離
    public void setData(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseProfessionItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_profession_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseProfessionItemAdapter.ViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.tvName.setText(course.getCourse_name());

        //給予課程文章編號對server端db發出請求,回傳每筆課程之參加人數
        int courseJoin = ConnectionServer.getCourseJoin(course.getCourse_id());
        if (courseJoin >= 1) {
            holder.tvLabel.setVisibility(View.VISIBLE);
        } else {
            holder.tvLabel.setVisibility(View.GONE);
        }
        holder.tvNumber.setText(courseJoin + "/" + course.getCourse_people_number());

        holder.tvAddress.setText(course.getCourse_location());

        //轉日期格式與之判斷是否截止給予相對數據
        //過期反黑且不可點擊
        Date date = course.getCourse_date();
        Date dataDeadline = course.getCourse_apply_deadline();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "";

        if (dataDeadline.getTime() > System.currentTimeMillis()) {
            dateStr = dateFormat.format(date);
            holder.viewGone.setVisibility(View.GONE);
            holder.tvTime.setText(context.getResources().getString(R.string.data_start) + dateStr);
            holder.cvItemCourse.setEnabled(true);
        } else {
            dateStr = dateFormat.format(dataDeadline);
            holder.viewGone.setVisibility(View.VISIBLE);
            holder.tvTime.setText(context.getResources().getString(R.string.data_deadline) + dateStr);
            holder.cvItemCourse.setEnabled(false);
        }

        //點擊各篇文章,切換至課程詳細內容
        holder.cvItemCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnvMaster.setSelectedItemId(R.id.item_information);
                bnvMaster.setVisibility(View.GONE);
//                bottomNavigationView.setSelectedItemId(R.id.menu_course);
            }
        });

    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItemCourse;
        TextView tvName, tvNumber, tvAddress, tvTime;
        LabelTextView tvLabel;
        View viewGone;

        public ViewHolder(View itemView) {
            super(itemView);
            cvItemCourse = (CardView) itemView.findViewById(R.id.cvItemCourse);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLabel = (LabelTextView) itemView.findViewById(R.id.tvLabel);
            viewGone = (View) itemView.findViewById(R.id.viewGone);
        }
    }


}
