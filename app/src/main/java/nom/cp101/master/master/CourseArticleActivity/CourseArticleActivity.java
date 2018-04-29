package nom.cp101.master.master.CourseArticleActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nom.cp101.master.master.R;

public class CourseArticleActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_article_act);

        String categoryName=getIntent().getStringExtra("categoryName");
        setTitle(categoryName);

        CourseArticleCategoryFragment courseArticleCategoryFragment=new CourseArticleCategoryFragment();

        Bundle bundle=new Bundle();
        bundle.putString("categoryName", categoryName);
        courseArticleCategoryFragment.setArguments(bundle);

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutCategory, courseArticleCategoryFragment).commit();
    }
}
