package nom.cp101.master.master.ExperienceArticle;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ZoomImageView;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Account.MyPhoto.MyPhotoShowFragment.REQUEST_READ_WRITE;
import static nom.cp101.master.master.Main.Common.getTimeAsName;
import static nom.cp101.master.master.Main.Common.showToast;

public class ExperienceUserPortraitActivity extends AppCompatActivity implements View.OnClickListener {
    private ZoomImageView zivPhoto;
    private ImageView ivClear, ivSave;
    private LinearLayout linearLayout;
    private TextView tvContent;
    private View vLine;
    private Bitmap bitmap = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_photo_show_item);
        context = ExperienceUserPortraitActivity.this;
        findViews();
        tvContent.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        vLine.setVisibility(View.GONE);

        ivClear.setOnClickListener(this);
        ivSave.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userId = bundle.getString("userId");
            bitmap = ConnectionServer.getPhotoByUserId(userId);
            if (bitmap != null) {
                zivPhoto.setImageBitmap(bitmap);
            } else {
                zivPhoto.setImageResource(R.drawable.user);
            }
        }
    }

    private void findViews() {
        zivPhoto = (ZoomImageView) findViewById(R.id.zivPhoto);
        ivClear = (ImageView) findViewById(R.id.ivClear);
        ivSave = (ImageView) findViewById(R.id.ivSave);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tvContent = (TextView) findViewById(R.id.tvContent);
        vLine = (View) findViewById(R.id.vLine);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClear:
                finish();
                break;

            case R.id.ivSave:
                //儲存
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        savePhoto();
                    }
                    Common.askPermissionByActivity(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_READ_WRITE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    savePhoto();

                } else {
                    //shouldShowRequestPermissionRationale第一次會返回true,因使用者未拒絕使用權限
                    // 接下來如果再次使用功能出現權限dialog,勾選了不在顯示(Don,t ask again!)時,則此method會返回false
                    // 此時再次要使用功能時,Permission dialog則不在提示,需額外自訂dialog引導使用者開啟權限的介面
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_READ_WRITE:
                    savePhoto();
                    break;
            }
        }
    }

    private void savePhoto() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
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

            FileOutputStream fos = null;
            boolean isSuccess = false;
            try {
                fos = new FileOutputStream(file);
                if (bitmap != null) {
                    isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
                }
                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (isSuccess) {
                    Uri uri = Uri.fromFile(file);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    showToast(context, context.getResources().getString(R.string.save_photo));

                } else {
                    showToast(context, context.getResources().getString(R.string.un_save_photo));
                }
            }
        }
    }


}

