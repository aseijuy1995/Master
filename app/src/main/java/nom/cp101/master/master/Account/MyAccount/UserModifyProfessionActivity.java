package nom.cp101.master.master.Account.MyAccount;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

public class UserModifyProfessionActivity extends AppCompatActivity implements View.OnClickListener {
    private static String URL_INTENT = "/UserInfo";
    private RecyclerView userModifyProfessionRecyclerShowItem;
    private ImageView userModifyProfessionImageViewNewItem, userModifyProfessionImageViewCancel;
    private UserModifyProfessionAdapter userProfessionAdapter;
    private String account = ""; // 存放帳號用
    private MyTask task;
    private Context context;
    private RecyclerTouchListener recyclerTouchListener;
    private List<User> userModifyProfessionList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_modify_profession_fragment);
        context = UserModifyProfessionActivity.this;
        findView();

        // 監聽按鈕
        userModifyProfessionImageViewCancel.setOnClickListener(this);
        userModifyProfessionImageViewNewItem.setOnClickListener(this);

        userModifyProfessionList = new ArrayList<>();
        // 拿到會員頁傳過來的專業陣列
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // 把傳過來的帳號存起來
            account = bundle.getString("account");
            // 傳過來的專業技能
            ArrayList<String> userProfessionList = bundle.getStringArrayList("profession");

            for (String str : userProfessionList) {
                // 依序拿出來並存進User陣列裡面, 並多加入刪除
                User user = new User();
                user.setUserProfession(str);
                userModifyProfessionList.add(user);
            }
        }

        // Start Recycler View
        userModifyProfessionRecyclerShowItem.setLayoutManager(new LinearLayoutManager(this));
        //add line
        userModifyProfessionRecyclerShowItem.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 傳入List,typeNumber辨識LayoutParams
        userProfessionAdapter = new UserModifyProfessionAdapter(userModifyProfessionList, 1);
        userModifyProfessionRecyclerShowItem.setAdapter(userProfessionAdapter);
        userModifyProfessionRecyclerShowItem.setItemAnimator(new DefaultItemAnimator());

        recyclerTouchListener = new RecyclerTouchListener(this, userModifyProfessionRecyclerShowItem);
        recyclerTouchListener.setSwipeOptionViews(R.id.delete_profession);
        recyclerTouchListener.setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, int position) {
                switch (viewID) {
                    case R.id.delete_profession:
                        User user = userModifyProfessionList.get(position);
                        // 傳進會員帳號, 專業名稱, 並呼叫DB刪除該筆資料
                        int result = deleteProfession(account, user.getUserProfession());
                        if (result != 0) {
                            // 刪除畫面資料(非DB資料 刪除畫面資料 DB還是存在所以 ... )
                            userModifyProfessionList.remove(position);
                            userProfessionAdapter.notifyDataSetChanged();
                            showToast(context, getResources().getString(R.string.success));
                        } else {
                            showToast(context, getResources().getString(R.string.failed));
                        }
                        break;
                }
            }
        });
    }

    private void findView() {
        userModifyProfessionImageViewCancel = (ImageView) findViewById(R.id.user_iv_modify_profession_cancel);
        userModifyProfessionImageViewNewItem = (ImageView) findViewById(R.id.user_iv_modify_profession_new);
        userModifyProfessionRecyclerShowItem = (RecyclerView) findViewById(R.id.user_recycler_modify_profession);
    }

    // 新增會員專業項目
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 新增專業
            case R.id.user_iv_modify_profession_new:
                // 從DB拿到專業大類別
                ArrayList<String> allProfessionCategory = getAllProfessionCategory();
                // 轉成陣列
                final String[] allProfessionCategoryArray = allProfessionCategory.toArray(new String[0]);

                // 開始 AlertDialog
                AlertDialog.Builder professionCategoryAlertDialog = new AlertDialog.Builder(context);
                professionCategoryAlertDialog.setTitle(getResources().getString(R.string.selectProfessionCagegory));
                professionCategoryAlertDialog.setPositiveButton(getResources().getString(R.string.cancel), null);
                professionCategoryAlertDialog.setItems(allProfessionCategoryArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 拿到選擇的專業類別
                        String professionCategoryName = allProfessionCategoryArray[which];
                        // 拿到選擇的專業類別內的項目
                        ArrayList<String> allProfessionItem = getAllProfessionItem(professionCategoryName);
                        // 轉成字串陣列
                        final String[] allProfessionItemArray = allProfessionItem.toArray(new String[0]);

                        // 再創造一個 AlertDialog
                        AlertDialog.Builder professionItemAlertDialog = new AlertDialog.Builder(context);
                        professionItemAlertDialog.setTitle(getResources().getString(R.string.selectProfessionItem));
                        professionItemAlertDialog.setPositiveButton(getResources().getString(R.string.cancel), null);
                        professionItemAlertDialog.setNeutralButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                                UserModifyProfessionActivity.this.onClick(userModifyProfessionImageViewNewItem);
                            }
                        });
                        professionItemAlertDialog.setItems(allProfessionItemArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 拿出選擇的項目
                                String selectProfession = allProfessionItemArray[which];

                                // 開始拿出並比對是否重複
                                Boolean duplicate = true;
                                for (int i = 0; i < userModifyProfessionList.size(); i++) {
                                    User user = userModifyProfessionList.get(i);
                                    String result = user.getUserProfession();
                                    // 依序拿出來在存進User陣列裡面
                                    if (result.equals(selectProfession)) {
                                        // 重複後的處理 ...
                                        duplicate = false;
                                        showToast(context, getResources().getString(R.string.repeatProfession));
                                    }
                                }
                                // 若沒重複則執行
                                if (duplicate) {
                                    // 開始上傳資料, 傳入帳號和新增的項目
                                    int result = updataUserProfession(account, selectProfession);
                                    if (result != 0) {
                                        // 將項目更新至畫面上
                                        User newUser = new User();
                                        newUser.setUserProfession(selectProfession);
                                        userModifyProfessionList.add(newUser);
                                        userProfessionAdapter.notifyDataSetChanged();

                                        showToast(context, getResources().getString(R.string.addSuccessed));
                                    } else {
                                        showToast(context, getResources().getString(R.string.addFailed));
                                    }
                                }
                            }
                        });
                        professionItemAlertDialog.create();
                        professionItemAlertDialog.show();
                    }
                });
                professionCategoryAlertDialog.create();
                professionCategoryAlertDialog.show();
                break;

            // 返回會員資訊畫面
            case R.id.user_iv_modify_profession_cancel:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    // 拿到所有專業類別
    public ArrayList<String> getAllProfessionCategory() {
        String url = Common.URL + URL_INTENT;
        ArrayList<String> result = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAllProfessionCategory");
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
        }
        return result;
    }

    // 拿到選擇的所有專業項目
    public ArrayList<String> getAllProfessionItem(String category) {
        String url = Common.URL + URL_INTENT;
        ArrayList<String> result = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAllProfessionItem");
        jsonObject.addProperty("category", category);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
        }
        return result;
    }

    // 增加 User 專業
    public int updataUserProfession(String account, String profession) {
        String url = Common.URL + URL_INTENT;
        int result = 0;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "updataUserProfession");
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("profession", profession);
        task = new MyTask(url, jsonObject.toString());
        try {
            result = Integer.valueOf(task.execute().get());
        } catch (Exception e) {
        }
        return result;
    }

    // 刪除會員專業
    public int deleteProfession(String account, String profession) {
        int editResult = 0;
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "deleteProfession");
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("profession", profession);
        MyTask task = new MyTask(url, jsonObject.toString());
        try {
            editResult = Integer.valueOf(task.execute().get());
        } catch (Exception e) {
        }
        return editResult;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModifyProfessionRecyclerShowItem.addOnItemTouchListener(recyclerTouchListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userModifyProfessionRecyclerShowItem.removeOnItemTouchListener(recyclerTouchListener);
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}

