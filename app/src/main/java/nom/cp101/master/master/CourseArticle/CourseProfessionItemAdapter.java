package nom.cp101.master.master.CourseArticle;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Master.MasterActivity;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;
import static nom.cp101.master.master.Master.MasterActivity.SEND_COURSE;

public class CourseProfessionItemAdapter extends RecyclerView.Adapter<CourseProfessionItemAdapter.ViewHolder> {
    private Context context;
    private List<Course> courseList;


    public CourseProfessionItemAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseProfessionItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_profession_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseProfessionItemAdapter.ViewHolder holder, int position) {
        final Course course = courseList.get(position);
        //轉日期格式與之判斷是否截止給予相對數據
        //過期反黑且不可點擊
        Date dateStart = course.getCourse_date();
        Date dateEnd = course.getCourse_apply_deadline();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "";

//        判斷截止,則課程不予顯示
        if (dateEnd.getTime() < System.currentTimeMillis()) {
            holder.cvItemCourse.setVisibility(View.GONE);
        }


        //給予課程文章編號對server端db發出請求,回傳每筆課程之參加人數
        int courseJoin = ConnectionServer.getCourseJoin(course.getCourse_id());
        if (courseJoin >= 1) {
            holder.tvLabel.setVisibility(View.VISIBLE);
        } else {
            holder.tvLabel.setVisibility(View.GONE);
        }
        holder.tvNumber.setText(context.getString(R.string.join_count) + courseJoin + "/" + course.getCourse_people_number());

        holder.tvName.setText(course.getCourse_name());
        holder.tvAddress.setText(course.getCourse_location());

        dateStr = dateFormat.format(dateStart);
        holder.tvStartTime.setText(context.getResources().getString(R.string.data_start) + dateStr);
        dateStr = dateFormat.format(dateEnd);
        holder.tvEndTime.setText(context.getResources().getString(R.string.data_deadline) + dateStr);

        if (dateEnd.getTime() > System.currentTimeMillis()) {
            holder.viewGone.setVisibility(View.GONE);

            //Time-Out
        } else {
            holder.viewGone.setVisibility(View.VISIBLE);
        }

        //點擊各篇文章,切換至課程詳細內容
        holder.cvItemCourse.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                if (holder.viewGone.getVisibility() == View.GONE) {
                    if (Common.checkUserName(context, Common.getUserName(context))) {
                        Intent intent = new Intent(context, MasterActivity.class);
                        intent.putExtra(SEND_COURSE, SEND_COURSE);
                        intent.putExtra("course", course);
                        context.startActivity(intent);
                    }

                } else {
                    showToast(context, context.getResources().getString(R.string.data_time_out));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItemCourse;
        TextView tvName, tvStartTime, tvEndTime, tvNumber, tvAddress;
        LabelTextView tvLabel;
        View viewGone;

        public ViewHolder(View itemView) {
            super(itemView);
            cvItemCourse = (CardView) itemView.findViewById(R.id.cvItemCourse);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvStartTime = (TextView) itemView.findViewById(R.id.tvStartTime);
            tvEndTime = (TextView) itemView.findViewById(R.id.tvEndTime);
            tvLabel = (LabelTextView) itemView.findViewById(R.id.tvLabel);
            viewGone = (View) itemView.findViewById(R.id.viewGone);
        }
    }

}
