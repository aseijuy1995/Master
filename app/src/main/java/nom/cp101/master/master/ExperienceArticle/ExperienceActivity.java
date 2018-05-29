package nom.cp101.master.master.ExperienceArticle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

public class ExperienceActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fabExperience;
    ImageView ivExperience, ivHeadImg;
    Toolbar tbExperience;
    View item;

    EditText etComment;
    Button btnComment;
    TextView tvHeadName, tvTime, tvContent;
    RecyclerView rvComment;

    int postId = 0;
    ExperienceData experienceData = null;
    ExperienceCommentAdapter experienceCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_act);
        //初始化元件
        findViews();
        setSupportActionBar(tbExperience);

        postId = getIntent().getExtras().getInt("postId");
        //取得關於點擊的心得文章之所有內容
        experienceData = ConnectionServer.getExperienceData(Common.getUserName(this), postId);

        serRecyclerView(experienceData);
        setData(experienceData);
        btnComment.setOnClickListener(this);


    }

    private void findViews() {
        fabExperience = (FloatingActionButton) findViewById(R.id.fabExperience);
        ivExperience = (ImageView) findViewById(R.id.ivExperience);
        tbExperience = (Toolbar) findViewById(R.id.tbExperience);
        item = (View) findViewById(R.id.item);

        etComment = (EditText) item.findViewById(R.id.etComment);
        btnComment = (Button) item.findViewById(R.id.btnComment);

        ivHeadImg = (ImageView) item.findViewById(R.id.ivHeadImg);
        tvHeadName = (TextView) item.findViewById(R.id.tvHeadName);
        tvTime = (TextView) item.findViewById(R.id.tvTime);
        tvContent = (TextView) item.findViewById(R.id.tvContent);
    }


    private void setData(ExperienceData experienceData) {
        byte[] headImgByte = Base64.decode(experienceData.getUserPortraitStr(), Base64.DEFAULT);
        Bitmap headImgBitmap = BitmapFactory.decodeByteArray(headImgByte, 0, headImgByte.length);

        byte[] photoImgByte = Base64.decode(experienceData.getPhotoImgStr(), Base64.DEFAULT);
        Bitmap photoImgBitmap = BitmapFactory.decodeByteArray(photoImgByte, 0, photoImgByte.length);

        Date date = experienceData.getPostTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        int width = 0, height = 0;
        width = height = getResources().getDisplayMetrics().widthPixels / 4;
        ivExperience.setMaxWidth(width);
        ivExperience.setMaxHeight(height);
        ivExperience.setScaleType(ImageView.ScaleType.FIT_XY);

        ivExperience.setImageBitmap(photoImgBitmap);
        ivHeadImg.setImageBitmap(headImgBitmap);
        tvHeadName.setText(experienceData.getUserName());
        tvTime.setText(dateStr);
        tvContent.setText(experienceData.getPostContent());


    }

    private void serRecyclerView(ExperienceData experienceData) {
        rvComment = (RecyclerView) findViewById(R.id.rvComment);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        experienceCommentAdapter=new ExperienceCommentAdapter(this);
        experienceCommentAdapter.setData(experienceData);
        rvComment.setAdapter(experienceCommentAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnComment:

                if (!etComment.getText().toString().isEmpty() && etComment.getText().toString().trim() != null) {

                    String commentStr = etComment.getText().toString();

                    int insertOK=ConnectionServer.setExperienceComment(Common.getUserName(this), experienceData.getPostId(), commentStr);
                    if(insertOK==1){
                        Toast.makeText(getApplicationContext(), getString(R.string.successed), Toast.LENGTH_SHORT).show();
                    }
                    etComment.setText("");
                    experienceCommentAdapter.setData(experienceData);
                    experienceCommentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.etMsg_empty), Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }
}
