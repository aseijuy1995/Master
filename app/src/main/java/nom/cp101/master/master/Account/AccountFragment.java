package nom.cp101.master.master.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nom.cp101.master.master.Account.MyAccount.MyAccountFragment;
import nom.cp101.master.master.Account.MyCourse.CLASS.MyCourseMainFragment;
import nom.cp101.master.master.Account.MyPhoto.MyPhotoFragment;
import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/5/11.
 */

public class AccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_main_frag,container,false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.botton_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyAccountFragment()).commit();
        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch(item.getItemId()){
                        case R.id.menu_account:
                            selectedFragment = new MyAccountFragment();
                            break;
                        case R.id.menu_photo:
                            selectedFragment = new MyPhotoFragment();
                            break;
                        case R.id.menu_course:
                            selectedFragment = new MyCourseMainFragment();
                            break;
                    }
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };
}
