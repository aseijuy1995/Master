package nom.cp101.master.master.Account.MyCourse.CLASS;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ImageTask;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/4/20.
 */



public class MyCourseSingleFragment extends Fragment {
    private static final String TAG = "Single Course Fragment";
    private MyTask deleteTask,findByCourseIdTask;
    private TextView single_name,single_date,single_summary,single_location,single_need,single_qualification,single_note;
    private TextView single_price,single_number;
    private CheckableImageButton single_manage_btn;
    private ImageView single_image;
    Course course ;
    Button single_contect,single_apply;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_single_course_frag,container,false);
        single_name = view.findViewById(R.id.single_name);
        single_date = view.findViewById(R.id.single_date);
        single_summary = view.findViewById(R.id.single_summary);
        single_location = view.findViewById(R.id.single_location);
        single_need = view.findViewById(R.id.single_need);
        single_qualification = view.findViewById(R.id.single_qualification);
        single_note = view.findViewById(R.id.single_note);
        single_price = view.findViewById(R.id.single_price);
        single_number = view.findViewById(R.id.single_number);
        single_note = view.findViewById(R.id.single_note);
        single_manage_btn = view.findViewById(R.id.single_course_manage_btn);
        single_image = view.findViewById(R.id.single_image);
        single_apply = view.findViewById(R.id.single_apply);
        single_contect = view.findViewById(R.id.single_contect);


        Bundle bundle = getArguments();

        if(bundle != null){
            course =(Course)bundle.getSerializable("course");

            Date date = course.getCourse_date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(date);
            String price = String.valueOf(course.getCourse_price());
            String number = String.valueOf(course.getCourse_people_number());

            single_name.setText(course.getCourse_name());
            single_date.setText(dateStr);
            single_price.setText("NT$ "+price);
            single_summary.setText(course.getCourse_content());
            single_location.setText(course.getCourse_location());
            single_qualification.setText(course.getCourse_qualification());
            single_number.setText(number);
            single_need.setText(course.getCourse_need());
            single_note.setText(course.getCourse_note());
        }

        int imageSize = getActivity().getResources().getDisplayMetrics().widthPixels;
        String url = Common.URL + "/photoServlet";
        int photo_id = course.getCourse_image_id();
        Bitmap bitmap = null;
        ImageTask imageTask = new ImageTask(url, photo_id, imageSize);
        try {
            bitmap = imageTask.execute().get();
            if(bitmap == null){
                single_image.setImageResource(R.drawable.account_bulldog);
            }else{
                single_image.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        manageBtnClick();
        applyClick();
        return view;
    }

    private void applyClick() {

        single_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = "123";
                if(checkApply(course.getCourse_id(),user_id) == true){
                    Apply apply = new Apply(0,course.getCourse_id(),user_id,1,null);
                    insertApply(apply);
                    Common.showToast(getContext(),"報名成功");
                }else{
                    Common.showToast(getContext(),"您已經報名此課程");
                }
            }
        });
    }
    private void manageBtnClick() {

//        final Course this_course = new Course(course.getCourse_id(),course.getUser_id(),
//                course.getProfession_id(),course.getCourse_category_id(),course.getCourse_name(),
//                course.getCourse_date(),course.getCourse_content(),course.getCourse_price(),
//                course.getCourse_need(),course.getCourse_qualification(),course.getCourse_location(),
//                course.getCourse_apply_deadline(),course.getCourse_people_number(),
//                course.getCourse_applied_number(),course.getCourse_image_id(),course.getCourse_note());

        single_manage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),view);
                popupMenu.inflate(R.menu.account_single_course_menu);
                final List<Apply> applies = findApplyByCourseId(course.getCourse_id());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.menu_manage_course:
                                Fragment fragment = new MyCourseUpdateFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("course",course);
                                fragment.setArguments(bundle);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.add(R.id.fragment_container,fragment).commit();
                                transaction.addToBackStack(null);
                                break;
                            case R.id.menu_manage_student:
                                Fragment studentFragment = new MyCourseStudentManageFragment();

                                if(applies != null){
                                    Bundle applyBundle = new Bundle();
                                    applyBundle.putSerializable("applies", (Serializable) applies);
                                    applyBundle.putString("course_name",course.getCourse_name());
                                    studentFragment.setArguments(applyBundle);
                                    FragmentTransaction studentTransaction = getFragmentManager().beginTransaction();
                                    studentTransaction.replace(R.id.fragment_container,studentFragment).commit();
                                    studentTransaction.addToBackStack(null);
                                }else{
                                    Common.showToast(getContext(),"appies = null");
                                }
                                break;
                            case R.id.menu_delete_course:
                                final AlertDialog.Builder addAlertBuilder= new AlertDialog.Builder(getActivity());
                                View alertView = getLayoutInflater().inflate(R.layout.account_dialog,null);
                                TextView title = alertView.findViewById(R.id.alert_title);
                                TextView content = alertView.findViewById(R.id.alert_content);
                                Button okBtn = (Button)alertView.findViewById(R.id.alert_ok);
                                Button cancelBtn = (Button)alertView.findViewById(R.id.alert_cancel);
                                title.setText("WARNING!");
                                content.setText("Are you sure you want to delete this course?");

                                addAlertBuilder.setView(alertView);
                                final AlertDialog alertDialog = addAlertBuilder.create();
                                alertDialog.show();

                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Boolean isDeleteCourse;
                                        Boolean isDeleteApply = false;
                                        if(applies == null){
                                            isDeleteCourse = deleteCourseServlet("finalCourseServlet",course);
                                            if(isDeleteCourse = true){
                                                Common.showToast(getActivity(), R.string.msg_DeleteSuccess);
                                            }else{
                                                Common.showToast(getActivity(), R.string.msg_DeleteFail);
                                            }
                                        }else {
                                            isDeleteApply = deleteApplyServlet("applyServlet", course.getCourse_id());
                                            isDeleteCourse = deleteCourseServlet("finalCourseServlet",course);
                                            if(isDeleteApply == true && isDeleteCourse == true){
                                                Common.showToast(getActivity(), R.string.msg_DeleteSuccess);
                                            }else {
                                                Common.showToast(getActivity(), R.string.msg_DeleteFail);
                                            }
                                        }
                                        Fragment myCourseFragment = new MyCourseMainFragment();
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,myCourseFragment).commit();
                                        alertDialog.dismiss();
                                    }
                                });
                                cancelBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }



    boolean deleteCourseServlet(String servlet, Course course){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/" + servlet;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "delete");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            jsonObject.addProperty("course", gson.toJson(course));
            int count = 0;
            try {
                deleteTask = new MyTask(url, jsonObject.toString());
                String result = deleteTask.execute().get();
                count = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Log.d(TAG, "Delete Course Faild");
                return false;
            } else {
                return true;
            }
        } else {
            Log.d(TAG, "Connect Faild");
            return false;
        }
    }

    boolean deleteApplyServlet(String servlet, int course_id){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/" + servlet;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "deleteByCourseId");
            jsonObject.addProperty("course_id",course_id);
            int count = 0;
            try {
                deleteTask = new MyTask(url, jsonObject.toString());
                String result = deleteTask.execute().get();
                count = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Log.d(TAG, "Delete Apply Faild");
                return false;
            } else {
                return true;
            }
        } else {
            Log.d(TAG, "Connect Faild");
            return false;
        }
    }

    List<Apply> findApplyByCourseId(int course_id){
        List<Apply> applies = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/applyServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByCourseId");
            jsonObject.addProperty("course_id",course_id);
            findByCourseIdTask = new MyTask(url, jsonObject.toString());
            try {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                String jsonIn = findByCourseIdTask.execute().get();
                Type listType = new TypeToken<List<Apply>>(){ }.getType();
                applies = gson.fromJson(jsonIn,listType);
                return applies;
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return null;
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            Log.d(TAG, "Connect Faild");
            return null;
        }

    }

    public int insertApply(Apply apply){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/applyServlet";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "insert");
            jsonObject.addProperty("apply", gson.toJson(apply));
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"報名失敗");
                return id;
            } else {
                Log.d(TAG,"報名成功");
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return 0;
        }
    }

    public boolean checkApply(int course_id,String user_id){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/applyServlet";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "check");
            jsonObject.addProperty("course_id", course_id);
            jsonObject.addProperty("user_id", user_id);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"沒有重複");
                return true;
            } else {
                Log.d(TAG,"已經重複");
                return  false;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return false;
        }
    }


}
