package nom.cp101.master.master.Account.MyCourse;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ImageTask;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static android.app.Activity.RESULT_OK;
import static nom.cp101.master.master.Main.Common.getTimeAsName;
import static nom.cp101.master.master.Main.Common.showDatePicker;
import static nom.cp101.master.master.Main.Common.showToast;

/**
 * Created by chunyili on 2018/4/25.
 */

public class UpdateCourseFragment extends Fragment implements View.OnClickListener {
    private String TAG = "UpdateCourseFragment";
    private EditText update_course_name, update_course_detail,
            update_course_date, update_course_location, update_course_need,
            update_course_qualification, update_course_note,
            update_course_price, update_course_number, update_course_apply_deadline;
    private Button update_course_send;
    private Course course;
    private String name, detail, dateStr, location, need, qualification, note, deadlineStr, numberStr, priceStr, accountID;
    private Date date, deadline;
    private int price, number, courseID, applieNumber, categoryID, professionID, photo_id, status_id;
    private ImageView update_course_image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, croppedImageUri;
    private byte[] image;
    private Context context;
    Bitmap bitmap = null;

    private final int PERMISSION_CAMERA_READ = 0;
    private final int PERMISSION_READ = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_update_frag, container, false);
        findView(view);
        context = getContext();
        Bundle bundle = getArguments();
        if (bundle != null) {
            course = (Course) bundle.getSerializable("course");
            setImage();
            setInfo();
        }
        update_course_image.setOnClickListener(this);
        update_course_date.setOnClickListener(this);
        update_course_apply_deadline.setOnClickListener(this);
        update_course_send.setOnClickListener(this);
        return view;
    }

    private void findView(View view) {
        update_course_name = (EditText) view.findViewById(R.id.update_course_name);
        update_course_detail = (EditText) view.findViewById(R.id.update_course_detail);
        update_course_date = (EditText) view.findViewById(R.id.update_course_date);
        update_course_location = (EditText) view.findViewById(R.id.update_course_location);
        update_course_need = (EditText) view.findViewById(R.id.update_course_need);
        update_course_qualification = (EditText) view.findViewById(R.id.update_course_qualification);
        update_course_note = (EditText) view.findViewById(R.id.update_course_note);
        update_course_price = (EditText) view.findViewById(R.id.update_course_price);
        update_course_number = (EditText) view.findViewById(R.id.update_course_number);
        update_course_apply_deadline = (EditText) view.findViewById(R.id.update_course_apply_deadline);
        update_course_send = (Button) view.findViewById(R.id.update_course_send);
        update_course_image = (ImageView) view.findViewById(R.id.update_course_image);
    }

    private void setImage() {
        int imageSize = getActivity().getResources().getDisplayMetrics().widthPixels;
        String url = Common.URL + "/photoServlet";
        int photo_id = course.getCourse_image_id();

        ImageTask imageTask = new ImageTask(url, photo_id, imageSize);
        try {
            bitmap = imageTask.execute().get();
            if (bitmap != null) {
                update_course_image.setImageBitmap(bitmap);

            } else {
                update_course_image.setImageResource(R.drawable.account_add_image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInfo() {
        Date date = course.getCourse_date();
        Date deadline = course.getCourse_apply_deadline();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String oldDateStr = dateFormat.format(date);
        String oldDeadlineStr = dateFormat.format(deadline);
        int price = course.getCourse_price();
        int number = course.getCourse_people_number();
        String oldPriceStr = String.valueOf(price);
        String oldNumberStr = String.valueOf(number);

        update_course_number.setText(oldNumberStr);
        update_course_name.setText(course.getCourse_name());
        update_course_detail.setText(course.getCourse_content());
        update_course_date.setText(oldDateStr);
        update_course_price.setText(oldPriceStr);
        update_course_location.setText(course.getCourse_location());
        update_course_need.setText(course.getCourse_need());
        update_course_qualification.setText(course.getCourse_qualification());
        update_course_apply_deadline.setText(oldDeadlineStr);
        update_course_note.setText(course.getCourse_note());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_course_image:
                pictureDialog();
                break;

            case R.id.update_course_date:
                showDatePicker(getContext(), update_course_date);
                break;

            case R.id.update_course_apply_deadline:
                showDatePicker(getContext(), update_course_apply_deadline);
                break;

            case R.id.update_course_send:
                catchUpdate();

                if (checkET()) {
                    final AlertDialog.Builder addAlertBuilder = new AlertDialog.Builder(context);
                    View alertView = getLayoutInflater().inflate(R.layout.account_dialog, null);
                    addAlertBuilder.setView(alertView);
                    addAlertBuilder.setCancelable(false);
                    Button okBtn = (Button) alertView.findViewById(R.id.alert_ok);
                    Button cancelBtn = (Button) alertView.findViewById(R.id.alert_cancel);
                    TextView alertContent = (TextView) alertView.findViewById(R.id.alert_content);

                    alertContent.setText(context.getResources().getString(R.string.update_course_content));
                    final AlertDialog alertDialog = addAlertBuilder.create();
                    alertDialog.show();

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (((BitmapDrawable) update_course_image.getDrawable()).getBitmap().equals(bitmap)) {
                                updateCourse();

                            } else {
                                int photo_id = updateImage();
                                if (photo_id != 0) {
                                    updateCourse();
                                }
                            }
                            alertDialog.dismiss();
                        }
                    });

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                } else {
                    showToast(context, context.getResources().getString(R.string.empty_field));
                }
                break;
        }
    }

    private void pictureDialog() {
        final String[] list = context.getResources().getStringArray(R.array.listCoursePhoto);
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.picture))
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        switch (item) {
                            //Take Picture
                            case 0:
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    // 已拿取得權限 準備開啟相機
                                    cameraTurnOn();
                                }
                                Common.askPermissionByFragment(context,
                                        UpdateCourseFragment.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE},
                                        PERMISSION_CAMERA_READ);
                                break;

                            //Pick Picture
                            case 1:
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    openPhotoPicker();
                                }
                                Common.askPermissionByFragment(context,
                                        UpdateCourseFragment.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        PERMISSION_READ);
                                break;

                            case 2:
                                dialogInterface.dismiss();
                                break;

                            default:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                })
                .create()
                .show();
    }

    private void cameraTurnOn() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, getTimeAsName() + "_picture.jpg");

            if (Build.VERSION.SDK_INT >= 24) {
                contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                contentUri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            if (isIntentAvailable(getContext(), intent)) {
                startActivityForResult(intent, REQ_TAKE_PICTURE);

            } else {
                showToast(context, R.string.no_camera);
            }
        } else {
            showToast(context, R.string.errorExternalStorage);
        }
    }

    public void openPhotoPicker() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    cameraTurnOn();

                } else {
                    //shouldShowRequestPermissionRationale第一次會返回true,因使用者未拒絕使用權限
                    // 接下來如果再次使用功能出現權限dialog,勾選了不在顯示(Don,t ask again!)時,則此method會返回false
                    // 此時再次要使用功能時,Permission dialog則不在提示,需額外自訂dialog引導使用者開啟權限的介面
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)
                            && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
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

            case PERMISSION_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPhotoPicker();

                } else {
                    //shouldShowRequestPermissionRationale第一次會返回true,因使用者未拒絕使用權限
                    // 接下來如果再次使用功能出現權限dialog,勾選了不在顯示(Don,t ask again!)時,則此method會返回false
                    // 此時再次要使用功能時,Permission dialog則不在提示,需額外自訂dialog引導使用者開啟權限的介面
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
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;

                case REQ_PICK_PICTURE:
                    crop(intent.getData());
                    break;

                case REQ_CROP_PICTURE:
                    try {
                        Bitmap picture = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(croppedImageUri));
                        update_course_image.setImageBitmap(picture);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        image = out.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.e("main", e.toString());
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
        croppedImageUri = Uri.fromFile(file);
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 4);
        cropIntent.putExtra("aspectY", 3);
        cropIntent.putExtra("outputX", 800);
        cropIntent.putExtra("outputY", 600);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropIntent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(cropIntent, REQ_CROP_PICTURE);
    }

    private Boolean checkET() {
        if (update_course_image.getDrawable() == null
                || update_course_image.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.account_add_image).getConstantState())
                || name.isEmpty() || detail.isEmpty() || dateStr.isEmpty() || priceStr.isEmpty()
                || location.isEmpty() || need.isEmpty() || numberStr.isEmpty() || qualification.isEmpty()
                || deadlineStr.isEmpty() || note.isEmpty()) {
            return false;

        } else {
            return true;
        }
    }

    private void catchUpdate() {
        name = update_course_name.getText().toString().trim();
        detail = update_course_detail.getText().toString().trim();
        dateStr = update_course_date.getText().toString().trim();
        priceStr = update_course_price.getText().toString().trim();
        location = update_course_location.getText().toString().trim();
        need = update_course_need.getText().toString().trim();
        numberStr = update_course_number.getText().toString().trim();
        qualification = update_course_qualification.getText().toString().trim();
        deadlineStr = update_course_apply_deadline.getText().toString().trim();
        note = update_course_note.getText().toString().trim();
        number = Integer.valueOf(numberStr);
        price = Integer.valueOf(priceStr);

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(dateStr);
            deadline = dateFormat.parse(deadlineStr);
            Log.d(TAG, String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        categoryID = course.getCourse_category_id();
        courseID = course.getCourse_id();
        accountID = course.getUser_id();
//        appliedNumber = course.getCourse_applied_number();
        professionID = course.getProfession_id();
        photo_id = course.getCourse_image_id();
        status_id = course.getCourse_status_id();
    }

    private int updateImage() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/photoServlet";
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "update");
            jsonObject.addProperty("photo", imageBase64);
            jsonObject.addProperty("photo_id", course.getCourse_image_id());
            int count = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                count = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return count;

        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
            return 0;
        }
    }

    private void updateCourse() {
        final Gson gson = new Gson();
        final Gson gsonWithDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Course courseDetail = new Course(categoryID, 2, name, detail, price, need, qualification, location, note);
        Course courseProfile = new Course(courseID, accountID, categoryID, date, deadline, number, photo_id, status_id);


        int result1 = Common.insertUpdateCourseServlet(getActivity(), TAG, "CourseServlet", "update", gsonWithDate, courseProfile);
        int result2 = Common.insertUpdateCourseServlet(getActivity(), TAG, "CourseDetailServlet", "update", gson, courseDetail);

        if (result1 != 0 || result2 != 0) {
            showToast(getActivity(), R.string.updateSuccess);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.left_in,
                            R.anim.right_out,
                            R.anim.right_in,
                            R.anim.left_out)
                    .replace(R.id.fragment_container, new MyCourseFragment())
                    .commit();

        } else {
            showToast(getActivity(), R.string.updateFailed);
        }
    }


}

