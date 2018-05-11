package nom.cp101.master.master.ExperienceArticleActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

//        Comment comment=commentList.get(position);
//
//
//////        holder.ivMsgHead.setImageBitmap();
//        holder.tvMsgName.setText(comment.getUser_id());
//        holder.tvMsgTime.setText(comment.getComment_time().toString());


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMsgHead;
        TextView tvMsgName, tvMsgTime;
        public ViewHolder(View itemView) {
            super(itemView);
            ivMsgHead=(ImageView)itemView.findViewById(R.id.ivMsgHead);
            tvMsgName=(TextView)itemView.findViewById(R.id.tvMsgName);
            tvMsgTime=(TextView)itemView.findViewById(R.id.tvMsgTime);
        }
    }
}
