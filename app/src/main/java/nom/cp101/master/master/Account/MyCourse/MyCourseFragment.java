package nom.cp101.master.master.Account.MyCourse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.COACH_ACCESS;
import static nom.cp101.master.master.Main.Common.STUDENT_ACCESS;


public class MyCourseFragment extends Fragment {

    FloatingActionButton fabBtn;
    FragmentTransaction transaction;
    private MyTask courseGetAllTask;
    private static String TAG = "MyCourseFragment";
    VerticalInfiniteCycleViewPager pager;
    private int access;
    private String user_id;
    private ImageView course_didnot_signIn;
    private ArrayList<Course> courses;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_my_course_frag, container, false);
        pager = (VerticalInfiniteCycleViewPager)view.findViewById(R.id.verticle_cycle);
        fabBtn = view.findViewById(R.id.fab_btn_add);
        course_didnot_signIn = view.findViewById(R.id.course_didnot_signIn);
        user_id = Common.getUserName(getContext());
        access = Common.getUserAccess(getContext());
        if(user_id.isEmpty()){
            course_didnot_signIn.setVisibility(View.VISIBLE);
            pager.setVisibility(View.INVISIBLE);
            fabBtn.setVisibility(View.INVISIBLE);
        }else if(access == COACH_ACCESS){
            courses = findCourseByCoach("findCourseByCoach",user_id);
            if(courses != null){
                pager.setAdapter(new MyAdapter(courses, getContext(),getActivity()));
            }

        }else if(access == STUDENT_ACCESS){

            courses = findCourseByStudent("findCourseByStudent",user_id);
            if(courses != null){
                pager.setAdapter(new MyAdapter(courses, getContext(),getActivity()));
            }

            fabBtn.setVisibility(View.INVISIBLE);
        }
        addClick();
        return view;
    }


    private void addClick() {
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addCourseFragment = new AddCourseFragment();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, addCourseFragment).commit();
                transaction.addToBackStack(null);
            }
        });
    }

    private ArrayList<Course> selectAll(String servletStr, String value){
        if (Common.networkConnected(getActivity())){
            String url = Common.URL + "/" + servletStr;
            ArrayList<Course> courses = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action",value);
            String jsonOut = jsonObject.toString();
            courseGetAllTask = new MyTask(url,jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Course>>(){ }.getType();
                courses = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (courses == null || courses.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoCoursesFound);
                return null;
            } else {

                return courses;
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return null;
        }
    }

    private ArrayList<Course> findCourseByCoach( String value,String user_id){
        if (Common.networkConnected(getActivity())){
            String url = Common.URL + "/finalCourseServlet";
            ArrayList<Course> courses = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action",value);
            jsonObject.addProperty("user_id",user_id);
            String jsonOut = jsonObject.toString();
            courseGetAllTask = new MyTask(url,jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Course>>(){ }.getType();
                courses = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (courses == null || courses.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoCoursesFound);
                return null;
            } else {

                return courses;
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return null;
        }
    }


    private ArrayList<Course> findCourseByStudent( String value,String user_id){
        if (Common.networkConnected(getActivity())){
            String url = Common.URL + "/finalCourseServlet";
            ArrayList<Course> courses = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action",value);
            jsonObject.addProperty("user_id",user_id);
            String jsonOut = jsonObject.toString();
            courseGetAllTask = new MyTask(url,jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Course>>(){ }.getType();
                courses = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (courses == null || courses.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoCoursesFound);
                return null;
            } else {

                return courses;
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return null;
        }
    }
}


