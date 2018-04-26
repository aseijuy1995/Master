package nom.cp101.master.master;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yujie on 2018/4/26.
 */

//此類別為課程文章需呼叫的各method
public class ArticleData {


    //專業類別之名稱與圖示,將ArticleViewPagerProjectData的物件包裝成一個ArticleViewPagerData內的陣列便於呼叫
    //回傳list為顯示於課程文章上ViewPager中
    public static final List<ArticleViewPagerData> takeArticleViewPagerDataList() {

        List articleViewPagerDataList = new ArrayList<>();

        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "1"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "2"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "3"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "4"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "5"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "6"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "7"));
        articleViewPagerDataList.add(new ArticleViewPagerData(android.R.drawable.sym_action_email, "8"));

        return articleViewPagerDataList;
    }


    //專業類別之名稱與圖示,將ArticleViewPagerProjectData的物件包裝成一個ArticleViewPagerData內的陣列便於呼叫
    //回傳list為顯示於課程文章上ViewPager中
    public static final List<ArticleCourseData> takeArticleCourseDataList() {

//        List articleCourseDataList = new ArrayList<>();
        List articleCourseDataList = new LinkedList();

        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "1",R.drawable.a1, 0, "111"));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "2",R.drawable.a1, 0, "777"));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "3",R.drawable.a2, 0, "555"));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "4",R.drawable.a2, 0, "111"));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "5",R.drawable.a3, 0, "222"));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "6",R.drawable.a3, 0, "333"));

        return articleCourseDataList;
    }
}
