package nom.cp101.master.master.Master;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.JsonObject;

import nom.cp101.master.master.Account.AccountMain.AccountFragment;
import nom.cp101.master.master.Account.MyAccount.UserFragment;
import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.CourseArticle.CourseFragment;
import nom.cp101.master.master.ExperienceArticle.ExperienceFragment;
import nom.cp101.master.master.Main.Common;
//import nom.cp101.master.master.Main.MainService;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.Message.CLASS.ChatRoomFragment;
import nom.cp101.master.master.Notification.NotificationFragment;
import nom.cp101.master.master.R;

public class MasterActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MasterActivity";
    public static BottomNavigationView bnvMaster;

    public static final String SEND_USER = "SEND_USER";
    public static final String LOGIN_BACK = "LOGIN_BACK";
    public static final String LOGIN = "LOGIN";
    public static final String SEND_COURSE = "SEND_COURSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_main);
        findViews();

        bnvMaster.setOnNavigationItemSelectedListener(this);
        //取消BottomNavigationView動畫
        MasterBNVHelper.disableShiftMode(bnvMaster);
        bnvMaster.setSelectedItemId(R.id.item_course);
    }

    private void findViews() {
        bnvMaster = (BottomNavigationView) findViewById(R.id.bnvMaster);
    }

    private void initContent() {
        Fragment fragment = new CourseFragment();
        switchFragment(fragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameMaster, fragment);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.item_course:
                fragment = new CourseFragment();
                switchFragment(fragment);
                return true;

            case R.id.item_experience:
                fragment = new ExperienceFragment();
                switchFragment(fragment);
                return true;

            case R.id.item_message:
                fragment = new ChatRoomFragment();
                switchFragment(fragment);
                return true;

//            case R.id.item_notification:
//                fragment = new NotificationFragment();
//                switchFragment(fragment);
//                return true;

            case R.id.item_information:
                if (Common.checkUserName(this, Common.getUserName(this))) {
                    fragment = new AccountFragment();
                    switchFragment(fragment);
                }
                return true;

            default:
                initContent();
                break;
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //貼文前往user資訊
        if (intent.getStringExtra(SEND_USER) != null) {
            if (intent.getStringExtra(SEND_USER).equals(SEND_USER)) {
                bnvMaster.setSelectedItemId(R.id.item_information);
                AccountFragment accountFragment = new AccountFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SEND_USER, SEND_USER);
                accountFragment.setArguments(bundle);
                switchFragment(accountFragment);
            }
        }

        //當登入頁面返回主頁,判斷當前bottomNavigation-select?
        if (intent.getStringExtra(LOGIN_BACK) != null) {
            if (intent.getStringExtra(LOGIN_BACK).equals(LOGIN_BACK)) {
                if (bnvMaster.getSelectedItemId() == R.id.item_information) {
                    bnvMaster.setSelectedItemId(R.id.item_course);
                }
            }
        }

        //當登入後判斷當前bottomNavigation-select
        if (intent.getStringExtra(LOGIN) != null) {

            if (intent.getStringExtra(LOGIN).equals(LOGIN)) {
                switch (bnvMaster.getSelectedItemId()) {
                    case R.id.item_course:
                        bnvMaster.setSelectedItemId(R.id.item_course);
                        break;

                    case R.id.item_experience:
                        bnvMaster.setSelectedItemId(R.id.item_experience);
                        break;

                    case R.id.item_message:
                        bnvMaster.setSelectedItemId(R.id.item_message);
                        break;

//                    case R.id.item_notification:
//                        bnvMaster.setSelectedItemId(R.id.item_notification);
//                        break;

                    case R.id.item_information:
                        bnvMaster.setSelectedItemId(R.id.item_information);
                        break;
                }
            }
        }

        //前往點選課程詳盡資訊
        if (intent.getStringExtra(SEND_COURSE) != null) {
            if (intent.getStringExtra(SEND_COURSE).equals(SEND_COURSE)) {
                bnvMaster.setSelectedItemId(R.id.item_information);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                AccountFragment accountFragment = new AccountFragment();
                Bundle bundle = new Bundle();
                Course course = (Course) intent.getSerializableExtra("course");
                bundle.putSerializable("course", course);
                accountFragment.setArguments(bundle);

                fm.popBackStack();
                ft.setCustomAnimations(R.anim.right_in,
                        R.anim.left_out,
                        R.anim.right_out,
                        R.anim.left_in)
                        .replace(R.id.frameMaster, accountFragment)
                        .commit();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment;
        switch (bnvMaster.getSelectedItemId()) {
//            case R.id.item_course:
//                fragment = new CourseFragment();
//                switchFragment(fragment);
//                break;

            case R.id.item_experience:
                bnvMaster.setSelectedItemId(R.id.item_experience);
                break;

            case R.id.item_message:
                bnvMaster.setSelectedItemId(R.id.item_message);
                break;

//            case R.id.item_notification:
//                bnvMaster.setSelectedItemId(R.id.item_notification);
//                break;
//            case R.id.item_information:
//                break;
        }
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

