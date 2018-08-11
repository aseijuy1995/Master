package nom.cp101.master.master.ExperienceArticle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.getTimeAsName;
import static nom.cp101.master.master.Main.Common.showToast;

public class ExperienceAppendActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private TextView tvCancel, tvRelease, tvHead;
    private CircleImageView ivHead;
    private ImageView ivPicture, ivClear, ivCamera, ivPictures, ivMap;
    private EditText etContent;
    private Context context;
    private final String TAG = "ExperienceAppendActivity";

    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, cropUri;
    private byte[] image;
    private String user_id;

    private final int PROFESSION_CAMERA_READ_WRITE_EXTERNAL = 0;
    private final int PROFESSION_READ_EXTERNAL = 1;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_add_activity);
        context = ExperienceAppendActivity.this;
        user_id = Common.getUserName(context);
        findViews();
        setUserData();

        tvCancel.setOnClickListener(this);
        tvRelease.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivPictures.setOnClickListener(this);
        ivMap.setOnClickListener(this);
    }

    private void findViews() {
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvRelease = (TextView) findViewById(R.id.tvRelease);
        ivHead = (CircleImageView) findViewById(R.id.ivHead);
        tvHead = (TextView) findViewById(R.id.tvHead);
        etContent = (EditText) findViewById(R.id.etContent);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        ivClear = (ImageView) findViewById(R.id.ivClear);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivPictures = (ImageView) findViewById(R.id.ivPictures);
        ivMap = (ImageView) findViewById(R.id.ivMap);
    }

    //server取數據
    private void setUserData() {
        User user = ConnectionServer.getUserData(user_id);
        if (user.getUserPortraitBase64() != null) {
            byte[] userPortraitByte = Base64.decode(user.getUserPortraitBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(userPortraitByte, 0, userPortraitByte.length);
            ivHead.setImageBitmap(bitmap);
        }
        tvHead.setText(user.getUserName());
    }

    @Override
    public void onClick(View v) {
        String etStr = etContent.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tvCancel:
                if (etStr == null || etStr.equals("") && ivPicture.getDrawable() == null) {
                    SharedPreferences.Editor edit = getSharedPreferences("save_post", MODE_PRIVATE).edit();
                    edit.putString("post_img", null).putString("post_edit", null).apply();
                    finish();
                } else {
                    dialogWhileBack();
                }
                break;

            case R.id.tvRelease:
                if (ivPicture.getDrawable() == null) {
                    showToast(context, context.getString(R.string.picture_empty));
                }
                if (etStr == null || etStr.equals("")) {
                    showToast(context, context.getString(R.string.content_empty));
                }
                if (ivPicture.getDrawable() != null && ivPicture.getVisibility() != View.GONE && etStr != null && !etStr.equals("")) {
                    int photo_id = insertImage(user_id);
                    int result = insertExperience(user_id, etStr, photo_id);
                    if (result != 0) {
                        showToast(context, context.getString(R.string.new_post_success));
                        SharedPreferences.Editor edit = getSharedPreferences("save_post", MODE_PRIVATE).edit();
                        edit.putString("post_img", null).putString("post_edit", null).apply();
                        finish();
                    }
                }
                break;

            case R.id.ivClear:
                ivPicture.setImageDrawable(null);
                ivPicture.setVisibility(View.GONE);
                ivClear.setVisibility(View.GONE);
                break;

            case R.id.ivCamera:
                // 選擇拍照
                // 檢查有沒有權限(相機權限, 存取權限)
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 已拿取得權限 準備開啟相機
                    cameraTurnOn();
                }
                // 第一次申請權限會自動呼叫 onRequestPermissionsResult
                Common.askPermissionByActivity(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PROFESSION_CAMERA_READ_WRITE_EXTERNAL);
                break;

            case R.id.ivPictures:
                // 選擇相簿
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 檢查權限(存取權限)
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // 開啟相簿
                        photoTurnOn();
                    }
                    Common.askPermissionByActivity(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PROFESSION_READ_EXTERNAL);
                }
                break;

            case R.id.ivMap:
                showToast(this, "map");
                break;
        }
    }

    private void dialogWhileBack() {
        AlertDialog builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_for_article_dialog))
                .setMessage(context.getString(R.string.msg_for_article_dialog))
                .setNeutralButton(R.string.store, this)
                .setNegativeButton(R.string.give_up_edit, this)
                .setPositiveButton(R.string.continue_edit, this)
                .create();
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SharedPreferences.Editor edit = null;
        switch (which) {
            case DialogInterface.BUTTON_NEUTRAL:
                edit = getSharedPreferences("save_post", MODE_PRIVATE).edit();
                String etStr = etContent.getText().toString().trim();
                if (ivPicture.getDrawable() != null && ivPicture.getVisibility() != View.GONE) {
                    Drawable postDrawable = ivPicture.getDrawable();
                    Bitmap postBitmap = Bitmap.createBitmap(postDrawable.getIntrinsicWidth(), postDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(postBitmap);
                    postDrawable.setBounds(0, 0, postDrawable.getIntrinsicWidth(), postDrawable.getIntrinsicHeight());
                    postDrawable.draw(canvas);

                    int size = postDrawable.getIntrinsicWidth() * postDrawable.getIntrinsicHeight() * 4;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
                    postBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] postByte = baos.toByteArray();

                    edit.putString("post_img", Base64.encodeToString(postByte, Base64.DEFAULT)).apply();
                } else {
                    edit.putString("post_img", null).apply();
                }

                if (etStr != null || !etStr.equals("")) {
                    edit.putString("post_edit", etContent.getText().toString().trim()).apply();
                } else {
                    edit.putString("post_edit", null).apply();
                }
                showToast(context, context.getResources().getString(R.string.save_success));
                finish();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                edit = getSharedPreferences("save_post", MODE_PRIVATE).edit();
                edit.putString("post_img", null).putString("post_edit", null).apply();
                dialog.cancel();
                finish();
                break;

            case DialogInterface.BUTTON_POSITIVE:
                dialog.dismiss();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("save_post", MODE_PRIVATE);
        String postImg = preferences.getString("post_img", null);
        String postText = preferences.getString("post_edit", null);
        if (postImg != null) {
            byte[] postByte = Base64.decode(postImg, Base64.DEFAULT);
            ivPicture.setImageBitmap(BitmapFactory.decodeByteArray(postByte, 0, postByte.length));
            ivClear.setVisibility(View.VISIBLE);
            ivPicture.setVisibility(View.VISIBLE);
            ivClear.bringToFront();
        } else {
            ivClear.setVisibility(View.GONE);
            ivPicture.setVisibility(View.GONE);
        }
        if (postText != null) {
            etContent.setText(postText);
        }
    }

    @Override
    public void onBackPressed() {
        String etStr = etContent.getText().toString().trim();
        if (etStr == null || etStr.equals("") && ivPicture.getDrawable() == null) {
            super.onBackPressed();
        } else {
            dialogWhileBack();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //拍照權限
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
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                        } else if (Build.VERSION.SDK_INT <= 8) {
                                            localIntent.setAction(Intent.ACTION_VIEW);
                                            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
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
                    photoTurnOn();

                } else {
                    //shouldShowRequestPermissionRationale第一次會返回true,因使用者未拒絕使用權限
                    // 接下來如果再次使用功能出現權限dialog,勾選了不在顯示(Don,t ask again!)時,則此method會返回false
                    // 此時再次要使用功能時,Permission dialog則不在提示,需額外自訂dialog引導使用者開啟權限的介面
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                                            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                        } else if (Build.VERSION.SDK_INT <= 8) {
                                            localIntent.setAction(Intent.ACTION_VIEW);
                                            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
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
        }
    }

    @SuppressLint("LongLogTag")
    private int insertImage(String user_id) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((BitmapDrawable) ivPicture.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
        image = out.toByteArray();

        if (Common.networkConnected(this)) {
            String url = Common.URL + "/photoServlet";
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "insert");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("photo", imageBase64);
            int photo_id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                photo_id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (photo_id == 0) {
                return photo_id;
            } else {
                return photo_id;
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
            return 0;
        }
    }

    @SuppressLint("LongLogTag")
    private int insertExperience(String user_id, String content, int photo_id) {
        if (Common.networkConnected(this)) {
            String url = Common.URL + "/ExperienceArticleServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("experienceArticle", "insertExperience");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("content", content);
            jsonObject.addProperty("photo_id", photo_id);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0) {
                Log.d(TAG, "新增文章失敗");
                return id;
            } else {
                Log.d(TAG, "新增文章成功");
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(this, R.string.msg_NoNetwork);
            return 0;
        }
    }

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
                contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            } else {
                contentUri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            if (isIntentAvailable(this, intent)) {
                startActivityForResult(intent, REQ_TAKE_PICTURE);
            } else {
                showToast(context, getResources().getString(R.string.no_camera));
            }
        } else {
            showToast(context, context.getResources().getString(R.string.errorExternalStorage));
        }
    }

    private void photoTurnOn() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 開啟相簿
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQ_PICK_PICTURE);
        } else {
            showToast(context, context.getResources().getString(R.string.errorExternalStorage));
        }
    }

    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;

                case REQ_PICK_PICTURE:
                    // 檢查外部儲存記憶體
                    crop(data.getData());
                    break;

                case REQ_CROP_PICTURE:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), cropUri);
                        ivPicture.setVisibility(View.VISIBLE);
                        ivClear.setVisibility(View.VISIBLE);
                        ivPicture.setScaleType(ImageView.ScaleType.FIT_XY);
                        ivPicture.setImageBitmap(bitmap);
                        ivClear.bringToFront();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void crop(Uri uri) {
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
        cropIntent.putExtra("aspectX", 4);
        cropIntent.putExtra("aspectY", 3);
        cropIntent.putExtra("outputX", 800);
        cropIntent.putExtra("outputY", 600);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropIntent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(cropIntent, REQ_CROP_PICTURE);
    }

}
