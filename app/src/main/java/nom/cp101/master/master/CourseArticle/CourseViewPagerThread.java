package nom.cp101.master.master.CourseArticle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

public class CourseViewPagerThread extends Thread {
    //自訂常數給handler判斷msg的key值
    private static final int PAGE_ITEM = 0;
    private ViewPager vp;

    public CourseViewPagerThread(ViewPager vp) {
        this.vp = vp;
    }

    @Override
    public void run() {
        super.run();
        //每隔3秒執行一次,意味停留3s
        handler.postDelayed(this, 1000 * 3);
        //取得當前pageItem,透過handler的handleMessage讓ui thread做的事情傳回main thread
        //傳遞的值為vp.getCurrentItem(),為了以防使用者滑動pageg時,可依照當前pageItem繼續輪播
        //因目前不在main thread內,則不可對任何UI做對應處理
        Message msg = handler.obtainMessage(PAGE_ITEM, vp.getCurrentItem());
        //也可使用post,本質沒有太大差別,post較為簡單
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        //給予對應msg.what(類似於key)讓ui thread的結果傳回main thread
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //將傳遞來的pageItem+1,才可繼續輪播
                case PAGE_ITEM:
                    vp.setCurrentItem((int) msg.obj + 1);
                    break;
            }
        }
    };

}
