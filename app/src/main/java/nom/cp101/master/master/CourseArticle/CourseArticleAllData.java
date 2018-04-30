package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.content.res.TypedArray;

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
        TypedArray typedArray = null;
        //將傳進的專案類別先做比對,相輔則給予對應之陣列數據
        if (categoryName.equals(context.getResources().getString(R.string.water_sports))) {
            typedArray = context.getResources().obtainTypedArray(R.array.waterSportsImg);
            projectName = context.getResources().getStringArray(R.array.waterSportsName);

        }else if (categoryName.equals(context.getResources().getString(R.string.extreme_sport))) {
            typedArray = context.getResources().obtainTypedArray(R.array.extremeSportImg);
            projectName = context.getResources().getStringArray(R.array.extremeSportName);

        }else if (categoryName.equals(context.getResources().getString(R.string.work_out))) {
            typedArray = context.getResources().obtainTypedArray(R.array.workOutImg);
            projectName = context.getResources().getStringArray(R.array.workOutName);

        }else if (categoryName.equals(context.getResources().getString(R.string.ball_sports))) {
            typedArray = context.getResources().obtainTypedArray(R.array.ballSportsImg);
            projectName = context.getResources().getStringArray(R.array.ballSportsName);

        } else if (categoryName.equals(context.getResources().getString(R.string.musical_instrument))) {
            typedArray = context.getResources().obtainTypedArray(R.array.musicalInstrumentImg);
            projectName = context.getResources().getStringArray(R.array.musicalInstrumentName);

        } else if (categoryName.equals(context.getResources().getString(R.string.language_learning))) {
            typedArray = context.getResources().obtainTypedArray(R.array.languageLearningImg);
            projectName = context.getResources().getStringArray(R.array.languageLearningName);

        } else if (categoryName.equals(context.getResources().getString(R.string.leisure_talent))) {
            typedArray = context.getResources().obtainTypedArray(R.array.leisureTalentImg);
            projectName = context.getResources().getStringArray(R.array.leisureTalentName);

        } else if (categoryName.equals(context.getResources().getString(R.string.programming))) {
            typedArray = context.getResources().obtainTypedArray(R.array.programmingImg);
            projectName = context.getResources().getStringArray(R.array.programmingName);
        }
        //依照各專業項目的總數給予相對之圖片陣列空間,以便跑迴圈將typedArray內的值取出並帶入int[]的projectImg內
        projectImg = new int[projectName.length];
        for (int i = 0; i < projectImg.length; i++) {
            projectImg[i] = typedArray.getResourceId(i, -1);
        }
        //使用完android文件表示需釣譽recycler(),要不可能會出現OutOfMemory內存不足之錯誤訊息
        typedArray.recycle();
        //將專業項目名稱與圖片包成各個CourseArticleCategoryData,並添加至courseArticleCategoryDataList中
        for (int i = 0; i < projectName.length; i++) {
            courseArticleCategoryDataList.add(new CourseArticleCategoryData(projectImg[i], projectName[i]));
        }
        return courseArticleCategoryDataList;
    }


    public static List<CourseArticleData> takeCourseArticleProjectDataList(Context context, String projectName) {

        List<CourseArticleData> courseArticleProjectDataList = new ArrayList<>();

//        //將傳進的專案類別先做比對,相輔則給予對應之陣列數據
//        if (projectName.equals(context.getResources())) {
//
//
//        }
//
//
//        //依照各專業項目的總數給予相對之圖片陣列空間,以便跑迴圈將typedArray內的值取出並帶入int[]的projectImg內
//        projectImg = new int[projectName.length];
//        for (int i = 0; i < projectImg.length; i++) {
//            projectImg[i] = typedArray.getResourceId(i, -1);
//        }
//        //使用完android文件表示需釣譽recycler(),要不可能會出現OutOfMemory內存不足之錯誤訊息
//        typedArray.recycle();
//        //將專業項目名稱與圖片包成各個CourseArticleCategoryData,並添加至courseArticleCategoryDataList中
//        for (int i = 0; i < projectName.length; i++) {
//            courseArticleCategoryDataList.add(new CourseArticleCategoryData(projectImg[i], projectName[i]));
//        }
        return courseArticleProjectDataList;
    }
}

