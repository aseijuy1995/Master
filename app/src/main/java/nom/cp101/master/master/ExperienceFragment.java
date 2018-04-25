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

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceFragment extends Fragment {
    RecyclerView rv;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().setTitle("Experience");
        view = inflater.inflate(R.layout.experience_frag, container, false);
        //取得RecyclerView並接上ExperienceAdapter
        setRecyclerView();

        return view;
    }


    //取得RecyclerView並接上ExperienceAdapter
    private void setRecyclerView() {
        rv = (RecyclerView) view.findViewById(R.id.rv_experience);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new ExperienceAdapter(getActivity(), getFragmentManager()));
    }
}

