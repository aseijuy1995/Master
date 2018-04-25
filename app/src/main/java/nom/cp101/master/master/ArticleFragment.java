package nom.cp101.master.master;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

//文章frag
public class ArticleFragment extends Fragment {
    RecyclerView rv;
    View view;
    List<ProjectData> projectList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.article_frag, container, false);
        //取得RecyclerView並接上ArticleAdapter
        setRecyclerView();

        return view;
    }



    //取得RecyclerView並接上ArticleAdapter
    private void setRecyclerView() {
        rv = (RecyclerView) view.findViewById(R.id.rv_article);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new ArticleAdapter(getActivity(), getFragmentManager(), getProjectData()));
    }

    //載入專業類別的數據
    private List<ProjectData> getProjectData() {
        projectList=new ArrayList<>();

        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "1"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "2"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "3"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "4"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "5"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "6"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "7"));
        projectList.add(new ProjectData(android.R.drawable.sym_action_email, "8"));

        return projectList;
    }
}

