package nom.cp101.master.master.CourseArticleActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nom.cp101.master.master.R;

//課程文章切換類別之分頁
public class CourseArticleActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_article_act);
        //取得點選之類別名稱並呈現於標題列
        String categoryName = getIntent().getStringExtra("categoryName");
        setTitle(categoryName);
        //將取得之雷別名稱傳入CourseArticleCategoryFragment上,以便辨識需呈獻是項目
        CourseProfessionFragment courseProfessionFragment = new CourseProfessionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryName", categoryName);
        courseProfessionFragment.setArguments(bundle);
        //將指定之courseArticleCategoryFragment嵌在此CourseArticleCategoryActivity上
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutCategory, courseProfessionFragment).commit();
    }
}
