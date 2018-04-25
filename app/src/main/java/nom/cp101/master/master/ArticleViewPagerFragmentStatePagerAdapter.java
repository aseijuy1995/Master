package nom.cp101.master.master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yujie on 2018/4/20.
 */

//文章列表,position=0
public class ArticleViewPagerFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    //文章列表內,置入ArticleImg的圖片數據
    List<ArticleImg> articleImgList;

    public ArticleViewPagerFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        //傳進viewPager所需圖片陣列
        articleImgList=getProjectData();
    }

    @Override
    public Fragment getItem(int position) {
        //取得當前顯示的ArticleImg內存放的數據
        ArticleImg articleImg=articleImgList.get(position);
        ArticleViewPagerFragment articleViewPagerFragment=new ArticleViewPagerFragment();
        //使用bundle傳進數據進articleViewPagerFrag內
        Bundle bundle=new Bundle();
        bundle.putInt("articleImg", articleImg.getImg());
        articleViewPagerFragment.setArguments(bundle);

        return articleViewPagerFragment;
    }

    //viewPager筆數
    @Override
    public int getCount() {
        return articleImgList.size();
    }


    //取得db存放之圖片
    private List<ArticleImg> getProjectData() {
        articleImgList=new ArrayList<>();

        articleImgList.add(new ArticleImg(R.drawable.ic_launcher_background));
        articleImgList.add(new ArticleImg(R.drawable.ic_launcher_foreground));
        articleImgList.add(new ArticleImg(R.drawable.ic_launcher_background));
        articleImgList.add(new ArticleImg(R.drawable.ic_launcher_foreground));
        articleImgList.add(new ArticleImg(R.drawable.ic_launcher_background));
        articleImgList.add(new ArticleImg(R.drawable.ic_launcher_foreground));

        return articleImgList;
    }
}
