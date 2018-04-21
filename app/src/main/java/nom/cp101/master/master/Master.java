package nom.cp101.master.master;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

//Master APP Activity
public class Master extends AppCompatActivity {
    //置入主頁面所需元件
    TabLayout tab_master;
    ViewPager viewpager_master;
    //文章frag
    ArticleFragment articleFragment;
    //私訊frag
    MessageFragment messageFragment;
    //通知frag
    NoticeFragment noticeFragment;
    //資料frag
    InformationFragment informationFragment;

    //記錄置入TabLayout內,與Viewpager橋接的個主功能Fragment
    List<Fragment> list_master;
    //連接ViewPager與主功能Fragment的橋接器
    MasterFragmentPagerAdapter masterFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_main);

        //初始化元件
        findViews();
        //主功能頁面Fragment與搭載在TabLayout內ViewPager的橋接設定
        setViewPager();
        //設定置入TabLayout的圖片
        setTabLayout();
    }


    //設定置入TabLayout的圖片
    private void setTabLayout() {

        tab_master.setupWithViewPager(viewpager_master, true);
        //暫先設置數字1~4
        for (int i = 0; i < list_master.size(); i++) {
            tab_master.getTabAt(i).setText(String.valueOf(i + 1));
        }
    }


    //主功能頁面Fragment與搭載在TabLayout內ViewPager的橋接設定
    private void setViewPager() {
        list_master = new ArrayList<>();

        //此區添加個主功能的Fragment,設置完成請將替代的Fragment移除
        articleFragment = new ArticleFragment();
        messageFragment = new MessageFragment();
        noticeFragment = new NoticeFragment();
        informationFragment = new InformationFragment();

        //此區置換個主功能的Fragment,設置完成請將添加對應的Fragment移除
        list_master.add(articleFragment);
        list_master.add(messageFragment);
        list_master.add(noticeFragment);
        list_master.add(informationFragment);

        //主畫面Master-期內掛載在TabLayout內的ViewPager與包裝過的list橋接設置
        masterFragmentPagerAdapter = new MasterFragmentPagerAdapter(getSupportFragmentManager(), list_master);
        viewpager_master.setAdapter(masterFragmentPagerAdapter);
    }


    //初始化元件-TabLayout, ViewPager
    private void findViews() {
        tab_master = (TabLayout) findViewById(R.id.tab_master);
        viewpager_master = (ViewPager) findViewById(R.id.viewpager_master);
    }
}
