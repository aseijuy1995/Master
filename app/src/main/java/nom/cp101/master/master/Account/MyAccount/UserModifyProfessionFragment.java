package nom.cp101.master.master.Account.MyAccount;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class UserModifyProfessionFragment extends Fragment {

    private static String URL_INTENT = "/UserInfo";
    private static final String TAG = "UserModifyProfessionFragment";
    private RecyclerView userModifyProfessionRecyclerShowItem;
    private Button userModifyProfessionButtonNewItem, userModifyProfessionButtonCancel;
    private UserProfessionAdapter userProfessionAdapter;
    private String account = ""; // 存放帳號用
    private MyTask task;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_modify_profession_fragment, container, false);
        findView(view);

        List<User> userModifyProfessionList = new ArrayList<>();

        // 拿到會員頁傳過來的專業陣列
        Bundle bundle = getArguments();
        if (bundle != null) {

            // 把傳過來的帳號存起來
            account = bundle.getString("account");
            // 傳過來的專業技能
            ArrayList<String> userProfessionList = bundle.getStringArrayList("profession");

            for (String str : userProfessionList) {

                // 依序拿出來並存進User陣列裡面, 並多加入刪除
                User user = new User();
                user.setUserProfession(str);
                user.setDelete("Delete"); // 加入刪除
                userModifyProfessionList.add(user);
            }
        }


        // Start Recycler View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        userModifyProfessionRecyclerShowItem.setLayoutManager(linearLayoutManager);
        userProfessionAdapter = new UserProfessionAdapter(userModifyProfessionList, account, getActivity()); // 傳入List
        userModifyProfessionRecyclerShowItem.setAdapter(userProfessionAdapter);

        return view;
    }


    // 新增會員專業項目
    private Button.OnClickListener userModifyProfessionButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 新增專業
            if (v.getId() == R.id.user_bt_modify_profession_new) {

                // 從DB拿到專業大類別
                ArrayList<String> allProfessionCategory = getAllProfessionCategory();
                // 轉成陣列
                final String[] allProfessionCategoryArray = allProfessionCategory.toArray(new String[0]);

                // 開始 AlertDialog
                AlertDialog.Builder professionCategoryAlertDialog = new AlertDialog.Builder(getActivity());
                professionCategoryAlertDialog.setTitle("選擇專業類別");
                professionCategoryAlertDialog.setPositiveButton("取消",null);
                professionCategoryAlertDialog.setItems(allProfessionCategoryArray,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 拿到選擇的專業類別
                        String professionCategoryName = allProfessionCategoryArray[which];
                        // 拿到選擇的專業類別內的項目
                        ArrayList<String> allProfessionItem = getAllProfessionItem(professionCategoryName);
                        // 轉成字串陣列
                        final String[] allProfessionItemArray = allProfessionItem.toArray(new String[0]);
                        // 再創造一個 AlertDialog
                        AlertDialog.Builder professionItemAlertDialog = new AlertDialog.Builder(getActivity());
                        professionItemAlertDialog.setTitle("選擇專業項目");
                        professionItemAlertDialog.setPositiveButton("取消",null);
                        professionItemAlertDialog.setNeutralButton("上一頁", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                userModifyProfessionButton.onClick(userModifyProfessionButtonNewItem);
                            }
                        });
                        professionItemAlertDialog.setItems(allProfessionItemArray,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 拿出選擇的項目
                                String selectProfession =  allProfessionItemArray[which];
                                // 拿出目前User所擁有的項目
                                List<User> userAllProfession = userProfessionAdapter.getListUserProfession();

                                // 開始拿出並比對是否重複
                                Boolean duplicate = true;
                                for (int i = 0 ; i < userAllProfession.size(); i++) {
                                    User user = userAllProfession.get(i);
                                    String result = user.getUserProfession();
                                    // 依序拿出來在存進User陣列裡面
                                    if (result.equals(selectProfession)) {

                                        // 重複後的處理 ...
                                        duplicate = false;
                                        Toast.makeText(getActivity(), "專業項目重複選擇", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                // 若沒重複則執行
                                if (duplicate) {

                                    // 開始上傳資料, 傳入帳號和新增的項目
                                    int result = updataUserProfession(account, selectProfession);

                                    if (result != 0) {
                                        // 將項目更新至畫面上
                                        Toast.makeText(getActivity(), "新增成功", Toast.LENGTH_SHORT).show();
                                        User newUser = new User();
                                        newUser.setUserProfession(selectProfession);
                                        newUser.setDelete("Delete");
                                        userProfessionAdapter.addItem(newUser);
                                    } else {
                                        Toast.makeText(getActivity(), "新增失敗, 請聯絡管理員或稍後在試", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        // 設定專業項目 AlertDialog 寬度
                        AlertDialog alertDialog = professionItemAlertDialog.create();
                        alertDialog.show();
                        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                        params.width = 800;
                        alertDialog.getWindow().setAttributes(params);
                    }
                });

                // 設定專業大類別 AlertDialog 寬度
                AlertDialog alertDialog = professionCategoryAlertDialog.create();
                alertDialog.show();
                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.width = 800;
                alertDialog.getWindow().setAttributes(params);
            }

            // 返回會員資訊畫面
            if (v.getId() == R.id.user_bt_modify_profession_cancel) {

                getFragmentManager().popBackStack();
            }
        }
    };



    // 拿到所有專業類別
    @SuppressLint("LongLogTag")
    public ArrayList<String> getAllProfessionCategory () {
        String url = Common.URL + URL_INTENT;
        ArrayList<String> result = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAllProfessionCategory");
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }



    // 拿到選擇的所有專業項目
    @SuppressLint("LongLogTag")
    public ArrayList<String> getAllProfessionItem (String category) {
        String url = Common.URL + URL_INTENT;
        ArrayList<String> result = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAllProfessionItem");
        jsonObject.addProperty("category", category);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }


    // 增加 User 專業
    @SuppressLint("LongLogTag")
    public int updataUserProfession (String account, String profession) {
        String url = Common.URL + URL_INTENT;
        int result = 0;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "updataUserProfession");
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("profession", profession);
        task = new MyTask(url, jsonObject.toString());
        try {
            result = Integer.valueOf(task.execute().get());
            Log.d(TAG, "Input: " + result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }


    // 初始化 BJ4
    private void findView(View view) {
        userModifyProfessionButtonCancel = view.findViewById(R.id.user_bt_modify_profession_cancel);
        userModifyProfessionRecyclerShowItem = view.findViewById(R.id.user_recycler_modify_profession);
        userModifyProfessionButtonNewItem = view.findViewById(R.id.user_bt_modify_profession_new);
        // 監聽按鈕
        userModifyProfessionButtonNewItem.setOnClickListener(userModifyProfessionButton);
        userModifyProfessionButtonCancel.setOnClickListener(userModifyProfessionButton);

    }
}
