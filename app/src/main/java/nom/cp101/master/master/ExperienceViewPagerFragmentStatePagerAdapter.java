package nom.cp101.master.master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yujie on 2018/4/20.
 */

//文章列表,position=0
public class ExperienceViewPagerFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    //文章列表內,置入ExperienceImg的圖片數據
    List<ExperienceImg> experienceImgList;

    public ExperienceViewPagerFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        //傳進viewPager所需圖片陣列
        experienceImgList=getProjectData();
    }

    @Override
    public Fragment getItem(int position) {
        //取得當前顯示的ArticleImg內存放的數據
        ExperienceImg experienceImg= experienceImgList.get(position);
        ExperienceViewPagerFragment experienceViewPagerFragment=new ExperienceViewPagerFragment();
        //使用bundle傳進數據進articleViewPagerFrag內
        Bundle bundle=new Bundle();
        bundle.putInt("experienceImg", experienceImg.getImg());
        experienceViewPagerFragment.setArguments(bundle);

        return experienceViewPagerFragment;
    }

    //viewPager筆數
    @Override
    public int getCount() {
        return  experienceImgList.size();
    }


    //取得db存放之圖片
    private List<ExperienceImg> getProjectData() {
        experienceImgList=new ArrayList<>();

        experienceImgList.add(new ExperienceImg(R.drawable.ic_launcher_background));
        experienceImgList.add(new ExperienceImg(R.drawable.ic_launcher_foreground));
        experienceImgList.add(new ExperienceImg(R.drawable.ic_launcher_background));
        experienceImgList.add(new ExperienceImg(R.drawable.ic_launcher_foreground));
        experienceImgList.add(new ExperienceImg(R.drawable.ic_launcher_background));
        experienceImgList.add(new ExperienceImg(R.drawable.ic_launcher_foreground));

        return  experienceImgList;
    }
}
