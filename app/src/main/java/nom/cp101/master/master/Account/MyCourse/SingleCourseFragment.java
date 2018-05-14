package nom.cp101.master.master.Account.MyCourse;

import android.content.Intent;
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
import nom.cp101.master.master.Message.CLASS.MessageActivity;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.COACH_ACCESS;
import static nom.cp101.master.master.Main.Common.STUDENT_ACCESS;

/**
 * Created by chunyili on 2018/4/20.
 */



public class SingleCourseFragment  extends Fragment {
    private static final String TAG = "Single Course Fragment";
    private MyTask deleteTask,findByCourseIdTask;
    private TextView single_name,single_date,single_summary,single_location,single_need,single_qualification,single_note;
    private TextView single_price,single_number;
    private CheckableImageButton single_manage_btn;
    private ImageView single_image;
    Course course ;
    Button single_contect,single_apply;
    String user_id,friend_name, room_position;
    private int access;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_single_course_frag,container,false);
        access = Common.getUserAccess(getContext());
        user_id = Common.getUserName(getContext());
        findView(view);
        getBundle();
        setImage();
        manageBtnClick();
        applyClick();
        contectClick();
        return view;
    }

    private void contectClick() {
        single_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(access != COACH_ACCESS){
                    friend_name = findUserNameById(course.getUser_id());
                    if(!friend_name.equals("")){
                        room_position = findRoomPosition(user_id,friend_name);
                        if(checkChatRoom(user_id,friend_name) == true){
                            Common.contectUser(user_id,friend_name,getContext(),getActivity());
                            enterChatRoom(room_position,user_id,friend_name);
                        }else{
                            enterChatRoom(room_position,user_id,friend_name);
                        }
                    }
                }
            }
        });
    }

    private void enterChatRoom(String room_position,String user_id,String friend_name) {
        Bundle bundle = new Bundle();
        bundle.putString("room_position",room_position);
        bundle.putString("userName",user_id);
        bundle.putString("friendName",friend_name);
        Intent intent = new Intent(getContext(),MessageActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
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
                single_image.setImageResource(R.drawable.account_bulldog);
            }else{
                single_image.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBundle() {
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

    }

    private void findView(View view) {
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

        if(access == STUDENT_ACCESS){
            single_manage_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void applyClick() {
        single_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(access != COACH_ACCESS){
                    if(checkApply(course.getCourse_id(),user_id) == true){
                        Apply apply = new Apply(0,course.getCourse_id(),user_id,1,null);
                        insertApply(apply);
                        Common.showToast(getContext(),"報名成功");
                    }else{
                        Common.showToast(getContext(),"您已經報名此課程");
                    }
                }
            }
        });
    }
    private void manageBtnClick() {
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
                                Fragment fragment = new UpdateCourseFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("course",course);
                                fragment.setArguments(bundle);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.add(R.id.fragment_container,fragment).commit();
                                transaction.addToBackStack(null);
                                break;
                            case R.id.menu_manage_student:
                                Fragment studentFragment = new StudentManageFragment();

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
                                        Fragment myCourseFragment = new MyCourseFragment();
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
    public String findUserNameById(String user_id){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findUserNameById");
            jsonObject.addProperty("user_id", user_id);
            String user_name = null;
            try {
                user_name = new MyTask(url, jsonObject.toString()).execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (user_name.isEmpty()) {
                Log.d(TAG,"找不到此人");
                Common.showToast(getContext(),"找不到此用戶");
                return "";
            } else {
                Log.d(TAG,"user_id :"+user_id+"用戶名稱為"+user_name);
                return  user_name;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return "";
        }
    }

    public boolean checkChatRoom(String user_id,String friend_name){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "checkChatRoom");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("friend_name", friend_name);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"聊天室沒有重複");

                return true;
            } else {
                Log.d(TAG,"聊天室已經重複");
                return  false;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return false;
        }
    }

    public String findRoomPosition(String user_id,String friend_name){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findRoomPosition");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("friend_name", friend_name);
            String room_position = null;
            try {
                room_position = new MyTask(url, jsonObject.toString()).execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (room_position.isEmpty()) {
                Log.d(TAG,"找不到此聊天室");
                return "";
            } else {
                Log.d(TAG,"此聊天室名稱為"+room_position);
                return  room_position;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return "";
        }
    }



}
