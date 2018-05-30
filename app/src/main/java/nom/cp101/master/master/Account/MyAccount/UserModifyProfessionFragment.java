package nom.cp101.master.master.Account.MyAccount;

import android.annotation.SuppressLint;
import android.content.Context;
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

import static nom.cp101.master.master.Main.Common.showToast;

public class UserModifyProfessionFragment extends Fragment implements View.OnClickListener{
    private static String URL_INTENT = "/UserInfo";
    private static final String TAG = "UserModifyProfessionFragment";
    private RecyclerView userModifyProfessionRecyclerShowItem;
    private Button userModifyProfessionButtonNewItem, userModifyProfessionButtonCancel;
    private UserProfessionAdapter userProfessionAdapter;
    private String account = ""; // 存放帳號用
    private MyTask task;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_modify_profession_fragment, container, false);
        context = getActivity();

        findView(view);

        // 監聽按鈕
        userModifyProfessionButtonNewItem.setOnClickListener(this);
        userModifyProfessionButtonCancel.setOnClickListener(this);


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
        userModifyProfessionRecyclerShowItem.setLayoutManager(new LinearLayoutManager(context));
        userProfessionAdapter = new UserProfessionAdapter(userModifyProfessionList, account, getActivity()); // 傳入List
        userModifyProfessionRecyclerShowItem.setAdapter(userProfessionAdapter);
        return view;
    }

    // 初始化 BJ4
    private void findView(View view) {
        userModifyProfessionButtonCancel = view.findViewById(R.id.user_bt_modify_profession_cancel);
        userModifyProfessionRecyclerShowItem = view.findViewById(R.id.user_recycler_modify_profession);
        userModifyProfessionButtonNewItem = view.findViewById(R.id.user_bt_modify_profession_new);
    }

    // 新增會員專業項目
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 新增專業
            case R.id.user_bt_modify_profession_new:
                // 從DB拿到專業大類別
                ArrayList<String> allProfessionCategory = getAllProfessionCategory();
                // 轉成陣列
                final String[] allProfessionCategoryArray = allProfessionCategory.toArray(new String[0]);

                // 開始 AlertDialog
                AlertDialog.Builder professionCategoryAlertDialog = new AlertDialog.Builder(context);
                professionCategoryAlertDialog.setTitle(context.getResources().getString(R.string.selectProfessionCagegory));
                professionCategoryAlertDialog.setPositiveButton(context.getResources().getString(R.string.cancel),null);
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
                        AlertDialog.Builder professionItemAlertDialog = new AlertDialog.Builder(context);
                        professionItemAlertDialog.setTitle(context.getResources().getString(R.string.selectProfessionItem));
                        professionItemAlertDialog.setPositiveButton( context.getResources().getString(R.string.cancel),null);
                        professionItemAlertDialog.setNeutralButton(context.getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                UserModifyProfessionFragment.this.onClick(userModifyProfessionButtonNewItem);
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
                                        showToast(context, context.getResources().getString(R.string.repeatProfession));
                                    }
                                }

                                // 若沒重複則執行
                                if (duplicate) {
                                    // 開始上傳資料, 傳入帳號和新增的項目
                                    int result = updataUserProfession(account, selectProfession);

                                    if (result != 0) {
                                        // 將項目更新至畫面上
                                        showToast(context, context.getResources().getString(R.string.addSuccessed));
                                        User newUser = new User();
                                        newUser.setUserProfession(selectProfession);
                                        newUser.setDelete("Delete");
                                        userProfessionAdapter.addItem(newUser);
                                    } else {
                                        showToast(context, context.getResources().getString(R.string.addFailed));
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

                break;

            // 返回會員資訊畫面
            case R.id.user_bt_modify_profession_cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }



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
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {}
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
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {}
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
        } catch (Exception e) {}
        return result;
    }

}

