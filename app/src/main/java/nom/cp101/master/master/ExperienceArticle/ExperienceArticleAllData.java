package nom.cp101.master.master.ExperienceArticle;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/25.
 */

//此類別為心得文章需呼叫的各method
public class ExperienceArticleAllData {

    static List<ImageView> experienceArticleViewPagerDataList;
    static List<ExperienceArticleData> experienceArticleTextDataList;
    static ExperienceArticleData experienceArticleImgData;

    //db存著置在ExperienceViewPager內的ExperienceViewPagerData陣列
    public static List<ImageView> takeExperienceViewPagerDataList(Context context) {
        experienceArticleViewPagerDataList = new ArrayList<ImageView>();

//        experienceArticleViewPagerDataList.add(new ExperienceArticleViewPagerData(R.drawable.a1));
//        experienceArticleViewPagerDataList.add(new ExperienceArticleViewPagerData(R.drawable.a2));
//        experienceArticleViewPagerDataList.add(new ExperienceArticleViewPagerData(R.drawable.a3));
//        experienceArticleViewPagerDataList.add(new ExperienceArticleViewPagerData(R.drawable.a1));
//        experienceArticleViewPagerDataList.add(new ExperienceArticleViewPagerData(R.drawable.a2));
//        experienceArticleViewPagerDataList.add(new ExperienceArticleViewPagerData(R.drawable.a3));

        int[] imgs={R.drawable.a1, R.drawable.a2, R.drawable.a3};
        for (int i = 0; i < imgs.length; i++) {

            ImageView iv=new ImageView(context);
            iv.setImageResource(imgs[i]);
            experienceArticleViewPagerDataList.add(iv);
        }


        return experienceArticleViewPagerDataList;
    }


    //db將ExperienceArticleData的物件包裝成一個ExperienceArticleDataList內的陣列便於呼叫
    //回傳list為顯示於心得文章中
    public static final List<ExperienceArticleData> takeExperienceArticleDataList() {

//        experienceArticleDataList = new LinkedList();
//
//        experienceArticleDataList.add(new ExperienceArticleData(0,0,"111","1111",111,"111","111"));
//        experienceArticleDataList.add(new ExperienceArticleData(0,0,"111","1111",111,"111","111"));
//        experienceArticleDataList.add(new ExperienceArticleData(0,0,"111","1111",111,"111","111"));
//        experienceArticleDataList.add(new ExperienceArticleData(0,0,"111","1111",111,"111","111"));


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticleText");

        ExperienceArticleTask experienceArticleTask = new ExperienceArticleTask(jsonObject.toString());

        try {

            experienceArticleTextDataList = experienceArticleTask.execute(Common.URL + "/ExperienceArticleServlet").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return experienceArticleTextDataList;
    }


    public static final void takeExperienceArticleImgData(int id, ImageView imageView, int imgSize, int i) {
        Bitmap bitmap = null;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "getExperienceArticleImg");
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("imgSize", imgSize);
        jsonObject.addProperty("i", i);
        ExperienceArticleImgTask experienceArticleTask = new ExperienceArticleImgTask(jsonObject.toString(), imageView, imgSize, i);

        try {
            experienceArticleTask.execute(Common.URL + "/ExperienceArticleServlet").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
//            imageView.setImageBitmap(bitmap);
        }

    }
}
