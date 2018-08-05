package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.Account.MyAccount.UserModifyProfessionAdapter;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

public class ExperienceUserOtherActivity extends AppCompatActivity implements View.OnClickListener {
    private String userId;
    private static String URL_INTENT = "/UserInfo";
    private MyTask task;
    private Context context;

    private Button userButtonModify, userButtonProfession;
    private ImageView userImagePortrait, userImageBackground, userImageClear;
    private TextView userTextName, userTextIdentity, userTextGender, userTextAddress, userTextTel, userTextProfile;
    private RecyclerView userRecyclerProfession;
    private CardView cvProfession, cvSign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_other_fragment);
        context = ExperienceUserOtherActivity.this;
        findViews();

        if (getIntent().getExtras() != null) {
            userId = getIntent().getExtras().getString("userId");
            userButtonModify.setVisibility(View.INVISIBLE);
            userButtonProfession.setVisibility(View.INVISIBLE);
            cvSign.setVisibility(View.GONE);

            userImageClear.setVisibility(View.VISIBLE);
            userImageClear.bringToFront();
            userImageClear.setOnClickListener(this);
        }
    }

    private void findViews() {
        userRecyclerProfession = (RecyclerView) findViewById(R.id.user_recycler_profession);
        userImagePortrait = (ImageView) findViewById(R.id.user_img_portrait);
        userImageBackground = (ImageView) findViewById(R.id.user_img_background);
        userImageClear = (ImageView) findViewById(R.id.user_img_clear);
        userTextName = (TextView) findViewById(R.id.user_tv_name);
        userTextIdentity = (TextView) findViewById(R.id.user_tv_identity);
        userTextGender = (TextView) findViewById(R.id.user_tv_gender);
        userTextAddress = (TextView) findViewById(R.id.user_tv_address);
        userTextTel = (TextView) findViewById(R.id.user_tv_tel);
        userTextProfile = (TextView) findViewById(R.id.user_tv_profile);
        userButtonModify = (Button) findViewById(R.id.user_bt_modify);
        userButtonProfession = (Button) findViewById(R.id.user_bt_profession);
        cvProfession = (CardView) findViewById(R.id.cvProfession);
        cvSign = (CardView) findViewById(R.id.cvSign);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo(userId);

        // 如果專業欄位沒有隱藏 執行去撈該會員的專業資料
        if (cvProfession.getVisibility() != View.GONE || cvProfession.getVisibility() != View.INVISIBLE) {
            List<User> userListProfession = new ArrayList<>();
            // 字串陣列接 DB拿下來的會員專業
            List<String> userProfessionResultList = getUserProfession(userId);
            User user = null;
            if (userProfessionResultList.size() == 0) {
                user = new User();
                user.setUserProfession(context.getResources().getString(R.string.noProfessionalSkillsYet));
                userListProfession.add(user);
            } else {
                for (String str : userProfessionResultList) {
                    user = new User();
                    // 依序拿出來在存進User陣列裡面
                    user.setUserProfession(str);
                    userListProfession.add(user);
                }
            }
            // RecyclerView 的東西
            userRecyclerProfession.setLayoutManager(new LinearLayoutManager(context));
            //typeNumber辨識LayoutParams
            userRecyclerProfession.setAdapter(new UserModifyProfessionAdapter(userListProfession, 0));
        }
    }

    /* 拿到會員資料 */
    public void getUserInfo(String userId) {
        String url = Common.URL + URL_INTENT;
        User user = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("account", userId);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            user = new Gson().fromJson(jsonIn, User.class);
        } catch (Exception e) {
        }

        if (user == null) {
            // 基本上這行進不來, 登入頁已經處理好了 ...
            showToast(context, context.getResources().getString(R.string.errorServer));
        } else {
            String userAccess = "", userGender = "";

            if (user.getUserAccess() == 1) {
                cvProfession.setVisibility(View.VISIBLE);
                userAccess = context.getResources().getString(R.string.coach);
            } else if (user.getUserAccess() == 2) {
                cvProfession.setVisibility(View.GONE);
                userAccess = context.getResources().getString(R.string.student);
            }

            if (user.getUserGender() == 1) {
                userGender = context.getResources().getString(R.string.man);
            } else if (user.getUserGender() == 2) {
                userGender = context.getResources().getString(R.string.women);
            }

            // 實現畫面 ...
            userTextName.setText(user.getUserName());
            userTextIdentity.setText(userAccess);
            userTextGender.setText(userGender);
            userTextAddress.setText(user.getUserAddress());
            userTextTel.setText(user.getUserTel());
            userTextProfile.setText(user.getUserProfile());

            // 拿出圖片 ... 若有圖片解碼後拿出來
            if (user.getUserPortraitBase64() != null) {
                String portraitBase64 = user.getUserPortraitBase64();
                byte[] portrait = Base64.decode(portraitBase64, Base64.DEFAULT);
                if (portrait != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
                    userImagePortrait.setImageBitmap(bitmap);
                }
            }
            if (user.getUserBackgroundBase64() != null) {
                String backgroundBase64 = user.getUserBackgroundBase64();
                byte[] background = Base64.decode(backgroundBase64, Base64.DEFAULT);
                if (background != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(background, 0, background.length);
                    userImageBackground.setImageBitmap(bitmap);
                }
            }
        }
    }

    /* 如果是教練 拿教練專業技能 */
    public List<String> getUserProfession(String account) {
        List<String> getUserProfessionResult = new ArrayList<>();
        String url = Common.URL + URL_INTENT;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserProfession");
        jsonObject.addProperty("account", account);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            getUserProfessionResult = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
        }
        return getUserProfessionResult;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_img_clear:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
        }
    }

}
