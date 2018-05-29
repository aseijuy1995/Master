package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.silencedut.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {
    Context context;
    FragmentManager fm;

    //取得文章所有數據
    List<ExperienceData> experienceDataList;

    public ExperienceAdapter(Context context, FragmentManager fm) {
        this.context = context;
        this.fm = fm;
    }


    //分離數據為刷新而用
    public void setData() {
        this.experienceDataList = ConnectionServer.getExperienceDatas(Common.getUserName(context));
    }


    @NonNull
    @Override
    public ExperienceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.experience_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ExperienceAdapter.ViewHolder holder, int position) {
        final ExperienceData experienceData = experienceDataList.get(position);

        byte[] headImgByte = Base64.decode(experienceData.getUserPortraitStr(), Base64.DEFAULT);
        Bitmap headImgBitmap = BitmapFactory.decodeByteArray(headImgByte, 0, headImgByte.length);

        byte[] photoImgByte = Base64.decode(experienceData.getPhotoImgStr(), Base64.DEFAULT);
        Bitmap photoImgBitmap = BitmapFactory.decodeByteArray(photoImgByte, 0, photoImgByte.length);

        Date date = experienceData.getPostTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        holder.ivPicture.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.elExperience.setExpand(false);

        holder.ivHead.setImageBitmap(headImgBitmap);
        holder.tvHead.setText(experienceData.getUserName());
        holder.ivPicture.setImageBitmap(photoImgBitmap);
        holder.sbLaud.setChecked(experienceData.isPostLike());
        holder.tvLikes.setText(String.valueOf(experienceData.getPostLikes()));
        holder.tvTime.setText(dateStr);
        holder.tvContent.setText(experienceData.getPostContent());


        holder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "連到會員中心", Toast.LENGTH_SHORT).show();
                holder.elExperience.setExpand(false);
            }
        });

        holder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //將裝有文字內容的ExperienceArticleData包裝成Serializable(實作序列化)才可轉移Object
                Intent intent = new Intent(context, ExperienceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("postId", experienceData.getPostId());
                intent.putExtras(bundle);
                context.startActivity(intent);
                holder.elExperience.setExpand(false);
            }
        });

        //判斷按讚動作是否有變化,並通知server端做對應
        holder.sbLaud.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                int postLikes = ConnectionServer.getExperienceLikeRefresh(Common.getUserName(context), experienceData.getPostId(), checked);
                holder.tvLikes.setText(String.valueOf(postLikes));
                holder.elExperience.setExpand(false);
            }
        });

        //展開留言區
        holder.ibLeaveMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.elExperience.toggle();
                holder.elExperience.setExpand(true);
            }

        });

        holder.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.elExperience.setExpand(false);
            }
        });

        holder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.elExperience.setExpand(false);
            }
        });


        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.etComment.getText().toString().isEmpty() && holder.etComment.getText().toString().trim() != null) {
                    String commentStr = holder.etComment.getText().toString();

                    int insertOK = ConnectionServer.setExperienceComment(Common.getUserName(context), experienceData.getPostId(), commentStr);
                    if (insertOK == 1) {
                        Toast.makeText(context, context.getString(R.string.successed), Toast.LENGTH_SHORT).show();

                    }
                    holder.etComment.setText("");
                } else {
                    Toast.makeText(context, context.getString(R.string.etMsg_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return experienceDataList.size();
    }


    //將會使用到的view包崇一個viewHolder,便於使用
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvExperience;
        ImageView ivHead, ivPicture;
        TextView tvHead, tvName, tvTime, tvLikes, tvContent;
        ShineButton sbLaud;
        ImageButton ibLeaveMsg;

        ExpandableLayout elExperience;
        EditText etComment;
        Button btnComment;

        public ViewHolder(View itemView) {
            super(itemView);
            cvExperience = (CardView) itemView.findViewById(R.id.cvExperience);
            ivHead = (ImageView) itemView.findViewById(R.id.ivHead);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            tvHead = (TextView) itemView.findViewById(R.id.tvHead);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            sbLaud = (ShineButton) itemView.findViewById(R.id.sbLaud);
            ibLeaveMsg = (ImageButton) itemView.findViewById(R.id.ibLeaveMsg);

            elExperience = (ExpandableLayout) itemView.findViewById(R.id.elExperience);
            etComment = (EditText) itemView.findViewById(R.id.etComment);
            btnComment = (Button) itemView.findViewById(R.id.btnComment);
        }
    }


}
