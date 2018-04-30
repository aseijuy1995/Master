package nom.cp101.master.master.CourseArticleActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nom.cp101.master.master.CourseArticle.CourseArticleAllData;
import nom.cp101.master.master.CourseArticle.CourseArticleData;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/30.
 */

public class CourseArticleProjectFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.course_article_project_frag, container,false);
        String projectName=getArguments().getString("projectName");
        getActivity().setTitle(projectName);
        //給予專案項目名稱判斷所需帶入之相關課程的所有文章
        List<CourseArticleData> courseArticleDataList=CourseArticleAllData.takeCourseArticleProjectDataList(getContext(), projectName);
        //設定recyclerView內所需帶入的文章
        setArticleRecyclerView(getContext(), courseArticleDataList);

        return view;
    }

    private void setArticleRecyclerView(Context context, List<CourseArticleData> courseArticleDataList) {
        RecyclerView rvProject=view.findViewById(R.id.rvProject);
        rvProject.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProject.setAdapter(new CourseArticleProjectAdapter(context, courseArticleDataList));
    }

}
