package nom.cp101.master.master;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

//橋接ArticleFragment, MessageFragment, NoticeFragment, InformationFragment各個主功能Fragment的橋接器
public class MasterFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list_master;

    //建構子
    public MasterFragmentPagerAdapter(FragmentManager fm, List<Fragment> list_master) {
        super(fm);
        this.list_master=list_master;
    }

    //回傳當前呈現之frag
    @Override
    public Fragment getItem(int position) {
        return list_master.get(position);
    }


    //回傳數量
    @Override
    public int getCount() {
        return list_master.size();
    }
}
