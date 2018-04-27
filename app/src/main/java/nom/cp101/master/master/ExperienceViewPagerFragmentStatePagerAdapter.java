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

//心得文章列表,position=0
public class ExperienceViewPagerFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    //心得文章列表內,置入ExperienceImg的圖片數據
    List<ExperienceViewPagerData> experienceViewPagerDataList;

    public ExperienceViewPagerFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        //傳進viewPager所需圖片陣列
        experienceViewPagerDataList=ExperienceData.takeExperienceViewPagerDataList();
    }

    @Override
    public Fragment getItem(int position) {
        //取得experienceViewPagerDataList每一index內的物件ExperienceViewPagerData
        ExperienceViewPagerData experienceViewPagerData= experienceViewPagerDataList.get(position);
        ExperienceViewPagerFragment experienceViewPagerFragment=new ExperienceViewPagerFragment();
        //使用bundle將數據傳進articleViewPagerFrag內
        Bundle bundle=new Bundle();
        bundle.putInt("experienceViewPagerDataImg", experienceViewPagerData.getExperienceImg());
        experienceViewPagerFragment.setArguments(bundle);

        return experienceViewPagerFragment;
    }

    //viewPager筆數
    @Override
    public int getCount() {
        return  experienceViewPagerDataList.size();
    }

}
