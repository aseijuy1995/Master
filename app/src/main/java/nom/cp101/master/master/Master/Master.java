package nom.cp101.master.master.Master;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Account.AccountFragment;
import nom.cp101.master.master.CourseArticle.CourseArticleFragment;
import nom.cp101.master.master.ExperienceArticle.ExperienceArticleFragment;
import nom.cp101.master.master.Message.MessageFragment;
import nom.cp101.master.master.Notification.NotificationFragment;
import nom.cp101.master.master.R;

//首頁Activity
public class Master extends AppCompatActivity {
    //置入主頁面所需元件
    Toolbar toolbarMaster;
    TabLayout tabMaster;
    ViewPager viewPagerMaster;
    SearchView searchMaster;
    AutoCompleteTextView autoCompleteTextViewMaster;

    //課程文章frag
    CourseArticleFragment courseArticleFragment;
    //心得文章frag
    ExperienceArticleFragment experienceArticleFragment;
    //私訊frag
    MessageFragment messageFragment;
    //通知frag
    NotificationFragment notificationFragment;
    //資料frag
    AccountFragment accountFragment;
    //記錄置入TabLayout內,與Viewpager橋接的個主功能Fragment
    List<Fragment> listMaster;
    //連接ViewPager與主功能Fragment的橋接器
    MasterFragmentPagerAdapter masterFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_main);
        //初始化元件
        findViews();
        //設定自訂ToolBar套為主頁面之ActionBar
        setSupportActionBar(toolbarMaster);
        //主功能頁面Fragment與搭載在TabLayout內ViewPager的橋接設定
        setViewPager();
        //設定置入TabLayout的圖片
        setTabLayout();
    }

    //設定置入TabLayout的圖片
    private void setTabLayout() {
        //tablayout各功能圖示
        int[] imgs = {R.drawable.tab_article,
                R.drawable.tab_experience,
                R.drawable.tab_message,
                R.drawable.tab_notice,
                R.drawable.tab_information};
        //TabLayout接上viewPager
        tabMaster.setupWithViewPager(viewPagerMaster, true);
        //設置tablayout各功能圖示
        for (int i = 0; i < listMaster.size(); i++) {
            tabMaster.getTabAt(i).setIcon(imgs[i]);
        }
        //取得tabLayout各鈕的position以便判斷顯示隱藏toolBar,並設其標題名
        tabMaster.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerMaster) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int position = tab.getPosition();

                if (position == 0) {
                    toolbarMaster.setVisibility(View.VISIBLE);
                    setTitle(R.string.courseArticle);

                } else if (position == 1) {
                    toolbarMaster.setVisibility(View.VISIBLE);
                    setTitle(R.string.experienceArticle);

                } else {
                    toolbarMaster.setVisibility(View.GONE);
                }
            }
        });

    }

    //主功能頁面Fragment與搭載在TabLayout內ViewPager的橋接設定
    private void setViewPager() {
        listMaster = new ArrayList<>();

        //此區添加個主功能的Fragment,設置完成請將替代的Fragment移除
        courseArticleFragment = new CourseArticleFragment();
        experienceArticleFragment = new ExperienceArticleFragment();
        messageFragment = new MessageFragment();
        notificationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();

        //此區置換個主功能的Fragment,設置完成請將添加對應的Fragment移除
        listMaster.add(courseArticleFragment);
        listMaster.add(experienceArticleFragment);
        listMaster.add(messageFragment);
        listMaster.add(notificationFragment);
        listMaster.add(accountFragment);

        //主畫面Master-其內掛載TabLayout內的ViewPager與裝有Fragment的list橋接設置
        masterFragmentPagerAdapter = new MasterFragmentPagerAdapter(getSupportFragmentManager(), listMaster);
        viewPagerMaster.setAdapter(masterFragmentPagerAdapter);
    }

    //初始化元件-ToolBar, TabLayout, ViewPager
    private void findViews() {
        toolbarMaster = (Toolbar) findViewById(R.id.toolBarMaster);
        tabMaster = (TabLayout) findViewById(R.id.tabMaster);
        viewPagerMaster = (ViewPager) findViewById(R.id.viewPagerMaster);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //取得toolbar的menu樣式檔
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        //通過getActionView()將menu上的item轉為view使用
        searchMaster = (SearchView) menu.findItem(R.id.searchMaster).getActionView();
        //抓取隱藏在searchView內的AutoCompleteTextView
        autoCompleteTextViewMaster = (AutoCompleteTextView) searchMaster.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //設定置入於SearchView內的AutoCompleteTextView橋接器
        setSearchAutoComplete();

        return true;
    }

    //設置SearcjAutoComplete
    private void setSearchAutoComplete() {
        //取得專業項目之所有名稱
        List<String> searchContent = MasterAllData.takeProjectNameList();
        //autoCompleteTextView橋接自定viewItem
        autoCompleteTextViewMaster.setAdapter(new ArrayAdapter<>(this,
                R.layout.master_search_autocomplete_item,
                R.id.tv_autocomplete,
                searchContent));

        //設置提示條件-輸入長度取決於何時顯示
        autoCompleteTextViewMaster.setThreshold(0);
        //選取autoCompleteTextView提示文字,前往選取之頁面
        autoCompleteTextViewMaster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取得點擊autoCompleteTextView內的Item值
                String searchContent = parent.getItemAtPosition(position).toString();
                //設置查詢數據給予searchView並提交(true)
                searchMaster.setQuery(searchContent, true);

//                //選取搜尋之內容並前往相關頁面
//                Intent i=new Intent(Master.this, CourseArticleActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("categoryName","水類運動");
//                i.putExtras(bundle);
//                startActivity(i);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //item為addText時(新增文章),先做Toast
            case R.id.addTextMaster:
                Toast.makeText(getApplicationContext(), "Click addText", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
