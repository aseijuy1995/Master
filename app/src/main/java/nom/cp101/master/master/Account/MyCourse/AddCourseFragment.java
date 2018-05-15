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
import static nom.cp101.master.master.Main.Common.insertUpdateCourseServlet;
import static nom.cp101.master.master.Main.Common.showDatePicker;
import static nom.cp101.master.master.Main.Common.showToast;

/**
 * Created by chunyili on 2018/4/18.
 */

public class AddCourseFragment extends Fragment {
    private final static String TAG = "AddCourseFragment";
    private MyTask insertTask;
    private ImageView add_course_image;
    private Button add_course_send;
    private EditText add_course_name,add_course_detail,
        add_course_date,add_course_location,add_course_need,
        add_course_qualification,add_course_note,
        add_course_price,add_course_number,add_course_apply_deadline;
    private String name,detail,dateStr,location,need,qualification,note, deadlineStr,numberStr,priceStr;
    private Date date,deadline;
    private int price,number,profession;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, croppedImageUri;
    private Bitmap picture;
    private byte[] image;
    private String user_id;
    private Spinner add_course_spinner;
    private ArrayList<Profession> professions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_add_frag,container,false);
        findView(view);
        user_id = Common.getUserName(getContext());
        addImageClick();
        dateClick();
        sendBtnClick();
        setSpinner();
        return view;
    }

    private void setSpinner() {
        ArrayList<String> professionNames = new ArrayList<String>();
        professions = findProfessionById(user_id);
        if(professions != null){
            for(Profession profession:professions){
                String proName = profession.getProfession_name();
                professionNames.add(proName);
            }
            profession = professions.get(0).getProfession_id();
        }
        Log.d(TAG, String.valueOf(professionNames));
        ArrayAdapter<String> adapterPlace = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, professionNames);
        adapterPlace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_course_spinner.setAdapter(adapterPlace);
        add_course_spinner.setSelection(0,true);
        add_course_spinner.setOnItemSelectedListener(listener);

    }

    Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            profession = professions.get(position).getProfession_id();
//            Common.showToast(getContext(),String.valueOf(profession));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //TODO
        }
    };

    private void findView(View view) {
        add_course_name = (EditText) view.findViewById(R.id.add_course_name);
        add_course_detail = (EditText) view.findViewById(R.id.add_course_detail);
        add_course_date = (EditText) view.findViewById(R.id.add_course_date);
        add_course_location = (EditText) view.findViewById(R.id.add_course_location);
        add_course_need = (EditText) view.findViewById(R.id.add_course_need);
        add_course_qualification = (EditText) view.findViewById(R.id.add_course_qualification);
        add_course_note = (EditText) view.findViewById(R.id.add_course_note);
        add_course_price = (EditText) view.findViewById(R.id.add_course_price);
        add_course_number = (EditText) view.findViewById(R.id.add_course_number);
        add_course_apply_deadline = (EditText)view.findViewById(R.id.add_course_apply_deadline);
        add_course_send = (Button) view.findViewById(R.id.add_course_send);
        add_course_image =  (ImageView)view.findViewById(R.id.add_course_image);
        add_course_spinner = view.findViewById(R.id.add_course_spinner);
    }

    private void addImageClick() {
        add_course_image.setOnClickListener(new View.OnClickListener() {
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
        add_course_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(getContext(),add_course_date);
            }
        });
        add_course_apply_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(getContext(),add_course_apply_deadline);
            }
        });
    }



    private void sendBtnClick() {
        add_course_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCourse();

                if(!checkET()){
                    showToast(getActivity(),"Input can't be empty!");
                }else{
                    final AlertDialog.Builder addAlertBuilder= new AlertDialog.Builder(getActivity());
                    View alertView = getLayoutInflater().inflate(R.layout.account_dialog,null);
                    Button okBtn = (Button)alertView.findViewById(R.id.alert_ok);
                    Button cancelBtn = (Button)alertView.findViewById(R.id.alert_cancel);

                    addAlertBuilder.setView(alertView);
                    final AlertDialog alertDialog = addAlertBuilder.create();
                    alertDialog.show();

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int photo_id = insertImage(user_id);
                            if(photo_id != 0){
                                createCourse(photo_id);
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
                }
            }
        });
    }

    private int insertImage(String user_id) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/photoServlet";
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "insert");
            jsonObject.addProperty("user_id",user_id);
            jsonObject.addProperty("photo", imageBase64);
            int photo_id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                photo_id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (photo_id == 0) {
//                Common.showToast(getActivity(), R.string.msg_InsertFail);
                return  photo_id;
            } else {
//                Common.showToast(getActivity(), R.string.msg_InsertSuccess);
                return photo_id;
            }
        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
            return 0;
        }
    }

    private void createCourse(int photo_id) {
        final Gson gson = new Gson();
        final Gson gsonWithDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Course courseDetail = new Course(0,profession,name,detail,price,need,qualification,location,note);
        final int detail_id = insertUpdateCourseServlet(getActivity(),TAG,"CourseDetailServlet","insert",gson,courseDetail);
        Course courseProfile = new Course(0,user_id,detail_id, date, deadline,number,photo_id,1);
        final int courseInsertFinish = insertUpdateCourseServlet(getActivity(),TAG,"CourseServlet","insert",gsonWithDate,courseProfile);

        if(courseInsertFinish != 0){
            showToast(getActivity(), R.string.InsertSuccess);
            Fragment myCourse = new MyCourseFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,myCourse).commit();
        }else{
            showToast(getActivity(), R.string.InsertFail);
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

    private void getCourse() {
        name = add_course_name.getText().toString().trim();
        detail = add_course_detail.getText().toString().trim();
        dateStr = add_course_date.getText().toString().trim();
        location = add_course_location.getText().toString().trim();
        need = add_course_need.getText().toString().trim();
        qualification = add_course_qualification.getText().toString().trim();
        note = add_course_note.getText().toString().trim();
        deadlineStr = add_course_apply_deadline.getText().toString().trim();
        numberStr = add_course_number.getText().toString().trim();
        priceStr = add_course_price.getText().toString().trim();
        if(numberStr.isEmpty() || priceStr.isEmpty()){
            showToast(getContext(),"Input can't be empty!");
        }else{
            number = Integer.valueOf(numberStr);
            price = Integer.valueOf(priceStr);
        }

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

    public ArrayList<Profession> findProfessionById(String user_id){
        if (Common.networkConnected(getActivity())) {
            ArrayList<Profession> proList = new ArrayList<Profession>();
            String url = Common.URL + "/UserInfo";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findProfessionById");
            jsonObject.addProperty("user_id", user_id );
            String jsonOut = jsonObject.toString();
            MyTask courseGetAllTask = new MyTask(url,jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Profession>>(){ }.getType();
                proList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (proList == null ) {
                Log.d(TAG,"無專業項目");
//                Common.showToast(getContext(),"還未新增專業項目");
                return null;
            } else {
                Log.d(TAG,"搜尋成功");
                return  proList;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getContext(), "no network");
            return null;
        }
    }
}
