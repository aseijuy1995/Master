package nom.cp101.master.master.Account.MyCourse.CLASS;

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

/**
 * Created by chunyili on 2018/4/17.
 */

public class InfiniteAdapter extends PagerAdapter {

    List<Course> listCourse;
    Context context;
    LayoutInflater layoutInflater;
    Activity activity;


    public InfiniteAdapter(List<Course> listCourse, Context context, Activity activity) {
        this.listCourse = listCourse;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.activity = activity;
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
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.account_course_item,container,false);

        final ImageView imageView = view.findViewById(R.id.imageView);
        final TextView course_name = view.findViewById(R.id.course_name);
        final TextView course_category = view.findViewById(R.id.course_category);
        final TextView course_date = view.findViewById(R.id.course_date);
        final TextView course_status = view.findViewById(R.id.course_status);
        final CardView cardView = view.findViewById(R.id.cardView);

        final Course course = listCourse.get(position);

        int imageSize = activity.getResources().getDisplayMetrics().widthPixels / 4;
        String url = Common.URL + "/photoServlet";
        int photo_id = course.getCourse_image_id();
        ImageTask spotImageTask = new ImageTask(url, photo_id, imageSize, imageView);
        spotImageTask.execute();


        Date date = course.getCourse_date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        course_name.setText(course.getCourse_name());
        course_date.setText(dateStr);
        container.addView(view);
        int status = course.getCourse_status_id();
        String statusStr;
        if(status == 1){
            statusStr = "報名中";
        }else{
            statusStr = "報名截止";
        }
        course_status.setText(statusStr);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",course);
                Fragment fragment = new MyCourseSingleFragment();
                fragment.setArguments(bundle);
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container,fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });


        return view;
    }



}
