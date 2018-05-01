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
    CourseArticleAdapter adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_article_frag, container, false);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayoutCourseArticle);
        //取得RecyclerView並接上CourseArticleAdapter
        setRecyclerView();
        setSwipeRefreshLayout();

        return view;
    }

    private void setSwipeRefreshLayout() {


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                adapter.notifyDataSetChanged();//更新RecyclerView


//                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                rvCourseArticle.setLayoutManager(layoutManager);
//                adapter=new CourseArticleAdapter(getActivity(), getFragmentManager());
//                rvCourseArticle.setAdapter(adapter);
//                swipeRefreshLayout.setVisibility(View.VISIBLE);//資料呈現

                setRecyclerView();
                swipeRefreshLayout.setRefreshing(false);//移除SwipeRefreshLayout更新時的loading圖示


            }
        });
    }

    //取得RecyclerView並接上ArticleAdapter
    private void setRecyclerView() {
        rvCourseArticle = (RecyclerView) view.findViewById(R.id.rvCourseArticle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvCourseArticle.setLayoutManager(layoutManager);
        adapter=new CourseArticleAdapter(getActivity(), getFragmentManager());
        rvCourseArticle.setAdapter(adapter);
        swipeRefreshLayout.setVisibility(View.VISIBLE);//資料呈現
    }

}

