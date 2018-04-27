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
    public static final List<ArticleGridViewData> takeArticleViewPagerDataList() {

        List articleViewPagerDataList = new ArrayList<>();

        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.water_sports));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.extreme_sport));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.work_out));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.ball_sports));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.musical_instrument));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.language_learning));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.leisure_talent));
        articleViewPagerDataList.add(new ArticleGridViewData(android.R.drawable.sym_action_email, R.string.programming));

        return articleViewPagerDataList;
    }


    //專業類別之名稱與圖示,將ArticleViewPagerProjectData的物件包裝成一個ArticleViewPagerData內的陣列便於呼叫
    //回傳list為顯示於課程文章上ViewPager中
    public static final List<ArticleCourseData> takeArticleCourseDataList() {

//        List articleCourseDataList = new ArrayList<>();
        List<ArticleCourseData> articleCourseDataList = new LinkedList();

        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "1","類別1",R.drawable.a1, "111" ,0));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "2","類別2",R.drawable.a1, "777",0));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "3","類別3",R.drawable.a2, "555",0));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "4","類別4",R.drawable.a2, "111",0));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "5","類別5",R.drawable.a3, "222",0));
        articleCourseDataList.add(new ArticleCourseData(android.R.drawable.sym_action_email, "6","類別6",R.drawable.a3, "333",0));

        return articleCourseDataList;
    }
}
