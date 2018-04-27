package nom.cp101.master.master;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yujie on 2018/4/25.
 */

//此類別為心得文章需呼叫的各method
public class ExperienceData{

    static List<ExperienceViewPagerData> experienceViewPagerDataList;
    static List<ExperienceArticleData> experienceArticleDataList;

    //db存著置在ExperienceViewPager內的ExperienceViewPagerData陣列
    public static List<ExperienceViewPagerData> takeExperienceViewPagerDataList() {
        experienceViewPagerDataList=new ArrayList<>();

        experienceViewPagerDataList.add(new ExperienceViewPagerData(R.drawable.a1));
        experienceViewPagerDataList.add(new ExperienceViewPagerData(R.drawable.a2));
        experienceViewPagerDataList.add(new ExperienceViewPagerData(R.drawable.a3));
        experienceViewPagerDataList.add(new ExperienceViewPagerData(R.drawable.a1));
        experienceViewPagerDataList.add(new ExperienceViewPagerData(R.drawable.a2));
        experienceViewPagerDataList.add(new ExperienceViewPagerData(R.drawable.a3));

        return  experienceViewPagerDataList;
    }


    //db將ExperienceArticleData的物件包裝成一個ExperienceArticleDataList內的陣列便於呼叫
    //回傳list為顯示於心得文章中
    public static final List<ExperienceArticleData> takeExperienceArticleDataList() {

        experienceArticleDataList = new LinkedList();

        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "7","1",R.drawable.a1, new Date(), "111",0));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "8","2",R.drawable.a1, new Date(), "777",0));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "9","3",R.drawable.a2, new Date(), "555",0));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "10","4",R.drawable.a2, new Date(), "111",0));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "11","5",R.drawable.a3, new Date(), "222",0));
        experienceArticleDataList.add(new ExperienceArticleData(android.R.drawable.sym_action_email, "12","6",R.drawable.a3, new Date(), "333",0));

        return experienceArticleDataList;
    }



}
