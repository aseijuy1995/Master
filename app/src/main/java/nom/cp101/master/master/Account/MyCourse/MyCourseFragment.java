package nom.cp101.master.master.Account.MyCourse;

import android.content.Context;
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
import android.widget.TextView;

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

public class MyCourseFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "MyCourseFragment";
    private Context context;
    private VerticalInfiniteCycleViewPager pager;
    private FloatingActionButton fabBtn;
    private ImageView course_didnot_signIn;
    private TextView course_not_found;

    private int access;
    private String user_id;
    private ArrayList<Course> courses;
    private MyTask courseGetAllTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_my_course_frag, container, false);
        pager = (VerticalInfiniteCycleViewPager) view.findViewById(R.id.verticle_cycle);
        fabBtn = (FloatingActionButton) view.findViewById(R.id.fab_btn_add);
        course_didnot_signIn = (ImageView) view.findViewById(R.id.course_didnot_signIn);
        course_not_found = (TextView) view.findViewById(R.id.course_not_found);
        context = getContext();
        user_id = Common.getUserName(getContext());
        fabBtn.setOnClickListener(this);

        if (!Common.checkUserName(getActivity(), user_id)) {
            course_didnot_signIn.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);
            fabBtn.setVisibility(View.GONE);
            course_not_found.setVisibility(View.GONE);
            return null;
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_btn_add:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in,
                        R.anim.left_out,
                        R.anim.left_in,
                        R.anim.right_out)
                        .replace(R.id.fragment_container, new AddCourseFragment())
                        .commit();
                ft.addToBackStack(null);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Common.getUserName(context) != null) {
            course_didnot_signIn.setVisibility(View.GONE);
            access = Common.getUserAccess(context, user_id);

            if (access == COACH_ACCESS) {
                fabBtn.setVisibility(View.VISIBLE);
                courses = findCourseByCoach("findCourseByCoach", user_id);
                if (courses.size() > 0) {
                    course_not_found.setVisibility(View.GONE);
                    pager.setVisibility(View.VISIBLE);
                    pager.setAdapter(new MyCourseAdapter(courses, getContext()));

                } else {
                    course_not_found.setVisibility(View.VISIBLE);
                    pager.setVisibility(View.GONE);
                }

            } else {
                fabBtn.setVisibility(View.GONE);
                courses = findCourseByStudent("findCourseByStudent", user_id);
                if (courses.size() > 0) {
                    course_not_found.setVisibility(View.GONE);
                    pager.setVisibility(View.VISIBLE);
                    pager.setAdapter(new MyCourseAdapter(courses, getContext()));

                } else {
                    course_not_found.setVisibility(View.VISIBLE);
                    pager.setVisibility(View.GONE);
                }
            }
        }
    }

    private ArrayList<Course> findCourseByCoach(String value, String user_id) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/finalCourseServlet";
            ArrayList<Course> courses = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", value);
            jsonObject.addProperty("user_id", user_id);
            String jsonOut = jsonObject.toString();
            courseGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Course>>() {
                }.getType();
                courses = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return courses;
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return null;
        }
    }

    private ArrayList<Course> findCourseByStudent(String value, String user_id) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/finalCourseServlet";
            ArrayList<Course> courses = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", value);
            jsonObject.addProperty("user_id", user_id);
            String jsonOut = jsonObject.toString();
            courseGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Course>>() {
                }.getType();
                courses = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return courses;
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (courseGetAllTask != null) {
            courseGetAllTask.cancel(true);
        }
    }
}


