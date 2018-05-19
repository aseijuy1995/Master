package nom.cp101.master.master.Master;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.JsonObject;

import java.util.List;

import nom.cp101.master.master.Account.AccountFragment;
import nom.cp101.master.master.CourseArticle.CourseFragment;
import nom.cp101.master.master.ExperienceArticle.ExperienceArticleFragment;
import nom.cp101.master.master.ExperienceArticleActivity.ExperienceArticleAppendActivity;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MainService;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.Message.CLASS.ChatRoomFragment;
import nom.cp101.master.master.Notification.NotificationFragment;
import nom.cp101.master.master.R;

public class Master extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MasterActivity";
    Toolbar toolbarMaster;
    BottomNavigationView bnvMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_main);
        findViews();
        setSupportActionBar(toolbarMaster);


        bnvMaster.setOnNavigationItemSelectedListener(this);
        initContent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent ServiceIntent = new Intent(this, MainService.class);
        startService(ServiceIntent);
    }

    private void findViews() {
        toolbarMaster = (Toolbar) findViewById(R.id.toolBarMaster);
        bnvMaster = (BottomNavigationView) findViewById(R.id.bnvMaster);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;

        switch (item.getItemId()) {
            case R.id.item_course:
                fragment = new CourseFragment();
                switchFragment(fragment);
                setTitle(R.string.course_article);
                return true;

            case R.id.item_experience:
                fragment = new ExperienceArticleFragment();
                switchFragment(fragment);
                setTitle(R.string.experience_article);
                return true;

            case R.id.item_message:
                fragment = new ChatRoomFragment();
                switchFragment(fragment);
                setTitle(R.string.message);
                return true;

            case R.id.item_notification:
                fragment = new NotificationFragment();
                switchFragment(fragment);
                setTitle(R.string.notification);
                return true;

            case R.id.item_information:
                fragment = new AccountFragment();
                switchFragment(fragment);
                setTitle(R.string.information);
                return true;

            default:
                initContent();
                break;
        }
        return false;
    }

    private void initContent() {
        Fragment fragment = new CourseFragment();
        switchFragment(fragment);
        setTitle(R.string.course_article);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameMaster, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchView searchMaster = (SearchView) menu.findItem(R.id.searchMaster).getActionView();
        //抓取隱藏在searchView內的AutoCompleteTextView
        AutoCompleteTextView autoCompleteTextViewMaster = (AutoCompleteTextView) searchMaster.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //設定置入於SearchView內的AutoCompleteTextView橋接器
        setSearchAutoComplete(searchMaster, autoCompleteTextViewMaster);
        return true;
    }

    //設置SearcjAutoComplete
    private void setSearchAutoComplete(final SearchView searchMaster, AutoCompleteTextView autoCompleteTextViewMaster) {
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
                openPost();
                break;
        }
        return true;
    }


    private void openPost() {
        Intent intent = new Intent(this, ExperienceArticleAppendActivity.class);
        startActivity(intent);
    }


    private void connecServer() {
        String user_id = Common.getUserName(this);
        if (user_id != null) {
            Common.connectServer(this, user_id);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        doUnbindService();
    }

    public String findRoomName(String user_id, String room_name) {
        if (Common.networkConnected(this)) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findRoomName");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("room_name", room_name);
            String result = null;
            try {
                result = new MyTask(url, jsonObject.toString()).execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (result.isEmpty()) {
                Log.d(TAG, "Find room name 失敗");
                return null;
            } else {
                Log.d(TAG, "Find room name 成功");
                return result;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(this, "no network");
            return null;
        }
    }


}

