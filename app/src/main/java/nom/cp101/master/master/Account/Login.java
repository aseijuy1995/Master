package nom.cp101.master.master.Account;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nom.cp101.master.master.Account.MyAccount.UserFragment;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class Login extends Fragment {

    public static String URL_INTENT = "/UserInfo";
    private static final String TAG = "Log in";
    private ImageView loginImageView;
    private EditText loginEditAccount, loginEditPassword;
    private Button loginButtonLogin, loginButtonSignup;
    private TextView loginTextMessage;
    private MyTask task;
    private LinearLayout loginLinearLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        findView(view);

        // 開始監聽畫面大小變化, 並加入動畫
        loginLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(changingViewAnimation);

        return view;
    }


    // 點擊事件處理 ...
    private Button.OnClickListener loginButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.login_bt_login) { // 登入 ...

                // 拿到User的帳號密碼 ...
                String userAccount = loginEditAccount.getText().toString().trim();
                String userPassword = loginEditPassword.getText().toString().trim();

                // 預計改寫成setError
                if (userAccount.length() <= 0 || userPassword.length() <= 0) {
                    loginTextMessage.setText("請輸入帳號密碼!!");
                    return;
                }

                if (isUserValid(userAccount, userPassword)) {  // to DB 檢查帳號密碼 ...
                    // 將帳號密碼儲存到記憶體
                    SharedPreferences preference = getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);
                    preference.edit().putBoolean("login", true).putString("account", userAccount).putString("password", userPassword).apply();
                    // 將帳號存起來
                    Common.user_id = userAccount;
                    // 跳回上一頁
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(),"帳號或密碼錯誤!!",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (v.getId() == R.id.login_bt_signup) { // 註冊 ....

                // 導向註冊頁面
                Fragment fragment = new Signup();
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        }
    };


    // 檢查帳號密碼, 返回Bool
    private boolean isUserValid(String userAccount, String userPassword) {
        String url = Common.URL + URL_INTENT;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "login");
        jsonObject.addProperty("account", userAccount);
        jsonObject.addProperty("password", userPassword);
        task = new MyTask(url, jsonObject.toString());
        boolean isUserValid = false;
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: " + jsonIn);
            isUserValid = new Gson().fromJson(jsonIn, Boolean.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return isUserValid;
    }


    private void findView(View view) {
        loginLinearLayout = view.findViewById(R.id.login_linearlayout);
        loginImageView = view.findViewById(R.id.login_image_view);
        loginTextMessage = view.findViewById(R.id.login_tv_message);
        loginEditAccount = view.findViewById(R.id.login_ed_account);
        loginEditPassword = view.findViewById(R.id.login_ed_password);
        loginButtonLogin = view.findViewById(R.id.login_bt_login);
        loginButtonSignup = view.findViewById(R.id.login_bt_signup);
        // 註冊按鈕監聽 ...
        loginButtonSignup.setOnClickListener(loginButton);
        loginButtonLogin.setOnClickListener(loginButton);
    }


    @Override
    public void onStart() { // 再次打開 APP 免在重打密碼
        super.onStart();
        SharedPreferences preference = getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);
        boolean login = preference.getBoolean("login", false);
        if (login) {
            String userAccount = preference.getString("account", "");
            String userPassword = preference.getString("password", "");

            if (isUserValid(userAccount, userPassword)) {
                // 將帳號存起來
                Common.user_id = userAccount;
                // 跳回上一頁
                getFragmentManager().popBackStack();

            } else {
                preference.edit().putBoolean("login",false).apply(); // 偏好設定 如登入失敗洗掉偏好設定檔
                Toast.makeText(getActivity(),"請重新輸入帳號密碼!!",Toast.LENGTH_LONG).show();
            }
        }
    }


    // 監聽鍵盤起降, 畫面改變動畫
    private ViewTreeObserver.OnGlobalLayoutListener changingViewAnimation = new ViewTreeObserver.OnGlobalLayoutListener() {

        // 畫面變化時調用此方法, 即鍵盤起降
        @Override
        public void onGlobalLayout() {

            // 拿到目前可看到的範圍
            Rect rect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            // 拿到螢幕總高度
            int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
            // 拿到變化後的高度
            int heightDifference = screenHeight - rect.bottom;
//            Log.d(TAG, "ScreenHeight: " + heightDifference);

            if (heightDifference < 300) {   // 鍵盤收起

                loginImageView.setVisibility(View.VISIBLE);
//                // 縮放動畫
//                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f,
//                        Animation.RELATIVE_TO_SELF, 0.5f,
//                        Animation.RELATIVE_TO_SELF, 0);
//                scaleAnimation.setDuration(200);
//                scaleAnimation.setFillAfter(true);
//                loginImageView.startAnimation(scaleAnimation);
//
//                // 位移動畫
//                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -300, 0);
//                translateAnimation.setDuration(200);
//                translateAnimation.setFillAfter(true);
//                loginEditAccount.startAnimation(translateAnimation);
//                loginEditPassword.startAnimation(translateAnimation);
//                loginButtonLogin.startAnimation(translateAnimation);
//                loginButtonSignup.startAnimation(translateAnimation);

            } else { // 鍵盤升起

                loginImageView.setVisibility(View.GONE);
//                // 縮放動畫
//                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,0.5f,1.0f,0.5f,
//                        Animation.RELATIVE_TO_SELF, 0.5f,
//                        Animation.RELATIVE_TO_SELF, 0);
//                scaleAnimation.setDuration(200);
//                scaleAnimation.setFillAfter(true);
//                loginImageView.startAnimation(scaleAnimation);
//
//                // 位移動畫
//                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -300);
//                translateAnimation.setDuration(200);
//                translateAnimation.setFillAfter(true);
//                loginEditAccount.startAnimation(translateAnimation);
//                loginEditPassword.startAnimation(translateAnimation);
//                loginButtonLogin.startAnimation(translateAnimation);
//                loginButtonSignup.startAnimation(translateAnimation);
            }
        }
    };


    @Override
    public void onStop() {
        super.onStop();

        // 結束時一定要回收, 不然一定閃退
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            loginLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(changingViewAnimation);
        }
        if (task != null) {
            task.cancel(true);
        }
    }


}
