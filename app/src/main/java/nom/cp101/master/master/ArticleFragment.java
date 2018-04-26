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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

//文章frag
public class ArticleFragment extends Fragment {
    RecyclerView rv;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().setTitle("Article");
        view = inflater.inflate(R.layout.article_frag, container, false);
        //取得RecyclerView並接上ArticleAdapter
        setRecyclerView();

        return view;
    }

    //取得RecyclerView並接上ArticleAdapter
    private void setRecyclerView() {
        rv = (RecyclerView) view.findViewById(R.id.rv_article);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
//        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//        layout.setReverseLayout(true);//列表翻转
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new ArticleAdapter(getActivity(), getFragmentManager()));
    }

}

