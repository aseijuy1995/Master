package nom.cp101.master.master.CourseArticleActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.angmarch.views.NiceSpinner;

import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.CourseArticle.ConnectionServer;

import nom.cp101.master.master.CourseArticle.CourseNiceSpinnerAdapter;
import nom.cp101.master.master.CourseArticle.ProfessionData;
import nom.cp101.master.master.R;

//顯示指定類別的各項目frag
public class CourseProfessionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
    NiceSpinner niceSpinner;
    RecyclerView rvProfessionCourse;
    ProfessionData professionData;

    List<Course> courseList = null;
    CourseProfessionItemAdapter courseProfessionItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_profession_frag, container, false);

        niceSpinner = (NiceSpinner) view.findViewById(R.id.nsProfessionCourse);
        rvProfessionCourse = (RecyclerView) view.findViewById(R.id.rvProfessionCourse);

        if (getArguments() != null) {
            //取得專業項目名稱
            professionData = (ProfessionData) getArguments().getSerializable("professionData");
            niceSpinner.setAdapter(new CourseNiceSpinnerAdapter(getActivity(), professionData.getProfession_item()));
            //預設
            String professionItem = professionData.getProfession_item().get(0);
            courseList = ConnectionServer.getCourseData(professionItem);
            setRecyclerView();

            niceSpinner.setOnItemSelectedListener(this);
        }
        return view;
    }

    //設置課程類別的recyclerView
    private void setRecyclerView() {
        rvProfessionCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseProfessionItemAdapter = new CourseProfessionItemAdapter(getActivity());
        courseProfessionItemAdapter.setData(courseList);
        rvProfessionCourse.setAdapter(courseProfessionItemAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.nsProfessionCourse:
                String professionItem = professionData.getProfession_item().get(position);
                courseList = ConnectionServer.getCourseData(professionItem);
                courseProfessionItemAdapter.setData(courseList);
                courseProfessionItemAdapter.notifyDataSetChanged();
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
