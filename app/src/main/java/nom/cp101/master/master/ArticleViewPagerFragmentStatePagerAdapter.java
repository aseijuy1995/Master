package nom.cp101.master.master;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by yujie on 2018/4/20.
 */

public class ArticleViewPagerFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public ArticleViewPagerFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        ArticleViewPagerFragment articleViewPagerFragment=new ArticleViewPagerFragment();

        return articleViewPagerFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
