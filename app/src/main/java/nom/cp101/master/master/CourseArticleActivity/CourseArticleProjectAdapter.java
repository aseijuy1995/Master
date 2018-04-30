package nom.cp101.master.master.CourseArticleActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.CourseArticle.CourseArticleData;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/30.
 */

class CourseArticleProjectAdapter extends RecyclerView.Adapter<CourseArticleProjectAdapter.ViewHolder> {
    Context context;
    List<CourseArticleData> courseArticleDataList;

    public CourseArticleProjectAdapter(Context context, List<CourseArticleData> courseArticleDataList) {
        this.context = context;
        this.courseArticleDataList = courseArticleDataList;
    }

    @NonNull
    @Override
    public CourseArticleProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.course_article_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseArticleProjectAdapter.ViewHolder holder, int position) {
        CourseArticleData courseArticleData=courseArticleDataList.get(position);

        holder.tvName.setText(courseArticleData.getCourseArticleName());
        holder.tvNumber.setText(courseArticleData.getCourseArticleNumber());
        holder.tvAddress.setText(courseArticleData.getCourseArticleAddress());
        holder.tvTime.setText(courseArticleData.getCourseArticleTime());

    }

    @Override
    public int getItemCount() {
        return courseArticleDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvNumber,tvAddress, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvNumber=(TextView)itemView.findViewById(R.id.tvNumber);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
        }
    }
}
