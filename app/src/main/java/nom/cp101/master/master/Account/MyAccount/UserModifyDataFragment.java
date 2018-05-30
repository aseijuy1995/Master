package nom.cp101.master.master.Account.MyAccount;

import android.annotation.SuppressLint;
import android.content.Context;
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

import static nom.cp101.master.master.Main.Common.showToast;

public class UserModifyDataFragment extends Fragment implements View.OnClickListener{

    private static String URL_INTENT = "/UserInfo";
    private static final String TAG = "UserModifyDataFragment";
    private RadioGroup userModifyRadioGender;
    private RadioButton userModifyRadioMen, userModifyRadioWomen;
    private Button userModifyButtonCancel, userModifyButtonOK;
    private EditText userModifyTextName, userModifyTextAddress, userModifyTextTel, userModifyTextProfile;
    private String userModifyAccount; // 存會員ID ...
    private int userModifyGender; // 存會員性別 ...
    private MyTask task;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_modify_data_fragment, container, false);
        context = getActivity();

        findView(view);
        // 監聽點擊事件 ...
        userModifyButtonOK.setOnClickListener(this);
        userModifyButtonCancel.setOnClickListener(this);

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
            if (getModifyGender.equals(context.getResources().getString(R.string.man))) {
                userModifyRadioMen.setChecked(true);
            } else if (getModifyGender.equals(context.getResources().getString(R.string.women))){
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
    }

    // 點擊事件處理 ...
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 跳出警告視窗, 並確認是否修改會員資料 ...
            case R.id.user_modify_bt_ok:
                new AlertDialog.Builder(context)
                        .setTitle(context.getResources().getString(R.string.modifyUserInformation))
                        .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                                    userModifyTextName.setError(context.getResources().getString(R.string.errorName));
                                    isValid = false;
                                } if (userModifyAddress.isEmpty()) {
                                    userModifyTextAddress.setError(context.getResources().getString(R.string.errorAddress));
                                    isValid = false;
                                } if (userModifyTel.isEmpty()) {
                                    userModifyTextTel.setError(context.getResources().getString(R.string.errorPhone));
                                    isValid = false;
                                } if (userModifyTel.isEmpty()) {
                                    userModifyTextProfile.setError(context.getResources().getString(R.string.errorProfile));
                                    isValid = false;
                                } if (userModifyGender == 0) {
                                    userModifyRadioWomen.setError(context.getResources().getString(R.string.errorGender));
                                    isValid = false;
                                } if (isValid) {

                                    // 開始 Updata ...
                                    if (updataUserInfo(userModifyName, userModifyAddress, userModifyTel, userModifyProfile) == 0) {
                                        showToast(context, context.getResources().getString(R.string.errorModify));
                                    } else {
                                        showToast(context, context.getResources().getString(R.string.successedModify));
                                        getFragmentManager().popBackStack();
                                    }
                                }
                            }
                        })
                        .setNegativeButton(context.getResources().getString(R.string.cancel), null).show();

                break;

            // 按下取消返回會員資訊頁面 ...
            case R.id.user_modify_bt_cancel:
                getFragmentManager().popBackStack();
                break;
        }
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
        } catch (Exception e) {}
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
