package nom.cp101.master.master.CourseArticleActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/30.
 */

public class CourseProfessionItemFragment extends Fragment {
    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    CourseProfessionItemAdapter courseProfessionItemAdapter;

    List<Course> courseList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_article_project_frag, container, false);
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutProject);
//
//        String projectName = getArguments().getString("projectName");
//        getActivity().setTitle(projectName);
//        //給予專案項目名稱判斷所需帶入之相關課程的所有文章
//        courseList = ConnectionServer.getCourseData();
//        //設定recyclerView內所需帶入的文章
//        setArticleRecyclerView(getContext());
//        //設置SwipeRefreshLayout,上滑更新課程文章
//        setSwipeRefreshLayout();

        return view;
    }

    //設置SwipeRefreshLayout,上滑更新課程文章
    private void setSwipeRefreshLayout() {
        //設置下拉圈大小
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //設置下拉圈顏色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                courseProfessionItemAdapter.setData(courseList);
                //使其getItemCount()從新呼叫
                courseProfessionItemAdapter.notifyDataSetChanged();
                //移除SwipeRefreshLayout更新時的loading圖示
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //設定recyclerView內所需帶入的文章
    private void setArticleRecyclerView(Context context) {
        RecyclerView rvProject = view.findViewById(R.id.rvProject);
        rvProject.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseProfessionItemAdapter = new CourseProfessionItemAdapter(context);
        courseProfessionItemAdapter.setData(courseList);
        rvProject.setAdapter(courseProfessionItemAdapter);
    }

}
