package nom.cp101.master.master.ExperienceArticleActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.ExperienceArticle.ExperienceArticleAllData;
import nom.cp101.master.master.ExperienceArticle.ExperienceArticleData;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/5/3.
 */

//心得文章連接留言的橋接器
public class ExperienceArticleRecyclerViewAdapter extends RecyclerView.Adapter<ExperienceArticleRecyclerViewAdapter.ViewHolder> {
    Context context;

    List<Comment> commentList;

    public ExperienceArticleRecyclerViewAdapter(Context context,ExperienceArticleData experienceArticleData) {
        this.context = context;
        this.commentList=experienceArticleData.getCommentList();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.experience_article_act_leave_msg,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int width=0, height=0;
//        width=height=context.getResources().getDisplayMetrics().widthPixels/5;
//        holder.ivMsgHead.setMaxWidth(width);
//        holder.ivMsgHead.setMaxHeight(height);
//        holder.ivMsgHead.setScaleType(ImageView.ScaleType.FIT_XY);


        Comment comment=commentList.get(position);

        ExperienceArticleData experienceArticleData=ExperienceArticleAllData.takeExperienceArticleData(comment.getUser_id());


        holder.ivMsgHead.setImageBitmap(BitmapFactory.decodeByteArray(experienceArticleData.getUser_portrait(), 0 ,experienceArticleData.getUser_portrait().length));
        holder.tvMsgName.setText(experienceArticleData.getUser_name());
        holder.tvMsgTime.setText(comment.getComment_time().toString());
        holder.tvMsgContent.setText(comment.getComment_content());





    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMsgHead;
        TextView tvMsgName, tvMsgTime, tvMsgContent;
        public ViewHolder(View itemView) {
            super(itemView);
            ivMsgHead=(ImageView)itemView.findViewById(R.id.ivMsgHead);
            tvMsgName=(TextView)itemView.findViewById(R.id.tvMsgName);
            tvMsgTime=(TextView)itemView.findViewById(R.id.tvMsgTime);
            tvMsgContent=(TextView)itemView.findViewById(R.id.tvMsgContent);


        }
    }
}
