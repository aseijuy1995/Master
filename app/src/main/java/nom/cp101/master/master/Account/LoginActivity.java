package nom.cp101.master.master.Account;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nom.cp101.master.master.Account.LoginFragment;
import nom.cp101.master.master.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_act);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLogSign, new LoginFragment()).commit();
    }
}
