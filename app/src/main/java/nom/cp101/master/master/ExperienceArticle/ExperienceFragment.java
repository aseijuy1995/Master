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

//心得文章frag
public class ExperienceFragment extends Fragment {
    View view;
    RecyclerView rvExperience;
    SwipeRefreshLayout srlExperience;
    ExperienceAdapter experienceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.experience_frag, container, false);
        srlExperience = (SwipeRefreshLayout) view.findViewById(R.id.srlExperience);
        rvExperience = (RecyclerView) view.findViewById(R.id.rvExperience);

        setRecyclerView();
        //設置SwipeRefreshLayout,上滑更新課程文章
        setSwipeRefreshLayout();

        return view;
    }

    //設置SwipeRefreshLayout,上滑更新課程文章
    private void setSwipeRefreshLayout() {
        //設置下拉圈大小
        srlExperience.setSize(SwipeRefreshLayout.LARGE);
        //設置下拉圈顏色
        srlExperience.setColorSchemeResources(android.R.color.holo_orange_dark);

        srlExperience.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //抓取在recyclerView呈現的心得文章所有文章
                experienceAdapter.setData();
                //抓取在viewPager呈現的心得文章所有文章
                //使其getItemCount()從新呼叫
                experienceAdapter.notifyDataSetChanged();
                //移除SwipeRefreshLayout更新時的loading圖示
                srlExperience.setRefreshing(false);
            }
        });
    }

    //取得RecyclerView並接上ExperienceAdapter
    private void setRecyclerView() {
        rvExperience.setLayoutManager(new LinearLayoutManager(getActivity()));
        experienceAdapter = new ExperienceAdapter(getActivity(), getFragmentManager());
        //抓取在recyclerView呈現的心得文章所有文章
        experienceAdapter.setData();
        rvExperience.setAdapter(experienceAdapter);
    }
}

