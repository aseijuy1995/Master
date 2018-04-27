package nom.cp101.master.master;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yujie on 2018/4/25.
 */

//此類別為課程文章需呼叫的各method
public class ExperienceData{
    static List experienceImgList;

    //存著置在ExperienceViewPager內的ExperienceViewPagerData陣列
    public static List<ExperienceViewPagerData> takeExperienceViewPagerDataList() {
        experienceImgList=new ArrayList<>();

        experienceImgList.add(new ExperienceViewPagerData(R.drawable.a1));
        experienceImgList.add(new ExperienceViewPagerData(R.drawable.a2));
        experienceImgList.add(new ExperienceViewPagerData(R.drawable.a3));
        experienceImgList.add(new ExperienceViewPagerData(R.drawable.a1));
        experienceImgList.add(new ExperienceViewPagerData(R.drawable.a2));
        experienceImgList.add(new ExperienceViewPagerData(R.drawable.a3));

        return  experienceImgList;
    }


    //專業類別之名稱與圖示,將ArticleViewPagerProjectData的物件包裝成一個ArticleViewPagerData內的陣列便於呼叫
    //回傳list為顯示於課程文章上ViewPager中
    public static final List<ExperienceArticleData> takeExperienceArticleDataList() {

        List<ExperienceArticleData> experienceArticleDataList = new LinkedList();

        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "7",R.drawable.a1, new Date(),0, "111"));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "8",R.drawable.a1, new Date(),0, "777"));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "9",R.drawable.a2, new Date(),0, "555"));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "10",R.drawable.a2, new Date(),0, "111"));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "11",R.drawable.a3, new Date(),0, "222"));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "12",R.drawable.a3, new Date(),0, "333"));

        return experienceArticleDataList;
    }



}
