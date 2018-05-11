package nom.cp101.master.master.CourseArticleActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nom.cp101.master.master.CourseArticle.CourseArticleAllData;
import nom.cp101.master.master.R;

//課程類別的frag
public class CourseArticleCategoryFragment extends Fragment {
    View view;
    List<CourseArticleCategoryData> courseArticleCategoryDataList;
    RecyclerView rvCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //將R.layout.course_article_category_frag.xml轉為view
        view = inflater.inflate(R.layout.course_article_category_frag, container, false);
        //從CourseArticleActivity取得點擊的類別名稱
        String categoryName = getArguments().getString("categoryName");
        //將取得的類別名稱呼叫takeCourseArticleCategoryDataList給予比對,回傳List<CourseArticleCategoryData>存於courseArticleCategoryDataList
        courseArticleCategoryDataList = CourseArticleAllData.takeCourseArticleCategoryDataList(getContext(), categoryName);
        //設置課程類別的recyclerView
        setRecyclerView();

        return view;
    }

    //設置課程類別的recyclerView
    private void setRecyclerView() {
        rvCategory = (RecyclerView) view.findViewById(R.id.rvCategory);
        //此使用GridLayoutManager,其改變列數
        rvCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //帶入指定類別內之項目List,以便塞入
        rvCategory.setAdapter(new CourseArticleCategoryAdapter(getContext(), getFragmentManager(), courseArticleCategoryDataList));
    }
}
