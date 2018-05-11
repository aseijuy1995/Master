package nom.cp101.master.master.ExperienceArticle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/5/8.
 */

public class ExperienceArticleViewPagerAdapter extends PagerAdapter {

    Context context;
    List<byte[]> list;

    public ExperienceArticleViewPagerAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
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
        //因要做輪播,則讓size到最後時會回到前一item,則須內做判斷position % list.size()

        Bitmap bitmap=BitmapFactory.decodeByteArray(list.get(position % list.size()), 0,list.get(position % list.size()).length);
        iv.setImageBitmap(bitmap);
//        iv.setImageResource(list.get(position % list.size()));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(iv);
        return iv;
    }

    //若滑動的page超過預載的樹糧食,則採用此method
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setData(){
        this.list=ExperienceArticleAllData.takeExperienceViewPagerDataList();
    }
}
