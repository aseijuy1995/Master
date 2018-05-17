package nom.cp101.master.master.ExperienceArticleActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nom.cp101.master.master.ExperienceArticle.ExperienceArticleAllData;
import nom.cp101.master.master.ExperienceArticle.ExperienceArticleData;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

public class ExperienceArticleActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    RecyclerView rv;
    View view;
    ImageView ivEAA,ivHeadImg;
    EditText etMsg;
    Button btnMsg;
    TextView tvHeadName, tvTime, tvContent;
    FloatingActionButton fab;
    ExperienceArticleData experienceArticleData=null;
    int postId=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_article_tool);

        postId=getIntent().getExtras().getInt("experienceArticlePostId");

        //取得關於點擊的心得文章之所有內容
        experienceArticleData=ExperienceArticleAllData.takeExperienceArticlePostData(Common.getUserName(this), postId);

        //初始化元件
        findViews();
        setSupportActionBar(toolbar);

        serRecyclerView(experienceArticleData);

        setData(experienceArticleData);

        btnMsg.setOnClickListener(this);


    }



    private void setData(ExperienceArticleData experienceArticleData) {
//        int width=0,height=0;
//        width=height=getResources().getDisplayMetrics().widthPixels/4;
//        ivEAA.setMaxWidth(width);
//        ivEAA.setMaxHeight(height);
//        ivEAA.setScaleType(ImageView.ScaleType.FIT_XY);



        ivEAA.setImageBitmap(BitmapFactory.decodeByteArray(experienceArticleData.getImgPictureByte(),0,experienceArticleData.getImgPictureByte().length));
        ivHeadImg.setImageBitmap(BitmapFactory.decodeByteArray(experienceArticleData.getImgHeadByte(),0,experienceArticleData.getImgHeadByte().length));
        tvHeadName.setText(experienceArticleData.getUserName());
        tvTime.setText(experienceArticleData.getPostTime());
        tvContent.setText(experienceArticleData.getPostContent());


    }

    private void serRecyclerView(ExperienceArticleData experienceArticleData) {
        rv=(RecyclerView)findViewById(R.id.rvLeaveMsg);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ExperienceArticleRecyclerViewAdapter(this, experienceArticleData));
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tbEAA);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        ivEAA=(ImageView)findViewById(R.id.ivEAA);
        view=(View)findViewById(R.id.item);

        etMsg=(EditText)view.findViewById(R.id.etMsg);
        btnMsg=(Button)view.findViewById(R.id.btnMsg);

        ivHeadImg=(ImageView) view.findViewById(R.id.ivHeadImg);
        tvHeadName=(TextView) view.findViewById(R.id.tvHeadName);
        tvTime=(TextView)view.findViewById(R.id.tvTime);
        tvContent=(TextView)view.findViewById(R.id.tvContent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMsg:

                if(!etMsg.getText().toString().isEmpty() && etMsg.getText().toString().trim()!=null){

                    String leave_Msg=etMsg.getText().toString();

                    ExperienceArticleAllData.takeExperienceArticleInsertMsg(Common.getUserName(this),
                            postId,
                            leave_Msg);

                    etMsg.setText("");
                    Toast.makeText(getApplicationContext(), getString(R.string.successed), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.etMsg_empty), Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }
}
