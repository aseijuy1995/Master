package nom.cp101.master.master.Account.MyAccount;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static android.app.Activity.RESULT_OK;
import static nom.cp101.master.master.Main.Common.getTimeAsName;
import static nom.cp101.master.master.Main.Common.showToast;

public class UserFragment extends Fragment implements View.OnClickListener {
    private Button userButtonModify, userButtonProfession;
    private ImageView userImagePortrait, userImageBackground;
    private TextView userTextName, userTextIdentity, userTextGender, userTextAddress, userTextTel, userTextProfile, userTextAccount, userButtonSignOut;
    private MyTask task;
    private RecyclerView userRecyclerProfession;
    private LinearLayout userAllInfo;
    private ImageView userAgainLogin;

    private final String URL_INTENT = "/UserInfo";
    private Context context;
    // 裝DB撈下來的會員專業
    private List<String> userProfessionResultList;
    // 1(教練) = 顯示專業技能欄位, 2(一般會員) = 隱藏專業技能欄位
    private CardView cvProfession;
    // 放原始照片
    private Uri imageUri, cropUri;
    // activity result 識別碼
    private static final int GALLERY_REQUEST = 0xa0;
    private static final int CAMERA_REQUEST = 0xa1;
    private static final int RESULT_REQUEST = 0xa2;
    // 判斷要改變大頭照還是背景圖
    private Boolean imageSelect = null;
    private final int PROFESSION_CAMERA_READ_WRITE_EXTERNAL = 0;
    private final int PROFESSION_READ_EXTERNAL = 1;
    private String userAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        findView(view);
        context = getActivity();

        // 註冊點擊監聽事件
        userImageBackground.setOnClickListener(this);
        userImagePortrait.setOnClickListener(this);
        userButtonModify.setOnClickListener(this);
        userButtonProfession.setOnClickListener(this);
        userButtonSignOut.setOnClickListener(this);
        userAgainLogin.setOnClickListener(this);

        //拿到 UserId
        userAccount = Common.getUserName(getActivity());
        // 如果沒則跳到登入畫面,有登入回傳true,沒登入回傳false
        //反向判斷"！"
        if (!Common.checkUserName(getActivity(), userAccount)) {
            userAgainLogin.setVisibility(View.VISIBLE);
            userAllInfo.setVisibility(View.GONE);
            return null;
        }
        return view;
    }

    private void findView(View view) {
        userRecyclerProfession = (RecyclerView) view.findViewById(R.id.user_recycler_profession);
        userImagePortrait = (ImageView) view.findViewById(R.id.user_img_portrait);
        userImageBackground = (ImageView) view.findViewById(R.id.user_img_background);
        userAgainLogin = (ImageView) view.findViewById(R.id.userAgainLogin);
        userAllInfo = (LinearLayout) view.findViewById(R.id.userAllInfo);
        userTextName = (TextView) view.findViewById(R.id.user_tv_name);
        userTextIdentity = (TextView) view.findViewById(R.id.user_tv_identity);
        userTextGender = (TextView) view.findViewById(R.id.user_tv_gender);
        userTextAddress = (TextView) view.findViewById(R.id.user_tv_address);
        userTextTel = (TextView) view.findViewById(R.id.user_tv_tel);
        userTextProfile = (TextView) view.findViewById(R.id.user_tv_profile);
        userButtonModify = (Button) view.findViewById(R.id.user_bt_modify);
        userButtonSignOut = (TextView) view.findViewById(R.id.user_bt_Signout);
        userButtonProfession = (Button) view.findViewById(R.id.user_bt_profession);
        userTextAccount = (TextView) view.findViewById(R.id.user_tv_account);
        cvProfession = (CardView) view.findViewById(R.id.cvProfession);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Common.getUserName(context) != null) {
            userAgainLogin.setVisibility(View.GONE);
            userAllInfo.setVisibility(View.VISIBLE);
            // 成功則開始拿到User 資料
            getUserInfo(userAccount);
            userTextAccount.setText(userAccount);

            // 如果專業欄位沒有隱藏 執行去撈該會員的專業資料
            if (cvProfession.getVisibility() != View.GONE || cvProfession.getVisibility() != View.INVISIBLE) {
                List<User> userListProfession = new ArrayList<>();
                // 字串陣列接 DB拿下來的會員專業
                userProfessionResultList = getUserProfession(userAccount);
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
                userRecyclerProfession.setLayoutManager(new LinearLayoutManager(getActivity()));
                //typeNumber辨識LayoutParams
                userRecyclerProfession.setAdapter(new UserModifyProfessionAdapter(userListProfession, 0));
            }
        }
    }

    /* 拿到會員資料 */
    public void getUserInfo(String account) {
        String url = Common.URL + URL_INTENT;
        User user = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findById");
        jsonObject.addProperty("account", account);
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

    /* 點擊事件處理 */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()) {
            /* 編輯會員資料 */
            case R.id.user_bt_modify:
                // 準備將資料傳到下一頁顯示
                String toModifyId = userTextAccount.getText().toString(); // 帳號
                String toModifyName = userTextName.getText().toString(); // 名字
                String toModifyGender = userTextGender.getText().toString(); // 性別
                String toModifyAddress = userTextAddress.getText().toString(); // 地址
                String toModifyTel = userTextTel.getText().toString(); // 電話
                String toModifyProfile = userTextProfile.getText().toString(); // 個人資訊

                // 包起來傳到修改頁面
                intent = new Intent(getActivity(), UserModifyDataActivity.class);
                bundle = new Bundle();
                bundle.putString("id", toModifyId);
                bundle.putString("name", toModifyName);
                bundle.putString("gender", toModifyGender);
                bundle.putString("address", toModifyAddress);
                bundle.putString("tel", toModifyTel);
                bundle.putString("profile", toModifyProfile);
                intent.putExtras(bundle);
                startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

            /* 會員登出 */
            case R.id.user_bt_Signout:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.userOut));

                builder.setPositiveButton(context.getResources().getString(R.string.signOut), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Common.setUserName(getActivity(), "");
                        // 清除登入資料
                        SharedPreferences preference = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
                        preference.edit().putBoolean("login", false).apply();
                        // 切回登入畫面
                        userAllInfo.setVisibility(View.GONE);
                        userAgainLogin.setVisibility(View.VISIBLE);
                    }
                });
                builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
                break;

                /* 更改大頭照 or 背景圖片 */
            case R.id.user_img_portrait:
            case R.id.user_img_background:
                // 跳出選單
                AlertDialog.Builder portraitAlertDialog = new AlertDialog.Builder(context);
                portraitAlertDialog.setTitle(context.getResources().getString(R.string.modifyUserImage));
                // 換圖片選單用
                final String[] listPortraitStr = context.getResources().getStringArray(R.array.listPortraitStr);
                portraitAlertDialog.setItems(listPortraitStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // 拍攝大頭照 or 背景圖
                            case 0:
                            case 2:
                                // 選擇拍照
                                // 檢查有沒有權限(相機權限, 存取權限)
                                selectImage(which);
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    // 已拿取得權限 準備開啟相機
                                    cameraTurnOn();
                                }
                                // 第一次申請權限會自動呼叫 onRequestPermissionsResult
                                Common.askPermissionByFragment(context,
                                        UserFragment.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PROFESSION_CAMERA_READ_WRITE_EXTERNAL);
                                break;

                            // 選擇大頭照 or 背景圖
                            case 1:
                            case 3:
                                selectImage(which);
                                // 選擇相簿
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    // 檢查權限(存取權限)
                                    if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        // 開啟相簿
                                        openPhotoPicker();
                                    }
                                    Common.askPermissionByFragment(context,
                                            UserFragment.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            PROFESSION_READ_EXTERNAL);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                });
                portraitAlertDialog.create();
                portraitAlertDialog.show();
                break;

                /* 新增刪除專業技能 */
            case R.id.user_bt_profession:
                // 將專業資料傳到下一頁
                String account = userTextAccount.getText().toString();
                intent = new Intent(getActivity(), UserModifyProfessionActivity.class);
                bundle = new Bundle();
                bundle.putString("account", account);
                bundle.putStringArrayList("profession", (ArrayList<String>) userProfessionResultList);
                intent.putExtras(bundle);
                startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

            case R.id.userAgainLogin:
                Common.checkUserName(context, Common.getUserName(context));
                break;
        }
    }

    // 第一次申請權限成功時調用 ...
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PROFESSION_CAMERA_READ_WRITE_EXTERNAL:
                List<Integer> integerList = new ArrayList<>();
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        integerList.add(grantResult);
                    }
                }
                if (grantResults.length > 0 && integerList.size() == 0) {
                    // 開啟相機
                    cameraTurnOn();

                } else {
                    //shouldShowRequestPermissionRationale第一次會返回true,因使用者未拒絕使用權限
                    // 接下來如果再次使用功能出現權限dialog,勾選了不在顯示(Don,t ask again!)時,則此method會返回false
                    // 此時再次要使用功能時,Permission dialog則不在提示,需額外自訂dialog引導使用者開啟權限的介面
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)
                            && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(context)
                                .setView(R.layout.master_prefession_dialog_item)
                                .setPositiveButton(context.getResources().getString(R.string.setProfession), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //前往管理權限介面
                                        Intent localIntent = new Intent();
                                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if (Build.VERSION.SDK_INT >= 9) {
                                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            localIntent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
                                        } else if (Build.VERSION.SDK_INT <= 8) {
                                            localIntent.setAction(Intent.ACTION_VIEW);
                                            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                            localIntent.putExtra("com.android.settings.ApplicationPkgName", getContext().getPackageName());
                                        }
                                        startActivity(localIntent);
                                    }
                                })
                                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    }
                }
                break;

            case PROFESSION_READ_EXTERNAL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 開啟相簿
                    openPhotoPicker();

                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(context)
                                .setView(R.layout.master_prefession_dialog_item)
                                .setPositiveButton(context.getResources().getString(R.string.setProfession), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //前往管理權限介面
                                        Intent localIntent = new Intent();
                                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if (Build.VERSION.SDK_INT >= 9) {
                                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                            localIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                                        } else if (Build.VERSION.SDK_INT <= 8) {
                                            localIntent.setAction(Intent.ACTION_VIEW);
                                            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                            localIntent.putExtra("com.android.settings.ApplicationPkgName", getActivity().getPackageName());
                                        }
                                        startActivity(localIntent);
                                    }
                                })
                                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    }
                }
                break;

            default:
                break;

        }
    }

    /* 準備啟動相機 */
    private void cameraTurnOn() {
        // 檢查外部儲存體
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //getFilesDir()-內部儲存(internal)該區隨著app卸載而刪除
            //getExternalFilesDir-外部儲存(external)該區隨著app卸載而刪
            //getExternalStorageDirectory-外部儲存(external)該區不會隨著app卸載而刪除,公有外存
            //getExternalStoragePublicDirectory()-同上,sdk<8則不支援
            //getExternalFilesDir()-外部儲存(external)該區隨著app卸載而刪除,私有外存,可訪問但無價值
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, getTimeAsName() + "_picture.jpg");

            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                imageUri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (isIntentAvailable(context, intent)) {
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                showToast(context, getResources().getString(R.string.no_camera));
            }
        } else {
            showToast(context, context.getResources().getString(R.string.errorExternalStorage));
        }
    }

    // 檢查 Intent 是否可用
    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /* 準備相簿 */
    public void openPhotoPicker() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 開啟相簿
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        } else {
            showToast(context, context.getResources().getString(R.string.errorExternalStorage));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            /* 從相簿拿圖片 */
                case GALLERY_REQUEST:
                    cropImageUri(data.getData());
                    break;

                /* 拍照擷取圖片 */
                case CAMERA_REQUEST:
                    cropImageUri(imageUri);
                    break;

                /* 完成後的處理 ... */
                case RESULT_REQUEST:
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), cropUri);
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
        }

    }

    /* 裁切圖片 */
    public void cropImageUri(Uri uri) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(file, getTimeAsName() + "_crop_picture.jpg");
        cropUri = Uri.fromFile(file);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        if (imageSelect) {
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 3);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 600);
        } else {
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 600);
            cropIntent.putExtra("outputY", 600);
        }
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropIntent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(cropIntent, RESULT_REQUEST);
    }

    /* 上傳圖片 */
    public void updataPhoto(Bitmap bitmap) {
        int editResult = 0;
        String url = Common.URL + URL_INTENT; // .........
        String account = userTextAccount.getText().toString(); // 拿到會員帳號
        byte[] image = bitmapToPNG(bitmap); // 轉成 PNG
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "updataUserPhoto");
        jsonObject.addProperty("account", account); // 帳號
        jsonObject.addProperty("imageSelect", imageSelect); // 判斷存圖片或是背景圖片
        // Base64 = 純文字編碼, 將非文字轉成文字
        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
        task = new MyTask(url, jsonObject.toString());
        try {
            editResult = Integer.valueOf(task.execute().get()); // 返回數字

            if (editResult != 0) {
                showToast(context, context.getResources().getString(R.string.successUploadImage));
            }
        } catch (Exception e) {
        }
    }

    public byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
        }
    }

    private void selectImage(int which) {
        if (which <= 1) {
            imageSelect = false;
        } else {
            imageSelect = true;
        }
    }
}