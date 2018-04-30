package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.CourseArticleActivity.CourseArticleCategoryData;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/26.
 */

//此類別為課程文章需呼叫的各method
public class CourseArticleAllData {


    //專業類別之名稱與圖示,將ArticleViewPagerProjectData的物件包裝成一個ArticleViewPagerData內的陣列便於呼叫
    //回傳list為顯示於課程文章上ViewPager中
    public static final List<CourseArticleGridViewData> takeArticleViewPagerDataList() {

        List courseArticleViewPagerDataList = new ArrayList<>();

        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.water_sports));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.extreme_sport));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.work_out));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.ball_sports));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.musical_instrument));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.language_learning));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.leisure_talent));
        courseArticleViewPagerDataList.add(new CourseArticleGridViewData(android.R.drawable.sym_action_email, R.string.programming));

        return courseArticleViewPagerDataList;
    }


    //專業類別之名稱與圖示,將ArticleViewPagerProjectData的物件包裝成一個ArticleViewPagerData內的陣列便於呼叫
    //回傳list為顯示於課程文章上ViewPager中
    public static final List<CourseArticleData> takeArticleCourseDataList() {

        List courseArticleDataList = new ArrayList<>();

        courseArticleDataList.add(new CourseArticleData("Name1", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name2", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name3", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name4", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name5", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name6", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name7", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name8", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name9", "11/22", "Taipei", "2018/05/22"));
        courseArticleDataList.add(new CourseArticleData("Name10", "11/22", "Taipei", "2018/05/22"));
        return courseArticleDataList;
    }

    //給予點擊之專業類別名稱,返回相對應之專業項目數據
    public static List<CourseArticleCategoryData> takeCourseArticleCategoryDataList(Context context, String categoryName) {

        List<CourseArticleCategoryData> courseArticleCategoryDataList = new ArrayList<>();
        int[] projectImg;
        String[] projectName = null;
        TypedArray t=null;
        //將傳進的專案類別先做比對,相輔則給予對應之陣列數據
        if (categoryName.equals(context.getResources().getString(R.string.water_sports))) {

            t= context.getResources().obtainTypedArray(R.array.waterSportsImg);
            projectName = context.getResources().getStringArray(R.array.waterSportsName);
        }

        projectImg = new int[projectName.length];
        for (int i = 0; i < projectImg.length; i++) {
            projectImg[i] = t.getResourceId(i, -1);
//                courseArticleCategoryDataList.add(new CourseArticleCategoryData(projectImg[0], projectName[0]));
            Log.d("aaa", "aaa");

        }


//
        for (int i = 0; i < projectName.length; i++) {
            courseArticleCategoryDataList.add(new CourseArticleCategoryData(projectImg[i], projectName[i]));
        }
        return courseArticleCategoryDataList;


    }
}

