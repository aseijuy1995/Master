package nom.cp101.master.master.Account.MyPhoto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class MyPhotoFragment extends Fragment implements View.OnClickListener {
    public static final String URL_INTENT = "/UserInfo";
    private MyTask task;
    private GridView photoGridView;
    private ImageView ivAgainLogin;
    private TextView tvEmptyPhoto;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_photo_frag, container, false);
        photoGridView = (GridView) view.findViewById(R.id.photoGridView);
        ivAgainLogin = (ImageView) view.findViewById(R.id.ivAgainLogin);
        tvEmptyPhoto = (TextView) view.findViewById(R.id.tvEmptyPhoto);
        context = getContext();
        ivAgainLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 拿到 User 帳號
        String userAccount = Common.getUserName(getActivity());
        ivAgainLogin.setVisibility(View.VISIBLE);

        // 如果沒則跳到登入畫面
        if (Common.checkUserName(getActivity(), userAccount)) {
            ivAgainLogin.setVisibility(View.GONE);
            // 拿到照片ID...
            ArrayList<String> userAllPostId = getUserAllPostId(userAccount);
            if (userAllPostId.size() <= 0) {
                tvEmptyPhoto.setVisibility(View.VISIBLE);
                photoGridView.setVisibility(View.GONE);

            } else {
                tvEmptyPhoto.setVisibility(View.GONE);
                photoGridView.setVisibility(View.VISIBLE);
                photoGridView.setAdapter(new MyPhotoAdapter(context, userAllPostId, photoGridView));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAgainLogin:
                Common.checkUserName(context, Common.getUserName(context));
                break;
        }
    }

    // 拿到使用者發過的所有文章ID
    private ArrayList<String> getUserAllPostId(String userAccount) {
        String url = Common.URL + URL_INTENT;
        ArrayList<String> result = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserAllPostId");
        jsonObject.addProperty("account", userAccount);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
        }
        return result;
    }

}
