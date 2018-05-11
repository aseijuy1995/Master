package nom.cp101.master.master.Account.MyAccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nom.cp101.master.master.R;


/**
 * Created by chunyili on 2018/4/17.
 */

public class MyAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_my_account_frag,container,false);
        return view;
    }
}
