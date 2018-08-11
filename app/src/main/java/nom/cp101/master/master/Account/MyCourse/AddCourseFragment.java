package nom.cp101.master.master.Account.MyCourse;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.Account.MyAccount.Profession;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static android.app.Activity.RESULT_OK;
import static nom.cp101.master.master.Main.Common.getTimeAsName;
import static nom.cp101.master.master.Main.Common.insertUpdateCourseServlet;
import static nom.cp101.master.master.Main.Common.showToast;

public class AddCourseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final static String TAG = "AddCourseFragment";
    private ImageView add_course_image;
    private Button add_course_send;
    private EditText add_course_name, add_course_detail,
            add_course_date, add_course_location, add_course_need,
            add_course_qualification, add_course_note,
            add_course_price, add_course_number, add_course_apply_deadline;
    private Spinner add_course_spinner;

    private String name, detail, dateStr, location, need, qualification, note, deadlineStr, numberStr, priceStr;
    private Date date, deadline;
    private int price, number, profession;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, croppedImageUri;
    private byte[] image;
    private String user_id;
    private ArrayList<Profession> professions;
    private Context context;

    private final int PERMISSION_CAMERA_READ_WRITE = 0;
    private final int PERMISSION_READ = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_add_frag, container, false);
        findView(view);
        context = getContext();
        user_id = Common.getUserName(getContext());

        setSpinner();
        add_course_image.setOnClickListener(this);
        add_course_spinner.setOnItemSelectedListener(this);
        add_course_date.setOnClickListener(this);
        add_course_apply_deadline.setOnClickListener(this);
        add_course_send.setOnClickListener(this);
        return view;
    }

    private void findView(View view) {
        add_course_image = (ImageView) view.findViewById(R.id.add_course_image);
        add_course_name = (EditText) view.findViewById(R.id.add_course_name);
        add_course_spinner = view.findViewById(R.id.add_course_spinner);
        add_course_detail = (EditText) view.findViewById(R.id.add_course_detail);
        add_course_date = (EditText) view.findViewById(R.id.add_course_date);
        add_course_price = (EditText) view.findViewById(R.id.add_course_price);
        add_course_location = (EditText) view.findViewById(R.id.add_course_location);
        add_course_need = (EditText) view.findViewById(R.id.add_course_need);
        add_course_number = (EditText) view.findViewById(R.id.add_course_number);
        add_course_qualification = (EditText) view.findViewById(R.id.add_course_qualification);
        add_course_apply_deadline = (EditText) view.findViewById(R.id.add_course_apply_deadline);
        add_course_note = (EditText) view.findViewById(R.id.add_course_note);
        add_course_send = (Button) view.findViewById(R.id.add_course_send);
    }

    private void setSpinner() {
        ArrayList<String> professionNames = new ArrayList<>();
        professions = findProfessionById(user_id);
        if (professions != null && professions.size() > 0) {
            for (Profession profession : professions) {
                String proName = profession.getProfession_name();
                professionNames.add(proName);
            }
            profession = professions.get(0).getProfession_id();
        }
        ArrayAdapter<String> adapterPlace = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, professionNames);
        adapterPlace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_course_spinner.setAdapter(adapterPlace);
        add_course_spinner.setSelection(0, true);
    }

    public ArrayList<Profession> findProfessionById(String user_id) {
        if (Common.networkConnected(getActivity())) {
            ArrayList<Profession> proList = new ArrayList<>();
            String url = Common.URL + "/UserInfo";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findProfessionById");
            jsonObject.addProperty("user_id", user_id);
            String jsonOut = jsonObject.toString();
            MyTask courseGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Type listType = new TypeToken<List<Profession>>() {
                }.getType();
                proList = new Gson().fromJson(jsonIn, listType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (proList == null) {
                Log.d(TAG, "無專業項目");
                return null;

            } else {
                Log.d(TAG, "搜尋成功");
                return proList;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(getContext(), "no network");
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_course_image:
                pictureDialog();
                break;

            case R.id.add_course_date:
                Common.showDatePicker(getContext(), add_course_date);
                break;

            case R.id.add_course_apply_deadline:
                Common.showDatePicker(getContext(), add_course_apply_deadline);
                break;

            case R.id.add_course_send:
                getCourse();
                if (checkET()) {
                    final AlertDialog.Builder addAlertBuilder = new AlertDialog.Builder(context);
                    View alertView = getLayoutInflater().inflate(R.layout.account_dialog, null);
                    addAlertBuilder.setView(alertView);
                    Button okBtn = (Button) alertView.findViewById(R.id.alert_ok);
                    Button cancelBtn = (Button) alertView.findViewById(R.id.alert_cancel);
                    final AlertDialog alertDialog = addAlertBuilder.create();
                    alertDialog.show();

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (image != null) {
                                int photo_id = insertImage(user_id);
                                if (photo_id != 0) {
                                    createCourse(photo_id);
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
                                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    // 已拿取得權限 準備開啟相機
                                    cameraTurnOn();
                                }
                                Common.askPermissionByFragment(context,
                                        AddCourseFragment.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSION_CAMERA_READ_WRITE);
                                break;

                            //Pick Picture
                            case 1:
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    openPhotoPicker();
                                }
                                Common.askPermissionByFragment(context,
                                        AddCourseFragment.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA_READ_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
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
                        add_course_image.setImageBitmap(picture);

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

    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.add_course_spinner:
                profession = professions.get(position).getProfession_id();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getCourse() {
        name = add_course_name.getText().toString().trim();
        detail = add_course_detail.getText().toString().trim();
        dateStr = add_course_date.getText().toString().trim();
        priceStr = add_course_price.getText().toString().trim();
        location = add_course_location.getText().toString().trim();
        need = add_course_need.getText().toString().trim();
        numberStr = add_course_number.getText().toString().trim();
        qualification = add_course_qualification.getText().toString().trim();
        deadlineStr = add_course_apply_deadline.getText().toString().trim();
        note = add_course_note.getText().toString().trim();

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
    }

    private Boolean checkET() {
        if (add_course_image.getDrawable() == null
                || add_course_image.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.account_add_image).getConstantState())
                || name.isEmpty() || add_course_spinner.getSelectedItem() == null
                || detail.isEmpty() || dateStr.isEmpty() || priceStr.isEmpty() || location.isEmpty()
                || need.isEmpty() || numberStr.isEmpty() || qualification.isEmpty() || deadlineStr.isEmpty()
                || note.isEmpty()) {
            return false;

        } else {
            return true;
        }
    }

    private int insertImage(String user_id) {
        if (Common.networkConnected(getActivity())) {
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
            return photo_id;

        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
            return 0;
        }
    }

    private void createCourse(int photo_id) {
        final Gson gson = new Gson();
        final Gson gsonWithDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Course courseDetail = new Course(0, profession, name, detail, price, need, qualification, location, note);
        final int detail_id = insertUpdateCourseServlet(getActivity(), TAG, "CourseDetailServlet", "insert", gson, courseDetail);

        Course courseProfile = new Course(0, user_id, detail_id, date, deadline, number, photo_id, 1);
        final int courseInsertFinish = insertUpdateCourseServlet(getActivity(), TAG, "CourseServlet", "insert", gsonWithDate, courseProfile);

        if (courseInsertFinish != 0) {
            showToast(context, R.string.InsertSuccess);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.left_in,
                            R.anim.right_out,
                            R.anim.right_in,
                            R.anim.left_out)
                    .replace(R.id.fragment_container, new MyCourseFragment())
                    .commit();
        } else {
            showToast(context, R.string.InsertFail);
        }
    }

}
