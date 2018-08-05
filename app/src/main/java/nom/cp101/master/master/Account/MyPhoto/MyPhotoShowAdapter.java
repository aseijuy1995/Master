package nom.cp101.master.master.Account.MyPhoto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MyPhotoShowAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> userAllPostId;

    public MyPhotoShowAdapter(FragmentManager fm, ArrayList<String> userAllPostId) {
        super(fm);
        this.userAllPostId = userAllPostId;
    }

    @Override
    public Fragment getItem(int position) {
        String postId = (String) userAllPostId.get(position);
        MyPhotoShowFragment myPhotoShowFragment = new MyPhotoShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        myPhotoShowFragment.setArguments(bundle);
        return myPhotoShowFragment;
    }

    @Override
    public int getCount() {
        return userAllPostId.size();
    }
}
