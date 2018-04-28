package nom.cp101.master.master.CourseArticle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nom.cp101.master.master.R;

//課程文章frag
public class CourseArticleFragment extends Fragment {
    RecyclerView rv;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_article_frag, container, false);
        //取得RecyclerView並接上ArticleAdapter
        setRecyclerView();

        return view;
    }

    //取得RecyclerView並接上ArticleAdapter
    private void setRecyclerView() {
        rv = (RecyclerView) view.findViewById(R.id.rvCourseArticle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new CourseArticleAdapter(getActivity(), getFragmentManager()));
    }

}

