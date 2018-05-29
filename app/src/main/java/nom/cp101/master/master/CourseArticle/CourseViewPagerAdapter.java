package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;

public class CourseViewPagerAdapter extends PagerAdapter {
    Context context;
    List<Course> courseList = null;

    public CourseViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (courseList != null)
            return Integer.MAX_VALUE;
        else
            return 0;
    }

    //判斷instantiateItem返回的key是否與當前試圖為同一
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //創建欲顯示之視圖,如嵌入的view有預先加載則會在此實作
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = new ImageView(context);
        Bitmap bitmap = null;
        //因要做輪播,則讓size到最後時會回到前一item,則須內做判斷position % list.size()
        if (courseList.size() != 0) {
            bitmap = ConnectionServer.getPhotoImg(courseList, position, (int) (context.getResources().getDisplayMetrics().widthPixels / 5.0f));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageBitmap(bitmap);


            container.addView(iv);
            return iv;
        }
        return null;
    }

    //若滑動的page超過預載的樹糧食,則採用此method
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setData() {
        this.courseList = ConnectionServer.getCourseDatas();
    }
}
