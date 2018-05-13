package nom.cp101.master.master.Account.MyAccount;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import nom.cp101.master.master.Account.Login;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class UserFragment extends Fragment {

    private static String URL_INTENT = "/UserInfo";
    private static final String TAG = "UserFragment";

    // 換圖片選單用
    private static final String[] listPortraitStr = {"拍攝頭像","選擇頭像","拍攝背景照片","選擇背景照片"};

    private Button userButtonModify, userButtonSignOut, userButtonProfession;
    private ImageView userImagePortrait, userImageBackground;
    private TextView userTextName, userTextIdentity, userTextGender, userTextAddress, userTextTel, userTextProfile, userTextAccount;
    private MyTask task;
    private RecyclerView userRecyclerProfession;
    private UserProfessionAdapter userProfessionAdapter;

    // 裝DB撈下來的會員專業
    private ArrayList<String> userProfessionResultList = new ArrayList<>();
    // 1(教練) = 顯示專業技能欄位, 2(一般會員) = 隱藏專業技能欄位
    private RelativeLayout userLayoutProfession;
    // 放切好的圖片
    private File cropFileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    // 放原始照片
    private Uri imageUri, cropImageUri;
    // activity result 識別碼
    private static final int GALLERY_REQUEST = 0xa0;
    private static final int CAMERA_REQUEST = 0xa1;
    private static final int RESULT_REQUEST = 0xa2;
    // 裁切框設定
    private int output_X = 300, output_Y = 300; // 裁切框大小
    private int aspect_X = 1,   aspect_Y = 1; // 裁切框比例
    private Boolean imageSelect = null; // 判斷要改變大頭照還是背景圖


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_fragment, container, false);
        findView(view);


        if (Common.user_id.trim() == "" || Common.user_id == null) {


            Fragment fragment = new Login();
            getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

        } else {

            String userAccount = Common.user_id;
            getUserInfo(Common.user_id);
            userTextAccount.setText(Common.user_id);

            // 如果專業欄位沒有隱藏 執行去撈該會員的專業資料
            if (userLayoutProfession.getVisibility() != view.GONE) {

                List<User> userListProfession = new ArrayList<>();
                // 字串陣列接 DB拿下來的會員專業
                userProfessionResultList = getUserProfession(userAccount);

                if (userProfessionResultList.size() == 0) {
                    User user = new User();
                    user.setUserProfession("尚未擁有專業技能");
                    userListProfession.add(user);
                } else {
                    for (String str : userProfessionResultList) {
                        // 依序拿出來在存進User陣列裡面
                        User user = new User();
                        user.setUserProfession(str);
                        userListProfession.add(user);
                    }
                }
                // RecyclerView 的東西
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                userRecyclerProfession.setLayoutManager(linearLayoutManager);
                userProfessionAdapter = new UserProfessionAdapter(userListProfession, userAccount, getActivity()); // 傳入List
                userRecyclerProfession.setAdapter(userProfessionAdapter);
            }

        }

        return view;
    }


    /* 準備相簿 */
    public void openPhotoPicker() {
        // 檔案選擇器 ...
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 只看圖片 ...
        intent.setType("image/*");
        // 檢查 Intent 是否可用
        if (isIntentAvailable(getActivity(), intent)) {
            // 將Data與識別碼傳進去, 會自動呼叫 onActivityResult ...
            startActivityForResult(intent, GALLERY_REQUEST);
        }
    }


    /* 準備啟動相機 */
    public void takePicture() {

        // 檢查外部儲存體
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            // 啟動相機
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 準備存放路徑
            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"photo.jpg");
            // 拿到存放路徑的Uri
            imageUri = Uri.fromFile(file);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(getActivity(), "com.donkor.demo.takephoto.fileprovider", file);
            }
            // 儲存照片
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 檢查 Intent 是否可用
            if (isIntentAvailable(getActivity(), intent)) {
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }else {

            Toast.makeText(getActivity(), "外部儲存體出錯", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST: /* 從相簿拿圖片 */

                    // 檢查外部儲存記憶體
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        // 將儲存路徑轉成 Uri 用來放裁剪好的結果圖片
                        cropImageUri = Uri.fromFile(cropFileUri);
                        // 解析選擇的圖片, 拿到圖片Uri
                        Uri newUri = Uri.parse(UserPhotoProcessing.getPath(getActivity(), data.getData()));
                        // 判斷 SDK 版本
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // 若 ... 再做一次處理
                            newUri = FileProvider.getUriForFile(getActivity(), "com.donkor.demo.takephoto.fileprovider", new File(newUri.getPath()));
                        }
                        // 開始裁切圖片 ...
                        cropImageUri(newUri, cropImageUri, aspect_X, aspect_Y, output_X, output_Y);
                    } else {

                        Toast.makeText(getActivity(), "外部儲存體出錯", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case CAMERA_REQUEST:  /* 拍照擷取圖片 */

                    // 拿到存放切好的圖片的存放路徑
                    cropImageUri = Uri.fromFile(cropFileUri);
                    // 開始裁切圖片 ...
                    cropImageUri(imageUri, cropImageUri, aspect_X, aspect_Y, output_X, output_Y);

                    break;

                case RESULT_REQUEST: /* 完成後的處理 ... */

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), cropImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (bitmap != null) {
                        if (imageSelect) {
                            // 背景圖
                            userImageBackground.setImageBitmap(bitmap);
                        } else {
                            // 大頭照
                            userImagePortrait.setImageBitmap(bitmap);
                        }
                        // 開始上傳圖片
                        updataPhoto(bitmap);
                    }
                    break;
            }
        } else {
            // 按下導航欄的返回後執行 ...
            Toast.makeText(getActivity(), "已取消更改圖片", Toast.LENGTH_SHORT).show();
        }
    }


    /* 裁切圖片 */
    public void cropImageUri( Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);  // 將裁切好的圖片保存起來
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RESULT_REQUEST);
    }


    /* 裁切圖片的設定值 */
    public void changeAspectSize(int which) {
        if (which > 1) {
            output_X = 600; // 若選擇更換背景圖片
            output_Y = 400;
            aspect_X = 3;
            aspect_Y = 2;
            imageSelect = true;
        } else {
            output_X = 200; // 若選擇更換大頭照
            output_Y = 200;
            aspect_X = 1;
            aspect_Y = 1;
            imageSelect = false;
        }
    }


    /* 拿到會員資料 */
    public void getUserInfo(String account) {
        String url = Common.URL + URL_INTENT; // .........
        User user = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("account",account);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            user = new Gson().fromJson(jsonIn, User.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (user == null) {
            // 基本上這行進不來, 登入頁已經處理好了 ...
            Toast.makeText(getActivity(),"伺服器異常, 無法獲取會員資料, 請聯絡管理員",Toast.LENGTH_LONG).show();
        } else {

            String userAccess = "", userGender = "";

            if (user.getUserAccess() == 1) {
                userAccess =  "教練";
            } else if (user.getUserAccess() == 2) {
                userLayoutProfession.setVisibility(View.GONE);
                userAccess = "學員";
            }

            if (user.getUserGender() == 1) {
                userGender = "Men";
            } else if (user.getUserGender() == 2) {
                userGender = "Women";
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
                byte[] portrait = Base64.decode(portraitBase64,Base64.DEFAULT);
                if (portrait != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
                    userImagePortrait.setImageBitmap(bitmap);
                }
            }
            if (user.getUserBackgroundBase64() != null) {
                String backgroundBase64 = user.getUserBackgroundBase64();
                byte[] background = Base64.decode(backgroundBase64,Base64.DEFAULT);
                if (background != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(background, 0, background.length);
                    userImageBackground.setImageBitmap(bitmap);
                }
            }
        }
    }


    /* 上傳圖片 */
    public void updataPhoto(Bitmap bitmap) {

        int editResult = 0;
        String url = Common.URL + URL_INTENT; // .........
        String account = userTextAccount.getText().toString(); // 拿到會員帳號
        byte[] image = bitmapToPNG(bitmap); // 轉成 PNG
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "updataUserPhoto");
        jsonObject.addProperty("account",account); // 帳號
        jsonObject.addProperty("imageSelect",imageSelect); // 判斷存圖片或是背景圖片
        // Base64 = 純文字編碼, 將非文字轉成文字
        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
        task = new MyTask(url, jsonObject.toString());
        try {
            editResult = Integer.valueOf(task.execute().get()); // 返回數字
            Log.d(TAG, "Input: " + editResult);

            if (editResult != 0) {
                Toast.makeText(getActivity(),"圖片上傳成功",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }



    /* 如果是教練 拿教練專業技能 */
    public ArrayList<String> getUserProfession(String account) {

        ArrayList<String> getUserProfessionResult = new ArrayList<>();
        String url = Common.URL + URL_INTENT;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserProfession");
        jsonObject.addProperty("account",account);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            getUserProfessionResult = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return getUserProfessionResult;
    }



    /* 點擊事件處理 */
    private Button.OnClickListener userButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            /* 編輯會員資料 */
            if (v.getId() == R.id.user_bt_modify) {

                // 準備將資料傳到下一頁顯示
                String toModifyId = userTextAccount.getText().toString(); // 帳號
                String toModifyGender = userTextGender.getText().toString(); // 性別
                String toModifyName = userTextName.getText().toString(); // 名字
                String toModifyAddress = userTextAddress.getText().toString(); // 地址
                String toModifyTel = userTextTel.getText().toString(); // 電話
                String toModifyProfile = userTextProfile.getText().toString(); // 個人資訊

                // 包起來傳到修改頁面
                Fragment fragment = new UserModifyDataFragment();
                Bundle bundle = new Bundle();
                bundle.putString("gender", toModifyGender);
                bundle.putString("id", toModifyId);
                bundle.putString("name", toModifyName);
                bundle.putString("address", toModifyAddress);
                bundle.putString("tel", toModifyTel);
                bundle.putString("profile", toModifyProfile);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }

             /* 會員登出 */
            if (v.getId() == R.id.user_bt_Signout) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("您即將登出")
                        .setPositiveButton("確定",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Common.user_id = "";
                                // 清除登入資料
                                SharedPreferences preference = getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);
                                preference.edit().putBoolean("login",false).apply();
                                // 切回登入畫面
                                Fragment login = new Login();
                                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).replace(R.id.fragment_container, login).commit();

                            }
                        }).setNegativeButton("取消", null).show();
            }

            /* 更改大頭照 or 背景圖片 */
            if (v.getId() == R.id.user_img_portrait || v.getId() == R.id.user_img_background) {

                // 跳出選單
                AlertDialog.Builder PortraitAlertDialog = new AlertDialog.Builder(getActivity());
                PortraitAlertDialog.setTitle("修改會員圖像");
                PortraitAlertDialog.setItems(listPortraitStr,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0 || which == 2) {  // 拍攝大頭照 or 背景圖

                            changeAspectSize(which); // 設定裁切框的大小
                            // 選擇拍照
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                // 檢查有沒有權限(相機權限, 存取權限)
                                Boolean cameeaPermissions = getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
                                Boolean readPermissions = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
                                if (cameeaPermissions || readPermissions) {
                                    // 第一次申請權限會自動呼叫 onRequestPermissionsResult
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2);
                                } else {
                                    // 已拿取得權限 準備開啟相機
                                    takePicture();
                                }
                            }

                        } else if (which == 1 || which == 3) { // 選擇大頭照 or 背景圖

                            changeAspectSize(which);  // 設定裁切框的大小
                            // 選擇相簿
                            // SDK 必須大於23 ... 23以下貌似有BUG會無法呼叫 onRequestPermissionsResult ...
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // 檢查權限(存取權限)
                                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // 第一次申請權限會自動呼叫 onRequestPermissionsResult
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                } else {
                                    // 已拿取得權限 準備開啟相簿
                                    openPhotoPicker();
                                }
                            }
                        }
                    }
                });
                PortraitAlertDialog.show();
            }

            /* 新增刪除專業技能 */
            if (v.getId() == R.id.user_bt_profession) {

                // 將專業資料傳到下一頁
                Fragment fragment = new UserModifyProfessionFragment();
                Bundle bundle = new Bundle();
                String account = userTextAccount.getText().toString();
                bundle.putString("account",account);
                bundle.putStringArrayList("profession",userProfessionResultList);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        }
    };


    // 檢查 Intent 是否可用
    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    // 第一次申請權限成功時調用 ...
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 開啟相簿
            openPhotoPicker();
        }
        if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            // 開啟相機
            takePicture();
        }
    }


    public byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    private void findView(View view) {
        userRecyclerProfession = view.findViewById(R.id.user_recycler_profession);
        userImagePortrait = view.findViewById(R.id.user_img_portrait);
        userImageBackground = view.findViewById(R.id.user_img_background);
        userTextName = view.findViewById(R.id.user_tv_name);
        userTextIdentity = view.findViewById(R.id.user_tv_identity);
        userTextGender = view.findViewById(R.id.user_tv_gender);
        userTextAddress = view.findViewById(R.id.user_tv_address);
        userTextTel = view.findViewById(R.id.user_tv_tel);
        userTextProfile = view.findViewById(R.id.user_tv_profile);
        userButtonModify = view.findViewById(R.id.user_bt_modify);
        userButtonSignOut = view.findViewById(R.id.user_bt_Signout);
        userButtonProfession = view.findViewById(R.id.user_bt_profession);
        userTextAccount = view.findViewById(R.id.user_tv_account);
        userLayoutProfession = view.findViewById(R.id.user_layout_profession);
        // 註冊點擊監聽事件
        userButtonModify.setOnClickListener(userButton);
        userButtonSignOut.setOnClickListener(userButton);
        userImagePortrait.setOnClickListener(userButton);
        userImageBackground.setOnClickListener(userButton);
        userButtonProfession.setOnClickListener(userButton);
    }



    @Override
    public void onStop() {
        super.onStop();
        if (task != null) {
            task.cancel(true);
        }
    }


}