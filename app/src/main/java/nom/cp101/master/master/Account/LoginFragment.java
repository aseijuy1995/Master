package nom.cp101.master.master.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nom.cp101.master.master.Account.MyAccount.UserFragment;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Account.AccountFragment.bottomNavigationView;
import static nom.cp101.master.master.Main.Common.showToast;
import static nom.cp101.master.master.Master.Master.bnvMaster;

public class LoginFragment extends Fragment implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener{

    public static String URL_INTENT = "/UserInfo";
    private ImageView loginImageView;
    private EditText loginEditAccount, loginEditPassword;
    private Button loginButtonLogin, loginButtonSignup;
    private MyTask task;
    private LinearLayout loginLinearLayout;
    private Boolean noAnimation = false;  // 禁止第一次進入畫面就跑動畫
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_login_frag, container, false);
        context = getActivity();

        findView(view);
        // 註冊按鈕監聽 ...
        loginButtonSignup.setOnClickListener(this);
        loginButtonLogin.setOnClickListener(this);

        // 開始監聽畫面大小變化, 並加入畫面變動
//        loginLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        return view;
    }

    private void findView(View view) {
        loginLinearLayout = view.findViewById(R.id.login_linearlayout);
        loginImageView = view.findViewById(R.id.login_image_view);
        loginEditAccount = view.findViewById(R.id.login_ed_account);
        loginEditPassword = view.findViewById(R.id.login_ed_password);
        loginButtonLogin = view.findViewById(R.id.login_bt_login);
        loginButtonSignup = view.findViewById(R.id.login_bt_signup);

    }


    // 點擊事件處理 ...
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 登入 ...
            case R.id.login_bt_login:
                // 拿到User的帳號密碼 ...
                String userAccount = loginEditAccount.getText().toString().trim().toLowerCase();
                String userPassword = loginEditPassword.getText().toString().trim().toLowerCase();

                // 預計改寫成setError
                if (userAccount.length() <= 0 || userPassword.length() <= 0) {
                    showToast(getActivity(), context.getResources().getString(R.string.enterAccountPassword));
                    return;
                }

                // to DB 檢查帳號密碼 ...
                if (isUserValid(userAccount, userPassword)) {
                    // 將帳號密碼儲存到記憶體
                    SharedPreferences preference = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putBoolean("login", true).putString("account", userAccount).putString("password", userPassword).apply();
                    // 將帳號存起來
                    Common.setUserName(getActivity(), userAccount);
                    // 拿到權限
                    int userAccess = getUserAccess(userAccount);
                    // 儲存權限
                    editor.putInt("userAccess", userAccess).apply();


                    getActivity().finish();
                    UserFragment.userAgainLogin.setVisibility(View.GONE);
                    UserFragment.userAllInfo.setVisibility(View.VISIBLE);

                } else {
                    showToast(context, context.getResources().getString(R.string.errorAccountPassword));
                    return;
                }
                break;

            // 註冊 ....
            case R.id.login_bt_signup:
                // 導向註冊頁面
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                        replace(R.id.frameLogSign, new SignupFragment()).addToBackStack(null).commit();
                break;
        }

    }


    private int getUserAccess(String userAccount) {
        String url = Common.URL + URL_INTENT;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserAccess");
        jsonObject.addProperty("account", userAccount);
        task = new MyTask(url, jsonObject.toString());
        int result = 0;
        try {
            result = Integer.valueOf(task.execute().get());
        } catch (Exception e) {}
        return result;
    }


    // 檢查帳號密碼, 返回Bool
    private boolean isUserValid(String userAccount, String userPassword) {
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "login");
        jsonObject.addProperty("account", userAccount);
        jsonObject.addProperty("password", userPassword);
        task = new MyTask(url, jsonObject.toString());
        boolean isUserValid = false;
        try {
            String jsonIn = task.execute().get();
            isUserValid = new Gson().fromJson(jsonIn, Boolean.class);
        } catch (Exception e) {}
        return isUserValid;
    }




    // 監聽鍵盤起降, 畫面改變動畫
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

            if (noAnimation) {
                // 縮放動畫
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                loginImageView.startAnimation(scaleAnimation);
                // 位移動畫
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -300, 0);
                translateAnimation.setDuration(200);
                translateAnimation.setFillAfter(true);
                loginEditAccount.startAnimation(translateAnimation);
                loginEditPassword.startAnimation(translateAnimation);
                loginButtonLogin.startAnimation(translateAnimation);
                loginButtonSignup.startAnimation(translateAnimation);
            }

        } else { // 鍵盤升起

            // 縮放動畫
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            loginImageView.startAnimation(scaleAnimation);
            // 位移動畫
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -300);
            translateAnimation.setDuration(200);
            translateAnimation.setFillAfter(true);
            loginEditAccount.startAnimation(translateAnimation);
            loginEditPassword.startAnimation(translateAnimation);
            loginButtonLogin.startAnimation(translateAnimation);
            loginButtonSignup.startAnimation(translateAnimation);

            // 鍵盤升起過一次, 並將 noAnimation 設成 true 開始可以跑動畫
            noAnimation = true;
        }

    }


//    @Override
//    public void onStart() { // 再次打開 APP 免在重打密碼
//        super.onStart();
//        SharedPreferences preference = getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);
//        boolean user_login_frag = preference.getBoolean("user_login_frag", false);
//        if (user_login_frag) {
//            String userAccount = preference.getString("account", "");
//            String userPassword = preference.getString("password", "");
//
//            if (isUserValid(userAccount, userPassword)) {
//
//                // 將帳號存起來
//                Common.setUserName(getActivity(),userAccount);
//                // 拿到權限
//                int userAccess = getUserAccess(userAccount);
//                // 儲存權限
//                Common.setUserAccess(getActivity(), userAccess);
//                // 跳回上一頁
//                getFragmentManager().popBackStack();
//
//            } else {
//                preference.edit().putBoolean("user_login_frag",false).apply(); // 偏好設定 如登入失敗洗掉偏好設定檔
//                Toast.makeText(getActivity(),"請重新輸入帳號密碼!!",Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    @Override
    public void onStop() {
        super.onStop();

        // 結束時一定要回收, 不然一定閃退
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            loginLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }

        if (task != null) {
            task.cancel(true);
        }
    }



}
