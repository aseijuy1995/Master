package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by yujie on 2018/5/16.
 */
//改寫gridview用於首頁
public class CourseGridView extends GridView {

    public CourseGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //设置上下不滚动
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //true:禁止滚动
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

