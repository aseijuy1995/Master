package nom.cp101.master.master.Account.MyCourse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ImageTask;
import nom.cp101.master.master.R;

public class MyCourseAdapter extends PagerAdapter {
    private List<Course> listCourse;
    private Context context;
    private LayoutInflater layoutInflater;


    public MyCourseAdapter(List<Course> listCourse, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.listCourse = listCourse;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listCourse.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.account_course_item, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView course_name = view.findViewById(R.id.course_name);
        TextView course_date = view.findViewById(R.id.course_date);
        TextView course_status = view.findViewById(R.id.course_status);
        CardView cardView = view.findViewById(R.id.cardView);

        final Course course = listCourse.get(position);
        Date date = course.getCourse_date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        int imageSize = ((Activity) context).getResources().getDisplayMetrics().widthPixels / 4;
        String url = Common.URL + "/photoServlet";
        int photo_id = course.getCourse_image_id();
        ImageTask spotImageTask = new ImageTask(url, photo_id, imageSize, imageView);
        spotImageTask.execute();

        course_name.setText(course.getCourse_name());
        course_date.setText(dateStr);
        container.addView(view);
        String statusStr = "";
        if (course.getCourse_apply_deadline().getTime() > System.currentTimeMillis()) {
            statusStr = context.getResources().getString(R.string.register);
        } else {
            statusStr = context.getResources().getString(R.string.un_register);
        }
        course_status.setText(statusStr);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", course);
                Fragment fragment = new SingleCourseFragment();
                fragment.setArguments(bundle);
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.right_in,
                        R.anim.left_out,
                        R.anim.left_in,
                        R.anim.right_out)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                ft.addToBackStack(null);
            }
        });
        return view;
    }
}
