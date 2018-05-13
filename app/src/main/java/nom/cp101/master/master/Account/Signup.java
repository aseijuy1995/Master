package nom.cp101.master.master.Account;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class Signup extends Fragment {

    public static String URL_INTENT = "/UserInfo";
    private static final String TAG = "Sign up";

    EditText signupTextAccount, signupTextPassword, signupTextName;
    RadioGroup signupRadioAccess;
    RadioButton signupRadioMaster, signupRadioStudent;
    Button signupButtonCancel, signupRadioOk;

    private int signupAccess = 0; // 存放身份用 ... 教練 = 1 , 學員 ＝ 2
    private String checkAccountRepeat; // 存放檢查帳號是否重複用 ...
    private Boolean checkAccountResult = false; // 存放檢查帳號結果用 ...
    private MyTask signupTask;

    // 正規表示式 ...
    // 檢查帳號, 開頭必須是英文, 不能有英文數字以外的字符, 且字串長度為 6~16
    private String canAccount = "\\A[a-zA-Z]\\w{5,15}\\z";
    // 檢查密碼, 不能有英文數字以外的字符, 且字串長度為 6~16
    private String canPassword = "\\A[a-zA-Z0-9]\\w{5,15}\\z";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup, container, false);

        findView(view);

        // 監聽帳號欄位用 ...
        signupTextAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ... 不用寫東西, 刪掉會跳錯
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ... 不用寫東西, 刪掉會跳錯
            }
            @Override
            public void afterTextChanged(Editable s) { // User 每輸入一個字都會執行 ...

                checkAccountRepeat = s.toString(); // 將User輸入的字串存起來 ...

                if (delayRun != null) {
                    // 如果EditText有變化時, 刪除上次延遲方法 ...
                    handler.removeCallbacks(delayRun);
                }
                // 寫入新的延遲方法 ... 延遲 1000毫秒, 若User不在輸入任何東西, 執行run方法 ...
                handler.postDelayed(delayRun, 1000);
            }
        });
        return view;
    }


    // 點擊事件處理 ...
    private Button.OnClickListener signupButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.signup_bt_ok) { // 點擊確認

                // 取得User輸入的資料 ...
                String signupAccount = signupTextAccount.getText().toString().trim();
                String signupPassword = signupTextPassword.getText().toString().trim();
                String signupName = signupTextName.getText().toString().trim();

                // 拿到身份(數字) ...
                for(int i = 0 ; i < signupRadioAccess.getChildCount() ; i++) {
                    RadioButton radioButton = (RadioButton)signupRadioAccess.getChildAt(i);
                    if(radioButton.isChecked()) {
                        signupAccess = i + 1;
                        signupRadioStudent.setError(null);
                    }
                }

                // 開始檢查資料是否合法 ...
                boolean isValid = true;

                Matcher checkAccount = Pattern.compile(canAccount).matcher(signupAccount);
                Matcher checkPassword = Pattern.compile(canPassword).matcher(signupPassword);

                if (!checkAccount.find()) {
                    signupTextAccount.setError("帳號格式不正確");
                    isValid = false;
                } if (!checkPassword.find()) {
                    signupTextPassword.setError("密碼格式不正確");
                    isValid = false;
                } if (signupName.isEmpty()) {
                    signupTextName.setError("您的名稱是?");
                    isValid = false;
                } if (signupAccess == 0) {
                    signupRadioStudent.setError("你的身份是?");
                    isValid = false;
                } if (checkAccountResult == true) {
                    signupTextAccount.setError("此帳號已被使用");
                    isValid = false;
                }

                if (isValid) {

                    // 創建帳號 ... 回傳int
                    if (setUserData(signupAccount, signupPassword, signupName, signupAccess) == 0) {
                        Toast.makeText(getActivity(),"註冊失敗, 請與管理員聯絡",Toast.LENGTH_LONG).show();
                    } else {

                        // 註冊成功, 儲存帳號密碼並返回登入頁面
                        Toast.makeText(getActivity(),"Welcome!",Toast.LENGTH_LONG).show();
                        // 儲存帳號密碼返回登入頁面, 即自動登入
                        SharedPreferences preference = getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);
                        preference.edit().putBoolean("login", true).putString("account", signupAccount).putString("password", signupPassword).apply();
                        getFragmentManager().popBackStack();
                    }
                }
            }

            if (v.getId() == R.id.signup_bt_cancel) { // 返回登入頁面 ...
                getFragmentManager().popBackStack();
            }
        }
    };


    private void findView(View view) {
        signupTextAccount = view.findViewById(R.id.signup_ed_account);
        signupTextPassword = view.findViewById(R.id.signup_ed_password);
        signupTextName = view.findViewById(R.id.signup_ed_name);
        signupRadioAccess = view.findViewById(R.id.signup_rg_access);
        signupRadioMaster = view.findViewById(R.id.signup_rb_master);
        signupRadioStudent = view.findViewById(R.id.signup_rb_student);
        signupButtonCancel = view.findViewById(R.id.signup_bt_cancel);
        signupRadioOk = view.findViewById(R.id.signup_bt_ok);
        // 註冊按鈕監聽 ...
        signupButtonCancel.setOnClickListener(signupButton);
        signupRadioOk.setOnClickListener(signupButton);
    }


    // 防止User每輸入一個字就要連接一次DB, 延遲呼叫用 ...
    private Handler handler = new Handler();
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            Matcher checkAccount = Pattern.compile(canAccount).matcher(checkAccountRepeat);
            if (!checkAccount.find()) {
                signupTextAccount.setError("帳號格式不正確");
            } else {

                // 開始連接 DB 檢查帳號 ... 回傳布林
                if (connectionDBCheckAccount(checkAccountRepeat)) {
                    checkAccountResult = true;
                    signupTextAccount.setError("此帳號已被使用");
                } else {
                    checkAccountResult = false;
                    Drawable errorIcon = getResources().getDrawable(R.drawable.ok_icon);
                    errorIcon.setBounds(new Rect(0, 0, 62, 62));
                    signupTextAccount.setError("此帳號可以使用",errorIcon);
                }
            }
        }
    };


    // 檢查User帳號是否重複用 ...
    private boolean connectionDBCheckAccount(String userAccount) {
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "signupCheckAccount");
        jsonObject.addProperty("account", userAccount);
        signupTask = new MyTask(url, jsonObject.toString());
        boolean isUserValid = false;
        try {
            String jsonIn = signupTask.execute().get();
            Log.d(TAG, "Input: " + jsonIn);
            isUserValid = new Gson().fromJson(jsonIn, Boolean.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return isUserValid;
    }


    // 開始 INSERT 新帳號 ...
    public int setUserData(String userAccount, String userPassword, String userName, int userAccess) {
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "signup");
        jsonObject.addProperty("account", userAccount);
        jsonObject.addProperty("password", userPassword);
        jsonObject.addProperty("name", userName);
        jsonObject.addProperty("access", userAccess);
        signupTask = new MyTask(url, jsonObject.toString());
        int signupResult = 0;
        try {
            signupResult = Integer.valueOf(signupTask.execute().get());
            Log.d(TAG, "Input: " + signupResult);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return signupResult;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (signupTask != null) {
            signupTask.cancel(true);
        }
    }


}
