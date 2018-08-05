package nom.cp101.master.master.Account.MyPhoto;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import nom.cp101.master.master.R;

public class MyPhotoShowActivity extends AppCompatActivity {
    private ViewPager vpPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_photo_show_activity);
        vpPhoto = (ViewPager) findViewById(R.id.vpPhoto);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<String> userAllPostId = bundle.getStringArrayList("userAllPostId");
            vpPhoto.setAdapter(new MyPhotoShowAdapter(getSupportFragmentManager(), userAllPostId));

            //設定多圖瀏覽時,viewPager只向指定圖片顯示
            String postId = bundle.getString("postId");
            if (postId != null) {
                int index = userAllPostId.indexOf(postId);
                vpPhoto.setCurrentItem(index);
            }
        }
    }
}
