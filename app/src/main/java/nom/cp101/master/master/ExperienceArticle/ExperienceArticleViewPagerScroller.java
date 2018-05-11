package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by yujie on 2018/5/10.
 */
//設置viewPager輪播數度
public class ExperienceArticleViewPagerScroller extends Scroller {
    //預設
    private int scrollerDuration=2000;

    public void setScrollerDuration(int duration){
        this.scrollerDuration=duration;
    }

    public ExperienceArticleViewPagerScroller(Context context) {
        super(context);
    }

    //將時間套用自定義
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, scrollerDuration);
    }

    //自訂套用的view
    public void initViewPagerScroll(ViewPager viewPager) {

        Field mScroller = null;
        try {
            //getDeclaredField(name)會回傳一個field的物件,裡面裝關於此類別的所有相關屬性
            //NoSuchFieldException表沒有對應字串相符
            //呼叫getDeclaredField(name)會觸發NoSuchFieldException
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            //加上setAccessible(true)及對相關final也有訪問權限
            mScroller.setAccessible(true);
            //將指定對象參數上,此Field對象表示的字段設置為指定的新值,如果基礎字段具有原始類型,則新值將自動解包
            mScroller.set(viewPager, this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }



}

