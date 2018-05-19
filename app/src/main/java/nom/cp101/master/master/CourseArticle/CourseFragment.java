package nom.cp101.master.master.CourseArticle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nom.cp101.master.master.R;

//課程文章frag
public class CourseFragment extends Fragment {
    SwipeRefreshLayout srlCourse;
    ViewPager vpCourse;
    CourseGridView gvCourse;

    CourseProfessionAdapter courseProfessionAdapter;
    CourseViewPagerAdapter courseViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_frag, container, false);
        srlCourse = (SwipeRefreshLayout) view.findViewById(R.id.srlCourse);
        vpCourse = (ViewPager) view.findViewById(R.id.vpCourse);
        gvCourse = (CourseGridView) view.findViewById(R.id.gvCourse);

        setViewPager();
        setGridView();
        setSwipeRefreshLayout();

        return view;
    }


    //設置相關其viewPager
    private void setViewPager() {
        //設置緩存數量,參數不列入當前預載數量範圍
        vpCourse.setOffscreenPageLimit(3);
        //取得屏幕當前寬高度,對比出viewPager想呈現的大小
        int pagerWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 4.0f / 5.0f);
        int pagerHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels / 3.0f);
        //取得viewPager相關佈局訊息
        ViewGroup.LayoutParams layoutParams = vpCourse.getLayoutParams();
        //當layoutParams為空,將取得欲設置之寬高度導入viewPager,否則設置寬度即可
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(pagerWidth, pagerHeight);
        } else {
            layoutParams.width = pagerWidth;
            layoutParams.height = pagerHeight;
        }
        //設置各pager間距
        vpCourse.setPageMargin(5);
        //提供pager轉頁時的動畫
        vpCourse.setPageTransformer(true, new CourseViewPagerTransformer());
        courseViewPagerAdapter = new CourseViewPagerAdapter(getActivity());
        courseViewPagerAdapter.setData();
        vpCourse.setAdapter(courseViewPagerAdapter);

        CourseViewPagerScroller courseViewPagerScroller = new CourseViewPagerScroller(getActivity());
        courseViewPagerScroller.setScrollerDuration(3300);
        courseViewPagerScroller.initViewPagerScroll(vpCourse);

        //另外開執行緒做page切換
        new CourseViewPagerThread(vpCourse).start();
    }


    //設置相關其gridView
    private void setGridView() {
        courseProfessionAdapter = new CourseProfessionAdapter(getActivity(), getFragmentManager());
        courseProfessionAdapter.setData();
        gvCourse.setAdapter(courseProfessionAdapter);
    }


    //設置SwipeRefreshLayout更新數據
    private void setSwipeRefreshLayout() {
        //設置下拉圈大小
        srlCourse.setSize(SwipeRefreshLayout.LARGE);
        //設置下拉圈顏色
        srlCourse.setColorSchemeResources(android.R.color.holo_blue_dark);

        srlCourse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                courseViewPagerAdapter.setData();
                courseViewPagerAdapter.notifyDataSetChanged();


                courseProfessionAdapter.setData();
                courseProfessionAdapter.notifyDataSetChanged();
                //移除SwipeRefreshLayout更新時的loading圖示
                srlCourse.setRefreshing(false);
            }
        });
    }

}

