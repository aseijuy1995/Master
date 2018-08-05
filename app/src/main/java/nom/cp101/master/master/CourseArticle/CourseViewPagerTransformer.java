package nom.cp101.master.master.CourseArticle;

import android.support.v4.view.ViewPager;
import android.view.View;

//設置ViewPager動畫呈現
public class CourseViewPagerTransformer implements ViewPager.PageTransformer {
    private static final float min_scale = 0.85f;

    @Override
    public void transformPage(View page, float position) {
        float scaleFactor = Math.max(min_scale, 1 - Math.abs(position));

        if (position < -1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else if (position < 0) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else if (position >= 0 && position < 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else if (position >= 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }
    }
}
