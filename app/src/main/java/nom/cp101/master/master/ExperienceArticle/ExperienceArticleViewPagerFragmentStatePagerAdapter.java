package nom.cp101.master.master.ExperienceArticle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by yujie on 2018/4/20.
 */

//心得文章列表,position=0
public class ExperienceArticleViewPagerFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    //心得文章列表內,置入ExperienceImg的圖片數據
    List<ExperienceArticleViewPagerData> experienceArticleViewPagerDataList;

    public ExperienceArticleViewPagerFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        //傳進viewPager所需圖片陣列
        experienceArticleViewPagerDataList = ExperienceArticleAllData.takeExperienceViewPagerDataList();
    }

    @Override
    public Fragment getItem(int position) {
        //取得experienceViewPagerDataList每一index內的物件ExperienceViewPagerData
        ExperienceArticleViewPagerData experienceArticleViewPagerData = experienceArticleViewPagerDataList.get(position);
        ExperienceArticleViewPagerFragment experienceArticleViewPagerFragment =new ExperienceArticleViewPagerFragment();
        //使用bundle將數據傳進articleViewPagerFrag內
        Bundle bundle=new Bundle();
        bundle.putInt("experienceArticleViewPagerDataImg", experienceArticleViewPagerData.getExperienceImg());
        experienceArticleViewPagerFragment.setArguments(bundle);

        return experienceArticleViewPagerFragment;
    }

    //viewPager筆數
    @Override
    public int getCount() {
        return  experienceArticleViewPagerDataList.size();
    }

}
