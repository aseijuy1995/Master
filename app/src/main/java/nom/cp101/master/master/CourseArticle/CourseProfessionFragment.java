package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.angmarch.views.NiceSpinner;

import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.R;

//顯示指定類別的各項目frag
public class CourseProfessionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;
    private NiceSpinner niceSpinner;
    private TextView tvProfessionCourse;
    private RecyclerView rvProfessionCourse;
    private Profession profession;
    private CourseNiceSpinnerAdapter courseNiceSpinnerAdapter;
    private CourseProfessionItemAdapter courseProfessionItemAdapter;
    private LinearLayoutManager llmProfession;

    private Context context;
    private List<Course> courseList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_profession_frag, container, false);
        findViews(view);
        context = getContext();

        if (getArguments() != null) {
            //取得專業項目名稱
            profession = (Profession) getArguments().getSerializable("profession");
            courseNiceSpinnerAdapter = new CourseNiceSpinnerAdapter(context, profession.getProfession_item());
            niceSpinner.setAdapter(courseNiceSpinnerAdapter);
            //預設
            niceSpinner.setOnItemSelectedListener(this);
            initRecyclerView();
        }
        return view;
    }

    private void findViews(View view) {
        niceSpinner = (NiceSpinner) view.findViewById(R.id.nsProfessionCourse);
        tvProfessionCourse = (TextView) view.findViewById(R.id.tvProfessionCourse);
        rvProfessionCourse = (RecyclerView) view.findViewById(R.id.rvProfessionCourse);
    }

    //設置課程類別的recyclerView
    private void initRecyclerView() {
        llmProfession = new LinearLayoutManager(context);
        rvProfessionCourse.setLayoutManager(llmProfession);
        rvProfessionCourse.setItemAnimator(new DefaultItemAnimator());
        initData(0);
    }

    private void initData(int position) {
        String professionItem = profession.getProfession_item().get(position);
        courseList = ConnectionServer.getCourseByProfessionItem(professionItem);

        if (courseList.size() > 0) {
            tvProfessionCourse.setVisibility(View.GONE);
            rvProfessionCourse.setVisibility(View.VISIBLE);
            courseProfessionItemAdapter = new CourseProfessionItemAdapter(context, courseList);
            rvProfessionCourse.setAdapter(courseProfessionItemAdapter);

        } else {
            tvProfessionCourse.setVisibility(View.VISIBLE);
            rvProfessionCourse.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.nsProfessionCourse:
                initData(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
