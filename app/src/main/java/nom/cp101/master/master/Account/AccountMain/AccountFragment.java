package nom.cp101.master.master.Account.AccountMain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nom.cp101.master.master.Account.MyAccount.UserFragment;
import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.Account.MyCourse.MyCourseFragment;
import nom.cp101.master.master.Account.MyCourse.SingleCourseFragment;
import nom.cp101.master.master.Account.MyPhoto.MyPhotoFragment;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Master.MasterActivity.SEND_USER;

public class AccountFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tlAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_main_activity, container, false);
        findViews(view);
        initTabLayout();

        tlAccount.addOnTabSelectedListener(this);
        initContent();
        return view;
    }

    private void findViews(View view) {
        tlAccount = (TabLayout) view.findViewById(R.id.tlAccount);
    }

    private void initTabLayout() {
        tlAccount.addTab(tlAccount.newTab().setIcon(R.drawable.account_box_black));
        tlAccount.addTab(tlAccount.newTab().setIcon(R.drawable.account_image_black));
        tlAccount.addTab(tlAccount.newTab().setIcon(R.drawable.account_run_black));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Fragment selectedFragment = null;
        switch (tab.getPosition()) {
            case 0:
                selectedFragment = new UserFragment();
                break;
            case 1:
                selectedFragment = new MyPhotoFragment();
                break;
            case 2:
                selectedFragment = new MyCourseFragment();
                break;
            default:
                selectedFragment = new UserFragment();
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //當MasterActivity傳遞Bundle過來,判斷開啟UserFragment頁面並指定tabLayout-select = 0
        if (getArguments() != null) {

            if (getArguments().getString(SEND_USER) != null) {
                if (getArguments().getString(SEND_USER).equals(SEND_USER)) {
                    tlAccount.getTabAt(0).select();
                }
            }

            if ((Course) getArguments().getSerializable("course") != null) {
                tlAccount.getTabAt(2).select();

                SingleCourseFragment singleCourseFragment = new SingleCourseFragment();
                Bundle bundle = new Bundle();
                Course course = (Course) getArguments().getSerializable("course");
                bundle.putSerializable("course", course);
                singleCourseFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.right_in,
                                R.anim.left_out,
                                R.anim.right_out,
                                R.anim.left_in)
                        .replace(R.id.fragment_container, singleCourseFragment)
                        .commit();
            }
        }

    }

    private void initContent() {
        Fragment fragment = new UserFragment();
        switchFragment(fragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}
