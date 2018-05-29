package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.R;

//文章連接留言的橋接器
public class ExperienceCommentAdapter extends RecyclerView.Adapter<ExperienceCommentAdapter.ViewHolder> {
    Context context;
    List<Comment> commentList;

    public ExperienceCommentAdapter(Context context) {
        this.context = context;

    }

    public void setData(ExperienceData experienceData) {
        this.commentList= ConnectionServer.getExperienceComment(experienceData.getPostId());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.experience_comment_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int width=0, height=0;
//        width=height=context.getResources().getDisplayMetrics().widthPixels/5;
//        holder.ivHeadComment.setMaxWidth(width);
//        holder.ivHeadComment.setMaxHeight(height);
        holder.ivHeadComment.setScaleType(ImageView.ScaleType.FIT_XY);

        Comment comment=commentList.get(position);

        byte[] headImgByte = Base64.decode(comment.getUser_portrait(), Base64.DEFAULT);
        Bitmap headImgBitmap = BitmapFactory.decodeByteArray(headImgByte, 0, headImgByte.length);

        Date date = comment.getComment_time();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        holder.ivHeadComment.setImageBitmap(headImgBitmap);
        holder.tvNameComment.setText(comment.getUser_name());
        holder.tvTimeComment.setText(dateStr);
        holder.tvComment.setText(comment.getComment_content());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeadComment;
        TextView tvNameComment, tvTimeComment, tvComment;
        public ViewHolder(View itemView) {
            super(itemView);
            ivHeadComment=(ImageView)itemView.findViewById(R.id.ivHeadComment);
            tvNameComment=(TextView)itemView.findViewById(R.id.tvNameComment);
            tvTimeComment=(TextView)itemView.findViewById(R.id.tvTimeComment);
            tvComment=(TextView)itemView.findViewById(R.id.tvComment);
        }
    }
}
