package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.Master.MasterActivity;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;
import static nom.cp101.master.master.Master.MasterActivity.SEND_USER;
import static nom.cp101.master.master.Master.MasterActivity.bnvMaster;


public class ExperienceDialogFragment extends DialogFragment implements View.OnClickListener {
    private static String URL_INTENT = "/UserInfo";
    private MyTask task;
    private Context context;
    private User user;
    private String userId;

    private CircleImageView ivPortrait;
    private ImageView ivBackground, ivClear;
    private LinearLayout llHome, llPhone, llMsg;
    private TextView tvName, tvProfile;

//    private String selfUserAccount = "aaa123"; // 自己的會員
//    private String otherUserAccount = "qqq111"; // 觀看的會員
//    private int followStatus = 0; // 儲存 follow 狀態 0 = 沒關注, 1 = 關注
//    private UserModifyProfessionAdapter otherUserProfessionAdapter;
//    private static final String TAG = "ExperienceDialogFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //cancel_title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.user_other_dialog_fragment, container, false);
        context = getContext();
        findView(view);
        llHome.setOnClickListener(this);
        llPhone.setOnClickListener(this);
        llMsg.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        ivBackground.setOnClickListener(this);
        ivPortrait.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            user = getOtherUserInfo(userId);
            // 開始拿出資料
            if (user != null) {
                // 呈現資料
                tvName.setText(user.getUserName());
                tvProfile.setText(user.getUserProfile());

                if (user.getUserBackgroundBase64() != null) {
                    String backgroundBase64 = user.getUserBackgroundBase64();
                    byte[] background = Base64.decode(backgroundBase64, Base64.DEFAULT);
                    if (background != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(background, 0, background.length);
                        ivBackground.setImageBitmap(bitmap);
                    }
                }

                // 解碼後拿出圖片, 若有圖片才會執行
                if (user.getUserPortraitBase64() != null) {
                    String portraitBase64 = user.getUserPortraitBase64();
                    byte[] portrait = Base64.decode(portraitBase64, Base64.DEFAULT);
                    if (portrait != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
                        ivPortrait.setImageBitmap(bitmap);
                    }
                }
            }
        }

//        // 拿到對方會員資訊
//        getOtherUserInfo(otherUserAccount);
//
//        // 檢查你有沒有關注該會員, 傳入自己和對方的帳號去DB 做比對
//        getWhetherFollow(selfUserAccount, otherUserAccount);
//
//        // 若專業欄位沒有隱藏起來, 則去撈DB教練專業資料
//        if (otherUserLayoutProfession.getVisibility() != view.GONE) {
//
//            // 準備要丟到RecyclerView的容器
//            List<User> otherUserListProfession = new ArrayList<>();
//            // 用來裝從DB撈下來的資料
//            ArrayList<String> getOtherUserProfessionResult = getOtherUserProfession(otherUserAccount);
//
//            if (otherUserListProfession.size() == 0) {
//                User user = new User();
//                user.setUserProfession("該會員尚未編輯專業資料");
//                otherUserListProfession.add(user);
//            } else {
//                for (String str : getOtherUserProfessionResult) {
//                    // 依序拿出來在存進User陣列裡面
//                    User user = new User();
//                    user.setUserProfession(str);
//                    otherUserListProfession.add(user);
//                }
//            }
//            // RecyclerView 的東西
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//            otherUserRecyclerProfession.setLayoutManager(linearLayoutManager);
//            otherUserProfessionAdapter = new UserModifyProfessionAdapter(otherUserListProfession, 1);
//            otherUserRecyclerProfession.setAdapter(otherUserProfessionAdapter);
//        }
//
//

//
//
//        // 關注 或 尚未關注
//
//        otherUserImageFollowStar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (followStatus == 0) { // 0 = 沒關注, 點擊後 Insert 關注
//
//                    // 一樣傳入兩個人的會員帳號
//                    int result = setFollowStatus(selfUserAccount, otherUserAccount, "insertUserFollowStatus");
//
//                    if (result != 0) {
//                        followStatus = 1;
//                        Toast.makeText(getActivity(), "已關注", Toast.LENGTH_LONG).show();
//                        otherUserImageFollowStar.setImageDrawable(getResources().getDrawable(R.drawable.follow_star_bright));
//                    } else {
//                        Toast.makeText(getActivity(), "關注失敗?!", Toast.LENGTH_LONG).show();
//                    }
//
//                } else { // 不等於 0 = 已關注, 點擊後 Delete 關注
//
//                    int result = setFollowStatus(selfUserAccount, otherUserAccount, "deleteUserFollowStatus");
//
//                    if (result != 0) {
//                        followStatus = 0;
//                        Toast.makeText(getActivity(), "已取消關注", Toast.LENGTH_LONG).show();
//                        otherUserImageFollowStar.setImageDrawable(getResources().getDrawable(R.drawable.follow_star_dark));
//                    } else {
//                        Toast.makeText(getActivity(), "取消關注失敗?!", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });

        return view;
    }

    private void findView(View view) {
        ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
        ivPortrait = (CircleImageView) view.findViewById(R.id.ivPortrait);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvProfile = (TextView) view.findViewById(R.id.tvProfile);
        llHome = (LinearLayout) view.findViewById(R.id.llHome);
        llPhone = (LinearLayout) view.findViewById(R.id.llPhone);
        llMsg = (LinearLayout) view.findViewById(R.id.llMsg);
        ivClear = (ImageView) view.findViewById(R.id.ivClear);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.llHome:
            case R.id.ivBackground:
                if (userId.equals(Common.getUserName(context))) {
                    intent = new Intent(context, MasterActivity.class);
                    intent.putExtra(SEND_USER, SEND_USER);
                    context.startActivity(intent);
                    getActivity().finish();
                    getDialog().dismiss();

                } else {
                    intent = new Intent(context, ExperienceUserOtherActivity.class);
                    bundle = new Bundle();
                    bundle.putString("userId", userId);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                break;

            case R.id.ivPortrait:
                if (ConnectionServer.getPhotoByUserId(userId) != null) {
                    intent = new Intent(context, ExperienceUserPortraitActivity.class);
                    bundle = new Bundle();
                    bundle.putString("userId", userId);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    showToast(context, context.getResources().getString(R.string.empty_portrait));
                }
                break;

            case R.id.llPhone:
                if (userId.equals(Common.getUserName(context))) {
                    showToast(context, context.getResources().getString(R.string.can_cell_yourself));
                } else {
                    if (user.getUserTel() != null) {
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getUserTel()));
                        context.startActivity(intent);
                    } else {
                        showToast(context, context.getResources().getString(R.string.empty_phone));
                    }
                }
                break;

            case R.id.llMsg:
//                未完成
                break;

            case R.id.ivClear:
                getActivity().onBackPressed();
                break;
        }
    }

    // other-user-data
    private User getOtherUserInfo(String userId) {
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
        return user;
    }


//    // 若看的人是教練, 去取得教練專業
//    @SuppressLint("LongLogTag")
//    private ArrayList<String> getOtherUserProfession(String otherUserAccount) {
//
//        ArrayList<String> getOtherUserProfessionResult = new ArrayList<>();
//        String url = Common.URL + URL_INTENT;
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "getUserProfession");
//        jsonObject.addProperty("account", otherUserAccount);
//        task = new MyTask(url, jsonObject.toString());
//        try {
//            String jsonIn = task.execute().get();
//            Log.d(TAG, "Input: " + jsonIn);
//            getOtherUserProfessionResult = new Gson().fromJson(jsonIn, ArrayList.class);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        return getOtherUserProfessionResult;
//    }
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (task != null) {
//            task.cancel(true);
//        }
//    }
//
//
//    // 檢查是否有關注對方
//    @SuppressLint("LongLogTag")
//    public void getWhetherFollow(String selfUserAccount, String otherUserAccount) {
//
//        String url = Common.URL + URL_INTENT;
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "getWhetherFollow");
//        jsonObject.addProperty("slefAccount", selfUserAccount);
//        jsonObject.addProperty("otherAccount", otherUserAccount);
//        task = new MyTask(url, jsonObject.toString());
//        boolean result = false;
//        try {
//            String jsonIn = task.execute().get();
//            Log.d(TAG, "Input: " + jsonIn);
//            result = new Gson().fromJson(jsonIn, Boolean.class);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//
//        // 如果有關注將星星圖片點亮
//        if (result) {
//            followStatus = 1;
//            otherUserImageFollowStar.setImageDrawable(getResources().getDrawable(R.drawable.follow_star_bright));
//        }
//    }
//
//
//    // 刪除或新增關注狀態
//    @SuppressLint("LongLogTag")
//    public int setFollowStatus(String selfUserAccount, String otherUserAccount, String select) {
//
//        String url = Common.URL + URL_INTENT;
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", select);  // insertUserFollowStatus , deleteUserFollowStatus
//        jsonObject.addProperty("slefAccount", selfUserAccount);
//        jsonObject.addProperty("otherAccount", otherUserAccount);
//        task = new MyTask(url, jsonObject.toString());
//        int result = 0;
//        try {
//            result = Integer.valueOf(task.execute().get());
//            Log.d(TAG, "Input: " + result);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        return result;
//    }


}
