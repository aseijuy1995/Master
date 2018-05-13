package nom.cp101.master.master.Account.MyAccount;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class UserLookOtherUsersFragment extends Fragment {

    private static String URL_INTENT = "/UserInfo";
    private String selfUserAccount = "aaa123"; // 自己的會員
    private String otherUserAccount = "qqq111"; // 觀看的會員

    private int followStatus = 0; // 儲存 follow 狀態 0 = 沒關注, 1 = 關注

    private static final String TAG = "UserLookOtherUsersFragment";
    private MyTask task;
    private RecyclerView otherUserRecyclerProfession;
    private RelativeLayout otherUserLayoutProfession;
    private UserProfessionAdapter otherUserProfessionAdapter;
    private ImageView otherUserImageBackground, otherUserImagePortrait, otherUserImageFollowStar;
    private Button otherUserButtonChat, otherUserButtonFollow, otherUserButtonCancel;
    private TextView otherUserTextName, otherUserTextAccess, otherUserTextGender, otherUserTextAddress, otherUserTextTel, otherUserTextProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_look_other_users_fragment, container, false);
        findView(view);

        // 從前一頁接到"對方"的會員帳號
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String otherUserAccount = bundle.getString("account");
//            getOtherUserInfo(otherUserAccount);
//        }

        // 拿到對方會員資訊
        getOtherUserInfo(otherUserAccount);

        // 檢查你有沒有關注該會員, 傳入自己和對方的帳號去DB 做比對
        getWhetherFollow(selfUserAccount, otherUserAccount);

        // 若專業欄位沒有隱藏起來, 則去撈DB教練專業資料
        if (otherUserLayoutProfession.getVisibility() != view.GONE) {

            // 準備要丟到RecyclerView的容器
            List<User> otherUserListProfession = new ArrayList<>();
            // 用來裝從DB撈下來的資料
            ArrayList<String> getOtherUserProfessionResult = getOtherUserProfession(otherUserAccount);

            if (otherUserListProfession.size() == 0) {
                User user = new User();
                user.setUserProfession("該會員尚未編輯專業資料");
                otherUserListProfession.add(user);
            } else {
                for (String str : getOtherUserProfessionResult) {
                    // 依序拿出來在存進User陣列裡面
                    User user = new User();
                    user.setUserProfession(str);
                    otherUserListProfession.add(user);
                }
            }
            // RecyclerView 的東西
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            otherUserRecyclerProfession.setLayoutManager(linearLayoutManager);
            otherUserProfessionAdapter = new UserProfessionAdapter(otherUserListProfession, null, getActivity());
            otherUserRecyclerProfession.setAdapter(otherUserProfessionAdapter);
        }


        // 點擊大頭照
        otherUserImagePortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 創造 Dialog
                Dialog userImageDialogPortrait;
                // 加入 Style, 加入 Layout ...
                userImageDialogPortrait = new Dialog(getActivity(),  R.style.user_look_users_image_dialog);
                userImageDialogPortrait.setContentView(R.layout.user_look_other_users_dialog);
                // 註冊 Layout 內的圖片
                ImageView imageView = userImageDialogPortrait.findViewById(R.id.other_user_image);
                // 拿出大頭照, 並放進去
                Bitmap bitmap = ((BitmapDrawable)otherUserImagePortrait.getDrawable()).getBitmap();
                imageView.setImageBitmap(bitmap);
                // Show
                userImageDialogPortrait.show();
                // 酷炫有型圖片動畫
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,4.0f,1.0f,4.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                imageView.startAnimation(scaleAnimation);
            }
        });


        // 撈關注 如何拿到自己的帳號? 該會員帳號?

        otherUserImageFollowStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (followStatus == 0) { // 0 = 沒關注, 點擊後 Insert 關注

                    // 一樣傳入兩個人的會員帳號
                    int result = setFollowStatus(selfUserAccount, otherUserAccount, "insertUserFollowStatus");

                    if (result != 0) {
                        followStatus = 1;
                        Toast.makeText(getActivity(),"已關注",Toast.LENGTH_LONG).show();
                        otherUserImageFollowStar.setImageDrawable(getResources().getDrawable(R.drawable.follow_star_bright));
                    } else {
                        Toast.makeText(getActivity(),"關注失敗?!",Toast.LENGTH_LONG).show();
                    }

                } else { // 不等於 0 = 已關注, 點擊後 Delete 關注

                    int result = setFollowStatus(selfUserAccount, otherUserAccount, "deleteUserFollowStatus");

                    if (result != 0) {
                        followStatus = 0;
                        Toast.makeText(getActivity(),"已取消關注",Toast.LENGTH_LONG).show();
                        otherUserImageFollowStar.setImageDrawable(getResources().getDrawable(R.drawable.follow_star_dark));
                    } else {
                        Toast.makeText(getActivity(),"取消關注失敗?!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });




        return view;
    }


    private Button.OnClickListener otherUserButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.other_user_bt_chat){

                // 聊天 ...
            }
            if (v.getId() == R.id.other_user_bt_follow) {

                // 關注 ...
            }
            if (v.getId() == R.id.other_user_bt_cancel) {

                // 返回 ...
            }
        }
    };


    // 拿到對方只資料
    @SuppressLint("LongLogTag")
    private void getOtherUserInfo(String otherUserAccount) {

        String url = Common.URL + URL_INTENT; // .........
        User user = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("account",otherUserAccount);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            user = new Gson().fromJson(jsonIn, User.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        // 開始拿出資料
        if (user == null) {

            Toast.makeText(getActivity(),"伺服器異常, 無法獲取會員資料, 請聯絡管理員",Toast.LENGTH_LONG).show();

        } else {

            // 拿到性別及身份
            String otherUserAccess = "", otherUserGender = "";

            if (user.getUserAccess() == 1) {
                otherUserAccess =  "教練";
            } else if (user.getUserAccess() == 2) {
                otherUserLayoutProfession.setVisibility(View.GONE); // 不是教練隱藏技能欄位
                otherUserAccess = "學員";
            }
            if (user.getUserGender() == 1) {
                otherUserGender = "Men";
            } else if (user.getUserGender() == 2) {
                otherUserGender = "Women";
            }
            // 呈現資料
            otherUserTextName.setText(user.getUserName());
            otherUserTextAccess.setText(otherUserAccess);
            otherUserTextGender.setText(otherUserGender);
            otherUserTextAddress.setText(user.getUserAddress());
            otherUserTextTel.setText(user.getUserTel());
            otherUserTextProfile.setText(user.getUserProfile());

            // 解碼後拿出圖片, 若有圖片才會執行
            if (user.getUserPortraitBase64() != null) {
                String portraitBase64 = user.getUserPortraitBase64();
                byte[] portrait = Base64.decode(portraitBase64,Base64.DEFAULT);
                if (portrait != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
                    otherUserImagePortrait.setImageBitmap(bitmap);
                }
            }
            if (user.getUserBackgroundBase64() != null) {
                String backgroundBase64 = user.getUserBackgroundBase64();
                byte[] background = Base64.decode(backgroundBase64,Base64.DEFAULT);
                if (background != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(background, 0, background.length);
                    otherUserImageBackground.setImageBitmap(bitmap);
                }
            }
        }
    }


    // 若看的人是教練, 去取得教練專業
    @SuppressLint("LongLogTag")
    private ArrayList<String> getOtherUserProfession(String otherUserAccount) {

        ArrayList<String> getOtherUserProfessionResult = new ArrayList<>();
        String url = Common.URL + URL_INTENT;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserProfession");
        jsonObject.addProperty("account",otherUserAccount);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            getOtherUserProfessionResult = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return getOtherUserProfessionResult;
    }


    private void findView(View view) {
        otherUserLayoutProfession = view.findViewById(R.id.other_user_layout_profession);
        otherUserRecyclerProfession = view.findViewById(R.id.other_user_recycler_profession);
        otherUserImageFollowStar = view.findViewById(R.id.other_user_img_follow);
        otherUserImageBackground = view.findViewById(R.id.other_user_img_background);
        otherUserImagePortrait = view.findViewById(R.id.other_user_img_portrait);
        otherUserTextName = view.findViewById(R.id.other_user_tv_name);
        otherUserTextAccess = view.findViewById(R.id.other_user_tv_access);
        otherUserTextGender = view.findViewById(R.id.other_user_tv_gender);
        otherUserTextAddress = view.findViewById(R.id.other_user_tv_address);
        otherUserTextTel = view.findViewById(R.id.other_user_tv_tel);
        otherUserTextProfile = view.findViewById(R.id.other_user_tv_profile);
        otherUserButtonChat = view.findViewById(R.id.other_user_bt_chat);
        otherUserButtonFollow = view.findViewById(R.id.other_user_bt_follow);
        otherUserButtonCancel = view.findViewById(R.id.other_user_bt_cancel);
        otherUserButtonChat.setOnClickListener(otherUserButton);
        otherUserButtonFollow.setOnClickListener(otherUserButton);
        otherUserButtonCancel.setOnClickListener(otherUserButton);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (task != null) {
            task.cancel(true);
        }
    }


    // 檢查是否有關注對方
    @SuppressLint("LongLogTag")
    public void getWhetherFollow(String selfUserAccount, String otherUserAccount) {

            String url = Common.URL + URL_INTENT;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getWhetherFollow");
            jsonObject.addProperty("slefAccount", selfUserAccount);
            jsonObject.addProperty("otherAccount", otherUserAccount);
            task = new MyTask(url, jsonObject.toString());
            boolean result = false;
            try {
                String jsonIn = task.execute().get();
                Log.d(TAG, "Input: " + jsonIn);
                result = new Gson().fromJson(jsonIn, Boolean.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            // 如果有關注將星星圖片點亮
            if (result) {
                followStatus = 1;
                otherUserImageFollowStar.setImageDrawable(getResources().getDrawable(R.drawable.follow_star_bright));
            }
    }


    // 刪除或新增關注狀態
    @SuppressLint("LongLogTag")
    public int setFollowStatus(String selfUserAccount, String otherUserAccount, String select) {

        String url = Common.URL + URL_INTENT;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", select);  // insertUserFollowStatus , deleteUserFollowStatus
        jsonObject.addProperty("slefAccount", selfUserAccount);
        jsonObject.addProperty("otherAccount", otherUserAccount);
        task = new MyTask(url, jsonObject.toString());
        int result = 0;
        try {
            result = Integer.valueOf(task.execute().get());
            Log.d(TAG, "Input: " + result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }


}
