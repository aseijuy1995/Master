package nom.cp101.master.master.ExperienceArticle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by yujie on 2018/4/20.
 */

//心得文章列表,position=0
public class ExperienceArticleViewPagerFragmentStatePagerAdapter extends PagerAdapter {
    //心得文章列表內,置入ExperienceImg的圖片數據
    List<ImageView> experienceArticleViewPagerDataList;
    int num = 300;

    public ExperienceArticleViewPagerFragmentStatePagerAdapter(Context context) {
        experienceArticleViewPagerDataList = ExperienceArticleAllData.takeExperienceViewPagerDataList(context);
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(View container, int position) {
        try {
            ((ViewPager) container).addView((View) experienceArticleViewPagerDataList.get(position % experienceArticleViewPagerDataList.size()), 0);
        } catch (Exception e) {
        }
        return experienceArticleViewPagerDataList.get(position % experienceArticleViewPagerDataList.size());
    }

}
