package nom.cp101.master.master.Account.MyCourse.CLASS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Date;
import java.util.List;

import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/4/26.
 */

public class MyCourseStudentManageFragment extends Fragment {
    private RecyclerView recyclerView;
    TextView studentCourseTitle, studentCourseApplied,tvStudentStatus;
    Date date;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_course_student_management_frag,container,false);
        Bundle bundle = getArguments();
        List<Apply> applies = (List<Apply>) bundle.getSerializable("applies");
        String course_name = bundle.getString("course_name");

        recyclerView = view.findViewById(R.id.studentRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        studentCourseTitle = view.findViewById(R.id.student_course_name);
        studentCourseTitle.setText(course_name);
        studentCourseApplied = view.findViewById(R.id.student_course_apply);

        studentCourseApplied.setText("總共 "+applies.size()+" 人");
        recyclerView.setAdapter(new MyCourseStudentManageAdapter(applies,getContext(),getActivity(),inflater,view));
        return view;
    }



}
