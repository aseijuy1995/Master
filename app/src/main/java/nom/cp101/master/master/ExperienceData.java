package nom.cp101.master.master;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceData{
    static List experienceImgList;
    int Img;

//    public ExperienceData(int img) {
//        Img = img;
//    }
//
//    public int getImg() {
//        return Img;
//    }
//
//    public void setImg(int img) {
//        Img = img;
//    }

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



}
