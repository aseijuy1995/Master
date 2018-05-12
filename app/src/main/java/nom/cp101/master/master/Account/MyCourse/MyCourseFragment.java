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

import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;


public class MyCourseFragment extends Fragment {

    FloatingActionButton fabBtn;
    FragmentTransaction transaction;
    private MyTask courseGetAllTask;
    private static String TAG = "MyCourseFragment";
    VerticalInfiniteCycleViewPager pager;
    private int identity;
    private String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_my_course_frag, container, false);
        pager = (VerticalInfiniteCycleViewPager)view.findViewById(R.id.verticle_cycle);
        fabBtn = view.findViewById(R.id.fab_btn_add);
        user_id = "billy";
        Common.setUserName(getContext(),user_id);
//        if(user_id.isEmpty()){
//
//        }else{
//
//        }

//        identity = 0;
//        if(identity == 0){
//            fabBtn.hide();
//        }
        addClick();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        connectServlet("finalCourseServlet","getAll");
    }

    private void addClick() {
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addCourseFragment = new AddCourseFragment();
                transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, addCourseFragment).commit();
                transaction.addToBackStack(null);
            }
        });
    }

    private void connectServlet(String servletStr, String value){
        if (Common.networkConnected(getActivity())){
            String url = Common.URL + "/" + servletStr;
            List<Course> courses = null;
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
            } else {
                pager.setAdapter(new MyAdapter(courses, getContext(),getActivity()));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }
}


