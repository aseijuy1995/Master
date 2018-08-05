package nom.cp101.master.master.Account.MyAccount;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

public class UserModifyDataActivity extends AppCompatActivity implements View.OnClickListener {
    private static String URL_INTENT = "/UserInfo";
    private ImageView ivBack;
    private RadioGroup userModifyRadioGender;
    private RadioButton userModifyRadioMen, userModifyRadioWomen;
    private Button userModifyButtonCancel, userModifyButtonOK;
    private EditText userModifyTextName, userModifyTextAddress, userModifyTextTel, userModifyTextProfile;
    private String userModifyAccount; // 存會員ID ...
    private int userModifyGender; // 存會員性別 ...
    private MyTask task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_modify_data_activity);
        findView();
        // 監聽點擊事件 ...
        userModifyButtonOK.setOnClickListener(this);
        userModifyButtonCancel.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        // 從會員資訊頁面拿到值 ...
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String getModifyGender = bundle.getString("gender"); // 拿到性別
            String getModifyAccount = bundle.getString("id"); // 拿到帳號
            String getModifyName = bundle.getString("name"); // 拿到名字
            String getModifyAddress = bundle.getString("address"); // 拿到地址
            String getModifyTel = bundle.getString("tel"); // 拿到電話
            String getModifyProfile = bundle.getString("profile"); // 拿到簡介

            // 判斷傳過來的性別貼在 Radio Button 上 ...
            if (getModifyGender.equals(getResources().getString(R.string.man))) {
                userModifyRadioMen.setChecked(true);
            } else if (getModifyGender.equals(getResources().getString(R.string.women))) {
                userModifyRadioWomen.setChecked(true);
            }
            // 將文字貼到 UI 上 ...
            userModifyTextName.setText(getModifyName);
            userModifyTextAddress.setText(getModifyAddress);
            userModifyTextTel.setText(getModifyTel);
            userModifyTextProfile.setText(getModifyProfile);
            userModifyAccount = getModifyAccount;
        }
    }

    private void findView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        userModifyRadioGender = (RadioGroup) findViewById(R.id.user_modify_rb_gender);
        userModifyRadioMen = (RadioButton) findViewById(R.id.user_modify_rb_men);
        userModifyRadioWomen = (RadioButton) findViewById(R.id.user_modify_rb_women);
        userModifyTextName = (EditText) findViewById(R.id.user_modify_et_name);
        userModifyTextAddress = (EditText) findViewById(R.id.user_modify_et_address);
        userModifyTextTel = (EditText) findViewById(R.id.user_modify_et_tel);
        userModifyTextProfile = (EditText) findViewById(R.id.user_modify_et_profile);
        userModifyButtonOK = (Button) findViewById(R.id.user_modify_bt_ok);
        userModifyButtonCancel = (Button) findViewById(R.id.user_modify_bt_cancel);
    }

    // Servlet 修改會員資料 ...
    @SuppressLint("LongLogTag")
    public int updataUserInfo(String Name, String address, String tel, String profile) {
        int editResult = 0;
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "editMemberInfo");
        jsonObject.addProperty("account", userModifyAccount);
        jsonObject.addProperty("name", Name);
        jsonObject.addProperty("gender", userModifyGender);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("tel", tel);
        jsonObject.addProperty("profile", profile);
        task = new MyTask(url, jsonObject.toString());
        try {
            editResult = Integer.valueOf(task.execute().get());
        } catch (Exception e) {
        }
        return editResult;
    }

    // 點擊事件處理 ...
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;

            // 跳出警告視窗, 並確認是否修改會員資料 ...
            case R.id.user_modify_bt_ok:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.modifyUserInformation));

                builder.setPositiveButton(getResources().getString(R.string.determine), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updata();
                        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                    }
                });
                builder.create();
                builder.show();
                break;

            // 按下取消返回會員資訊頁面 ...
            case R.id.user_modify_bt_cancel:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    private void dialogForSaveOrAbort() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.isSaveOrAdort));
        builder.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updata();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.give_up), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);

            }
        });
        builder.create();
        builder.show();
    }

    public void updata() {
        // 取得資料 ...
        String userModifyName = userModifyTextName.getText().toString().trim();
        String userModifyAddress = userModifyTextAddress.getText().toString().trim();
        String userModifyTel = userModifyTextTel.getText().toString().trim();
        String userModifyProfile = userModifyTextProfile.getText().toString();

        // 拿到性別(數字) ...
        for (int i = 0; i < userModifyRadioGender.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) userModifyRadioGender.getChildAt(i);
            if (radioButton.isChecked()) {
                userModifyGender = i + 1;
                userModifyRadioWomen.setError(null);
            }
        }
        // 開始檢查資料是否合法 ...
        boolean isValid = true;
        if (userModifyName.isEmpty()) {
            userModifyTextName.setError(getResources().getString(R.string.errorName));
            isValid = false;
        }
        if (userModifyAddress.isEmpty()) {
            userModifyTextAddress.setError(getResources().getString(R.string.errorAddress));
            isValid = false;
        }
        if (userModifyTel.isEmpty()) {
            userModifyTextTel.setError(getResources().getString(R.string.errorPhone));
            isValid = false;
        }
        if (userModifyTel.isEmpty()) {
            userModifyTextProfile.setError(getResources().getString(R.string.errorProfile));
            isValid = false;
        }
        if (userModifyGender == 0) {
            userModifyRadioWomen.setError(getResources().getString(R.string.errorGender));
            isValid = false;
        }
        if (isValid) {
            // 開始 Updata ...
            if (updataUserInfo(userModifyName, userModifyAddress, userModifyTel, userModifyProfile) == 0) {
                showToast(getApplicationContext(), getResources().getString(R.string.errorModify));
            } else {
                showToast(getApplicationContext(), getResources().getString(R.string.successedModify));

                Intent intent = new Intent();
                intent.putExtra("select", 4);
                finish();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        dialogForSaveOrAbort();
    }
}
