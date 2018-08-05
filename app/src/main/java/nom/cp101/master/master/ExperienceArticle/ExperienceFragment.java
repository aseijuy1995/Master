package nom.cp101.master.master.ExperienceArticle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

//心得文章frag
public class ExperienceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View view;
    private SwipeRefreshLayout srlExperience;
    private RecyclerView rvExperience;
    private LinearLayoutManager llmExperience;
    private ExperienceAdapter experienceAdapter;
    private List<Experience> experienceList;
    private ImageView ivAdd;
    private TextView tvEmpty;

    private int lastVisibleItemPosition = 0;
    private final int PAGE_COUNT = 5;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.experience_frag, container, false);
        findViews(view);
        ivAdd.setOnClickListener(this);
        return view;
    }

    private void findViews(View view) {
        ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        srlExperience = (SwipeRefreshLayout) view.findViewById(R.id.srlExperience);
        rvExperience = (RecyclerView) view.findViewById(R.id.rvExperience);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
    }

    @Override
    public void onResume() {
        super.onResume();
        rvExperience.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);

        experienceList = ConnectionServer.getExperiences(Common.getUserName(getContext()));
        if (experienceList.size() > 0) {
            rvExperience.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            initRecyclerView();
        }
        //設置SwipeRefreshLayout,上滑更新課程文章
        initSwipeRefreshLayout();
    }

    //取得RecyclerView並接上ExperienceAdapter
    private void initRecyclerView() {
        experienceAdapter = new ExperienceAdapter(getActivity(),
                getFragmentManager(),
                loadExperienceDatas(0, PAGE_COUNT),
                loadExperienceDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        llmExperience = new LinearLayoutManager(getActivity());
        rvExperience.setLayoutManager(llmExperience);
        rvExperience.setAdapter(experienceAdapter);
        rvExperience.setItemAnimator(new DefaultItemAnimator());
        rvExperience.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //若滑已置底
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //若加載條沒有隱藏&&最後position的位置為總數量-1
                    //position位置為總數量-1!!
                    if (experienceAdapter.isHiddenHint() == false && lastVisibleItemPosition + 1 == experienceAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(experienceAdapter.getLastPosition(), experienceAdapter.getLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    //若加載條隱藏&&最後position的位置為總數量-2,因加載條被隱藏,表示與顯示數量少1
                    if (experienceAdapter.isHiddenHint() == true && lastVisibleItemPosition + 2 == experienceAdapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView(experienceAdapter.getLastPosition(), experienceAdapter.getLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //取得最後position
                lastVisibleItemPosition = llmExperience.findLastVisibleItemPosition();
            }
        });
    }

    //自訂method,分頁加載
    private List<Experience> loadExperienceDatas(final int firstIndex, final int lastIndex) {
        List<Experience> loadList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < experienceList.size()) {
                loadList.add(experienceList.get(i));
            }
        }
        return loadList;
    }

    //自訂method,加載數據
    private void updateRecyclerView(int fromIndex, int toIndex) {
        // 获取从fromIndex到toIndex的数据
        List<Experience> updateList = loadExperienceDatas(fromIndex, toIndex);
        if (updateList.size() > 0) {
            experienceAdapter.updateList(updateList, true);

        } else {
            experienceAdapter.updateList(null, false);
        }
    }

    //設置SwipeRefreshLayout,上滑更新課程文章
    private void initSwipeRefreshLayout() {
        srlExperience.setSize(SwipeRefreshLayout.LARGE);
        srlExperience.setColorSchemeResources(android.R.color.holo_blue_dark);
        srlExperience.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        srlExperience.setRefreshing(true);

        if (this.experienceList.size() > 0) {
            rvExperience.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            //依據當前experience的比數判斷是否做刷新
            if (this.experienceList.size() == ConnectionServer.getExperiences(Common.getUserName(getContext())).size()) {
                experienceAdapter.notifyDataSetChanged();

            } else {
                experienceAdapter.resetDatas();
                experienceList = ConnectionServer.getExperiences(Common.getUserName(getContext()));
                updateRecyclerView(0, PAGE_COUNT);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    srlExperience.setRefreshing(false);
                }
            }, 500);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAdd:
                if (Common.checkUserName(getContext(), Common.getUserName(getContext()))) {
                    Intent intent = new Intent(getContext(), ExperienceAppendActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}

