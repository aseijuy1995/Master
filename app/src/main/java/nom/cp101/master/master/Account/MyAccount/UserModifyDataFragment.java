package nom.cp101.master.master.Account.MyAccount;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class UserModifyDataFragment extends Fragment {

    private static String URL_INTENT = "/UserInfo";
    private static final String TAG = "UserModifyDataFragment";
    private RadioGroup userModifyRadioGender;
    private RadioButton userModifyRadioMen, userModifyRadioWomen;
    private Button userModifyButtonCancel, userModifyButtonOK;
    private EditText userModifyTextName, userModifyTextAddress, userModifyTextTel, userModifyTextProfile;
    private String userModifyAccount; // 存會員ID ...
    private int userModifyGender; // 存會員性別 ...
    private MyTask task;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_modify_data_fragment, container, false);
        findView(view);

        // 從會員資訊頁面拿到值 ...
        Bundle bundle = getArguments();
        if (bundle != null) {
            String getModifyGender = bundle.getString("gender"); // 拿到性別
            String getModifyAccount = bundle.getString("id"); // 拿到帳號
            String getModifyName = bundle.getString("name"); // 拿到名字
            String getModifyAddress = bundle.getString("address"); // 拿到地址
            String getModifyTel = bundle.getString("tel"); // 拿到電話
            String getModifyProfile = bundle.getString("profile"); // 拿到簡介

            // 判斷傳過來的性別貼在 Radio Button 上 ...
            if (getModifyGender.equals("Men")) {
                userModifyRadioMen.setChecked(true);
            } else if (getModifyGender.equals("Women")){
                userModifyRadioWomen.setChecked(true);
            }

            // 將文字貼到 UI 上 ...
            userModifyTextName.setText(getModifyName);
            userModifyTextAddress.setText(getModifyAddress);
            userModifyTextTel.setText(getModifyTel);
            userModifyTextProfile.setText(getModifyProfile);
            userModifyAccount = getModifyAccount;
        }
        return view;
    }


    // 點擊事件處理 ...
    private Button.OnClickListener userModifyButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.user_modify_bt_ok) {  // 跳出警告視窗, 並確認是否修改會員資料 ...
                new AlertDialog.Builder(getActivity())
                        .setTitle("即將修改會員資料")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 取得資料 ...
                                String userModifyName = userModifyTextName.getText().toString().trim();
                                String userModifyAddress = userModifyTextAddress.getText().toString().trim();
                                String userModifyTel = userModifyTextTel.getText().toString().trim();
                                String userModifyProfile = userModifyTextProfile.getText().toString();

                                // 拿到性別(數字) ...
                                for(int i = 0 ; i < userModifyRadioGender.getChildCount() ; i++) {
                                    RadioButton radioButton = (RadioButton)userModifyRadioGender.getChildAt(i);
                                    if(radioButton.isChecked()) {
                                        userModifyGender = i + 1;
                                        userModifyRadioWomen.setError(null);
                                    }
                                }

                                // 開始檢查資料是否合法 ...
                                boolean isValid = true;
                                if (userModifyName.isEmpty()) {
                                    userModifyTextName.setError("你的名字是?");
                                    isValid = false;
                                } if (userModifyAddress.isEmpty()) {
                                    userModifyTextAddress.setError("你的地址是?");
                                    isValid = false;
                                } if (userModifyTel.isEmpty()) {
                                    userModifyTextTel.setError("你的聯絡號碼是?");
                                    isValid = false;
                                } if (userModifyTel.isEmpty()) {
                                    userModifyTextProfile.setError("建議填上個人簡介介紹您自己...");
                                    isValid = false;
                                } if (userModifyGender == 0) {
                                    userModifyRadioWomen.setError("你的性別是?");
                                    isValid = false;
                                } if (isValid) {

                                    // 開始 Updata ...
                                    if (updataUserInfo(userModifyName, userModifyAddress, userModifyTel, userModifyProfile) == 0) {
                                        Toast.makeText(getActivity(),"修改失敗...請稍後再試...",Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_LONG).show();
                                        getFragmentManager().popBackStack();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
            if (v.getId() == R.id.user_modify_bt_cancel) { // 按下取消返回會員資訊頁面 ...

                getFragmentManager().popBackStack();
            }
        }
    };


    private void findView(View view) {
        userModifyRadioGender = view.findViewById(R.id.user_modify_rb_gender);
        userModifyRadioMen = view.findViewById(R.id.user_modify_rb_men);
        userModifyRadioWomen = view.findViewById(R.id.user_modify_rb_women);
        userModifyTextName = view.findViewById(R.id.user_modify_et_name);
        userModifyTextAddress = view.findViewById(R.id.user_modify_et_address);
        userModifyTextTel = view.findViewById(R.id.user_modify_et_tel);
        userModifyTextProfile = view.findViewById(R.id.user_modify_et_profile);
        userModifyButtonOK = view.findViewById(R.id.user_modify_bt_ok);
        userModifyButtonCancel = view.findViewById(R.id.user_modify_bt_cancel);
        // 監聽點擊事件 ...
        userModifyButtonOK.setOnClickListener(userModifyButton);
        userModifyButtonCancel.setOnClickListener(userModifyButton);
    }


    // Servlet 修改會員資料 ...
    @SuppressLint("LongLogTag")
    public int updataUserInfo(String Name, String address, String tel, String profile) {
        int editResult = 0;
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "editMemberInfo");
        jsonObject.addProperty("account",userModifyAccount);
        jsonObject.addProperty("name", Name);
        jsonObject.addProperty("gender", userModifyGender);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("tel", tel);
        jsonObject.addProperty("profile", profile);
        task = new MyTask(url, jsonObject.toString());
        try {
            editResult = Integer.valueOf(task.execute().get());
            Log.d(TAG, "Input: " + editResult);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return editResult;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (task != null) {
            task.cancel(true);
        }
    }


}
