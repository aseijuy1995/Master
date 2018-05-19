package nom.cp101.master.master.ExperienceArticle;

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

/**
 * Created by yujie on 2018/4/25.
 */

//心得文章frag
public class ExperienceArticleFragment extends Fragment {
    View view;
    RecyclerView rvExperienceArticle;
    SwipeRefreshLayout swipeRefreshLayout;
    ExperienceArticleAdapter experienceArticleAdapter=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.experience_article_frag, container, false);
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutExperienceArticle);
//        //取得RecyclerView並接上ExperienceAdapter
//        setRecyclerView();
//        //設置SwipeRefreshLayout,上滑更新課程文章
//        setSwipeRefreshLayout();

        return view;
    }
    //設置SwipeRefreshLayout,上滑更新課程文章
    private void setSwipeRefreshLayout() {
        //設置下拉圈大小
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //設置下拉圈顏色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //抓取在recyclerView呈現的心得文章所有文章
                experienceArticleAdapter.setData();
                //抓取在viewPager呈現的心得文章所有文章
                experienceArticleAdapter.setViewPagerData();
                //使其getItemCount()從新呼叫
                experienceArticleAdapter.notifyDataSetChanged();
                //移除SwipeRefreshLayout更新時的loading圖示
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    //取得RecyclerView並接上ExperienceAdapter
    private void setRecyclerView() {
        rvExperienceArticle = (RecyclerView) view.findViewById(R.id.rvExperienceArticle);
        rvExperienceArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        experienceArticleAdapter=new ExperienceArticleAdapter(getActivity(), getFragmentManager());
        //抓取在recyclerView呈現的心得文章所有文章
        experienceArticleAdapter.setData();
        rvExperienceArticle.setAdapter(experienceArticleAdapter);
    }
}

