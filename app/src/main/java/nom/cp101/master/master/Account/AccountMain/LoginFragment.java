package nom.cp101.master.master.Account.AccountMain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.Master.MasterActivity;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;
import static nom.cp101.master.master.Master.MasterActivity.LOGIN;

public class LoginFragment extends Fragment implements View.OnClickListener {
    public static String URL_INTENT = "/UserInfo";
    private EditText loginEditAccount, loginEditPassword;
    private Button loginButtonLogin, loginButtonSignup;
    private MyTask task;
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
        return view;
    }

    private void findView(View view) {
        loginEditAccount = (EditText) view.findViewById(R.id.login_ed_account);
        loginEditPassword = (EditText) view.findViewById(R.id.login_ed_password);
        loginButtonLogin = (Button) view.findViewById(R.id.login_bt_login);
        loginButtonSignup = (Button) view.findViewById(R.id.login_bt_signup);
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

                if (userAccount.length() <= 0 || userPassword.length() <= 0) {
                    if (userAccount.length() <= 0)
                        loginEditAccount.setError(context.getResources().getString(R.string.account_empty));
                    if (userPassword.length() <= 0)
                        loginEditPassword.setError(context.getResources().getString(R.string.password_empty));
                    return;
                }

                // to DB 檢查帳號密碼 ...
                if (isUserValid(userAccount, userPassword)) {
                    // 將帳號密碼儲存到記憶體
                    SharedPreferences preference = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putBoolean("login", true)
                            .putString("account", userAccount)
                            .putString("password", userPassword)
                            .apply();

                    // 將帳號存起來
                    Common.setUserName(getActivity(), userAccount);
                    // 取得,儲存權限
                    editor.putInt("userAccess"+userAccount, getUserAccess(userAccount)).apply();

                    Intent intent = new Intent(context, MasterActivity.class);
                    intent.putExtra(LOGIN, LOGIN);
                    startActivity(intent);
                    getActivity().finish();
                    showToast(context, context.getResources().getString(R.string.welcome));

                } else {
                    showToast(context, context.getResources().getString(R.string.errorAccountPassword));
                    return;
                }
                break;

            // 註冊 ....
            case R.id.login_bt_signup:
                // 導向註冊頁面
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.right_in,
                        R.anim.left_out,
                        R.anim.left_in,
                        R.anim.right_out)
                        .replace(R.id.frameLogSign, new SignupFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }

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
        } catch (Exception e) {
        }
        return isUserValid;
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
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
        }

//        // 結束時一定要回收, 不然一定閃退
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            loginLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//        }


    }

}
