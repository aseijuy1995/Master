package nom.cp101.master.master.CourseArticle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Master.MasterActivity;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Master.MasterActivity.SEND_COURSE;

public class CourseViewPagerAdapter extends PagerAdapter {
    private final String TAG = "CourseViewPagerAdapter";
    private Context context;
    private List<Course> courseList = null;
    private LruCache<String, Bitmap> lruCache;

    public CourseViewPagerAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
        initMemoryCache();
    }

    //LruCache機制
    private void initMemoryCache() {
        //緩存記憶體空間
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        //override存於緩存中的圖片大小
        lruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
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
        if (courseList.size() > 0) {
            Course course = null;

            //position = 0, courseList.size() = 1, position % courseList.size() = 0 --單圖輪播

            /*
            position = 0, courseList.size() = 2, position % courseList.size() = 0
            position = 1, courseList.size() = 2, position % courseList.size() = 1
            position = 2, courseList.size() = 2, position % courseList.size() = 0
            position = 3, courseList.size() = 2, position % courseList.size() = 1 --多圖輪播
             */

            /*
            position = 0, courseList.size() = 3, position % courseList.size() = 0
            position = 1, courseList.size() = 3, position % courseList.size() = 1
            position = 2, courseList.size() = 3, position % courseList.size() = 2
            position = 3, courseList.size() = 3, position % courseList.size() = 0
            position = 4, courseList.size() = 3, position % courseList.size() = 1 --多圖輪播
            */

            position = position % courseList.size();
            if (position < 0) {
                position = courseList.size() + position;
            }
            course = courseList.get(position);

            ImageView iv = new ImageView(context);
            Bitmap bitmap = null;

            bitmap = lruCache.get(String.valueOf(course.getCourse_image_id()));

            if (bitmap != null) {
                iv.setImageBitmap(bitmap);
                Log.d(TAG, "load from cache");

            } else {
                bitmap = ConnectionServer.getPhotoByPhotoId(String.valueOf(course.getCourse_image_id()));
                if (bitmap != null) {
                    lruCache.put(String.valueOf(course.getCourse_image_id()), bitmap);
                    iv.setImageBitmap(bitmap);
                    Log.d(TAG, "load from server");
                }
            }
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);

            final Course finalCourse = course;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Common.checkUserName(context, Common.getUserName(context))) {
                        Intent intent = new Intent(context, MasterActivity.class);
                        intent.putExtra(SEND_COURSE, SEND_COURSE);
                        intent.putExtra("course", finalCourse);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    }
                }
            });
            return iv;
        }
        return null;
    }

    //若滑動的page超過預載的數量時,則採用此method
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
