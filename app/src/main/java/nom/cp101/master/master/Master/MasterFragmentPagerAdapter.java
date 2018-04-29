package nom.cp101.master.master.Master;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

//橋接CourseArticleFragment, ExperienceArticleFragment, MessageFragment, NoticeFragment, InformationFragment各個主功能Fragment的橋接器
public class MasterFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> listMaster;

    //建構子
    public MasterFragmentPagerAdapter(FragmentManager fm, List<Fragment> listMaster) {
        super(fm);
        this.listMaster = listMaster;
    }

    //回傳當前呈現之frag
    @Override
    public Fragment getItem(int position) {
        return listMaster.get(position);
    }


    //回傳數量
    @Override
    public int getCount() {
        return listMaster.size();
    }
}
