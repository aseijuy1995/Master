package nom.cp101.master.master.CourseArticle;

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

import nom.cp101.master.master.R;

//課程文章frag
public class CourseArticleFragment extends Fragment {
    RecyclerView rvCourseArticle;
    View view;
    CourseArticleAdapter courseArticleAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_article_frag, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutCourseArticle);
        rvCourseArticle = (RecyclerView) view.findViewById(R.id.rvCourseArticle);
        //取得RecyclerView並接上CourseArticleAdapter
        setRecyclerView();
        setSwipeRefreshLayout();

        swipeRefreshLayout.setVisibility(View.VISIBLE);//資料呈現
        return view;
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        //設置下拉圈大小
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //設置下拉圈顏色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                courseArticleAdapter.setData();
                //使其getItemCount()從新呼叫
                courseArticleAdapter.notifyDataSetChanged();
                //移除SwipeRefreshLayout更新時的loading圖示
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //取得RecyclerView並接上ArticleAdapter
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvCourseArticle.setLayoutManager(layoutManager);
        courseArticleAdapter = new CourseArticleAdapter(getActivity(), getFragmentManager());
        //自訂setData-method以便刷新給予數據
        courseArticleAdapter.setData();
        rvCourseArticle.setAdapter(courseArticleAdapter);
    }

}

