package nom.cp101.master.master.Account.MyCourse;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ImageTask;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static android.app.Activity.RESULT_OK;
import static nom.cp101.master.master.Main.Common.showDatePicker;
import static nom.cp101.master.master.Main.Common.showToast;

/**
 * Created by chunyili on 2018/4/25.
 */

public class UpdateCourseFragment extends Fragment {
    private String TAG = "UpdateCourseFragment";
    private EditText update_course_name,update_course_detail,
            update_course_date,update_course_location,update_course_need,
            update_course_qualification,update_course_note,
            update_course_price,update_course_number,update_course_apply_deadline;
    private Button update_course_send;
    private Course course;
    private String name,detail, dateStr,location,need,qualification,note, deadlineStr,numberStr,priceStr,accountID;
    private Date date,deadline;
    private int price,number,courseID,appliedNumber,categoryID,professionID,photo_id,status_id;
    private ImageView update_course_image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, croppedImageUri;
    private Bitmap picture;
    private byte[] image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_update_frag,container,false);
        Bundle bundle = getArguments();
        course = (Course) bundle.getSerializable("course");
        findView(view);
        setImage();
        setInfo();
        imageClick();
        dateClick();
        sendBtnClick();
        return view;
    }
    private void setImage() {
        int imageSize = getActivity().getResources().getDisplayMetrics().widthPixels;
        String url = Common.URL + "/photoServlet";
        int photo_id = course.getCourse_image_id();
        Bitmap bitmap = null;
        ImageTask imageTask = new ImageTask(url, photo_id, imageSize);
        try {
            bitmap = imageTask.execute().get();
            if(bitmap == null){
                update_course_image.setImageResource(R.drawable.account_bulldog);
            }else{
                update_course_image.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        update_course_apply_deadline = (EditText)view.findViewById(R.id.update_course_apply_deadline);
        update_course_send = (Button) view.findViewById(R.id.update_course_send);
        update_course_image = view.findViewById(R.id.update_course_image);
    }

    private void imageClick() {
        update_course_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureDialog();
            }
        });
    }

    private void pictureDialog() {
        List<String> dialogList = new ArrayList<String>();
        dialogList.add(getString(R.string.camera));
        dialogList.add(getString(R.string.albums));
        dialogList.add(getString(R.string.cancel));

        final CharSequence[] list = dialogList.toArray(new String[dialogList.size()]);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.picture));

        alert.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                //Take Picture
                if(list[item] == list[0]){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    file = new File(file, "picture.jpg");
                    contentUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    if (isIntentAvailible(getContext(), intent)) {
                        startActivityForResult(intent, REQ_TAKE_PICTURE);
                    } else {
                        showToast(getContext(),"There is no camera App");
                    }
                    //Pick Picture
                }else if(list[item] == list[1]){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,REQ_PICK_PICTURE);
                }
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private boolean isIntentAvailible(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_PICTURE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    try {
                        picture = BitmapFactory.decodeStream(
                                getActivity().getContentResolver().openInputStream(croppedImageUri));
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
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file,"picture_cropped_picture.jpg");
        croppedImageUri = Uri.fromFile(file);
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.setDataAndType(uri,"image/*");
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX", 1200);
            cropIntent.putExtra("aspectY", 900);
            cropIntent.putExtra("outputX", 1200);
            cropIntent.putExtra("outputY", 900);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        catch (ActivityNotFoundException anfe) {
            showToast(getContext(), "This device doesn't support the crop action!");
        }

    }

    private void dateClick() {
        update_course_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(getContext(),update_course_date);
            }
        });
        update_course_apply_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(getContext(),update_course_apply_deadline);
            }
        });
    }

    private void sendBtnClick() {
        update_course_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchUpdate();

                if(!checkET()){
                    showToast(getActivity(),"Input can't be empty!");
                }else{
                    final AlertDialog.Builder addAlertBuilder= new AlertDialog.Builder(getActivity());
                    View alertView = getLayoutInflater().inflate(R.layout.account_dialog,null);

                    Button okBtn = (Button)alertView.findViewById(R.id.alert_ok);
                    Button cancelBtn = (Button)alertView.findViewById(R.id.alert_cancel);

                    TextView alertContent = alertView.findViewById(R.id.alert_content);
                    alertContent.setText("Are you sure you want to update this course?");

                    addAlertBuilder.setView(alertView);
                    final AlertDialog alertDialog = addAlertBuilder.create();
                    alertDialog.show();

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(image != null){
                                updateImage();
                            }
                            updateCourse();
                            alertDialog.dismiss();
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                }

            }
        });
    }

    private void updateCourse() {
        final Gson gson = new Gson();
        final Gson gsonWithDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Course courseDetail = new Course(categoryID,name,detail,price,need,qualification,location,note);
        Course courseProfile = new Course(courseID,accountID,categoryID, date, deadline,number,appliedNumber,photo_id,status_id);

        int result1 = Common.insertUpdateCourseServlet(getActivity(),TAG,"CourseServlet","update",gsonWithDate,courseProfile);
        int result2 = Common.insertUpdateCourseServlet(getActivity(),TAG,"CourseDetailServlet","update",gson,courseDetail);

        if(result1 != 0 || result2 != 0){
            showToast(getActivity(), R.string.updateSuccess);
            Fragment myCourse = new MyCourseFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,myCourse).commit();
        }else{
            showToast(getActivity(), R.string.updateFailed);
        }
    }


    private Boolean checkET() {
        if(name.isEmpty()||detail.isEmpty()||dateStr.isEmpty()||priceStr.isEmpty()
                ||location.isEmpty()||need.isEmpty()||numberStr.isEmpty()||qualification.isEmpty()
                ||deadlineStr.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private void catchUpdate() {
        name = update_course_name.getText().toString().trim();
        detail = update_course_detail.getText().toString().trim();
        dateStr = update_course_date.getText().toString().trim();
        location = update_course_location.getText().toString().trim();
        numberStr = update_course_number.getText().toString().trim();
        priceStr = update_course_price.getText().toString().trim();
        need = update_course_need.getText().toString().trim();
        qualification = update_course_qualification.getText().toString().trim();
        note = update_course_note.getText().toString().trim();
        deadlineStr = update_course_apply_deadline.getText().toString().trim();
        categoryID = course.getCourse_category_id();
        courseID = course.getCourse_id();
        accountID = course.getUser_id();
        appliedNumber = course.getCourse_applied_number();
//        professionID = course.getProfession_id();
        photo_id = course.getCourse_image_id();
        status_id = course.getCourse_status_id();


        number = Integer.valueOf(numberStr);
        price = Integer.valueOf(priceStr);

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(dateStr);
            deadline = dateFormat.parse(deadlineStr);
            Log.d(TAG, String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG,"錯誤");
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

        update_course_name.setText(course.getCourse_name());
        update_course_detail.setText(course.getCourse_content());
        update_course_date.setText(oldDateStr);
        update_course_location.setText(course.getCourse_location());
        update_course_need.setText(course.getCourse_need());
        update_course_qualification.setText(course.getCourse_qualification());
        update_course_note.setText(course.getCourse_note());
        update_course_price.setText(oldPriceStr);
        update_course_number.setText(oldNumberStr);
        update_course_apply_deadline.setText(oldDeadlineStr);
    }

    private void updateImage() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/photoServlet";
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "update");
            jsonObject.addProperty("photo", imageBase64);
            jsonObject.addProperty("photo_id",course.getCourse_image_id());
            int count = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                count = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Log.d(TAG,"update Image fail");
            } else {
                Log.d(TAG,"update Image success");
            }
        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }
}

