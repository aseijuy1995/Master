package nom.cp101.master.master.CourseArticle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import nom.cp101.master.master.R;

public class CourseArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_article_act);

        String projectName=getIntent().getStringExtra("projectName");
        Toast.makeText(getApplicationContext(),projectName,Toast.LENGTH_SHORT).show();
    }
}
