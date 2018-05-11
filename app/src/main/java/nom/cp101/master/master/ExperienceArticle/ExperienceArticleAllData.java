package nom.cp101.master.master.ExperienceArticle;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/25.
 */

//此類別為心得文章需呼叫的各method
public class ExperienceArticleAllData {


    //ˇ請求server端取得db內所有心得文章之照片
    public static List<byte[]> takeExperienceViewPagerDataList() {
        List<byte[]> experienceArticleViewPagerDataList = null;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticlePager");

        ExperienceArticleTask experienceArticleTask = new ExperienceArticleTask(jsonObject.toString());
        String jsonStr="";
        Gson gson=new Gson();

        try {
            jsonStr=experienceArticleTask.execute(Common.URL+ "/ExperienceArticleServlet").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        experienceArticleViewPagerDataList=gson.fromJson(jsonStr.toString(),new TypeToken<List<byte[]>>(){}.getType());

        return experienceArticleViewPagerDataList;
    }


    //ˇ請求server端取得db內所有心得文章之文字部分內容
    public static final List<ExperienceArticleData> takeExperienceArticleDataList() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticleText");
        ExperienceArticleTask experienceArticleTask = new ExperienceArticleTask(jsonObject.toString());
        String jsonStr = "";

        Gson gson = new Gson();
        List<ExperienceArticleData> experienceArticleDataList = null;

        try {

            jsonStr = experienceArticleTask.execute(Common.URL + "/ExperienceArticleServlet").get();

            experienceArticleDataList = gson.fromJson(jsonStr.toString(), new TypeToken<List<ExperienceArticleData>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return experienceArticleDataList;
    }

    //ˇ請求server端取得db內所有心得文章之頭貼與文章圖片部分內容
    public static final void takeExperienceArticleImgData(int postId, ImageView imageView, int imgSize, int i) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticleImg");
        jsonObject.addProperty("id", postId);
        jsonObject.addProperty("imgSize", imgSize);
        jsonObject.addProperty("i", i);
        ExperienceArticleImgTask experienceArticleTask = new ExperienceArticleImgTask(jsonObject.toString(), imageView, imgSize, i);

        try {
            experienceArticleTask.execute(Common.URL + "/ExperienceArticleServlet").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //ˇ請求server端取得db內使用者對心得文章之按讚動作
    public static boolean takeExperienceArticlePostLikeCheck(String user_id, int post_id) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("experienceArticle","experienceArticlePostLikeCheck");
        jsonObject.addProperty("experienceArticlePostLikeUserId",user_id);
        jsonObject.addProperty("experienceArticlePostLikePostId",post_id);

        ExperienceArticleTask experienceArticleTask=new ExperienceArticleTask(jsonObject.toString());
        String jsonStr="";
        Gson gson=new Gson();
        boolean postListCheck=false;

        try {
            jsonStr=experienceArticleTask.execute(Common.URL+"/ExperienceArticleServlet").get();

            postListCheck=gson.fromJson(jsonStr.toString(), Boolean.class);

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return postListCheck;
    }

    //ˇ請求server端取得db內各心得文章之讚數
    public static int takeExperienceArticlePostLisk(int post_id) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticlePostLike");
        jsonObject.addProperty("experienceArticlePostLike", post_id);

        ExperienceArticleTask experienceArticleTask=new ExperienceArticleTask(jsonObject.toString());
        String jsonStr="";
        Gson gson=new Gson();
        int postLike=0;

        try {
            jsonStr=experienceArticleTask.execute(Common.URL+"/ExperienceArticleServlet").get();

            postLike=gson.fromJson(jsonStr.toString(),Integer.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return postLike;
    }

    //ˇ請求server端送出對當前操作之文章讚數動作給予相對之讚數變化回應
    public static int takeExperienceArticlePostLikeRefresh(String user_id, int postId, boolean isChecked) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticlePostLikeRefresh");
        jsonObject.addProperty("experienceArticlePostLikeRefreshUserId", user_id);
        jsonObject.addProperty("experienceArticlePostLikeRefreshPostId", postId);
        jsonObject.addProperty("experienceArticlePostLikeRefreshChecked", isChecked);

        ExperienceArticleTask experienceArticleTask=new ExperienceArticleTask(jsonObject.toString());
        String jsonStr="";
        Gson gson=new Gson();
        int postLikes=0;

        try {
            jsonStr=experienceArticleTask.execute(Common.URL+"/ExperienceArticleServlet").get();

            postLikes=gson.fromJson(jsonStr.toString() ,Integer.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return postLikes;
    }

    //ˇ請求server端送出對應文章id回傳關於文章所有數據
    public static ExperienceArticleData takeExperienceArticlePostData(String userId, int postId) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceArticlePostData");
        jsonObject.addProperty("experienceArticlePostUserId", userId);
        jsonObject.addProperty("experienceArticlePostId", postId);

        ExperienceArticleTask experienceArticleTask=new ExperienceArticleTask(jsonObject.toString());
        String jsonStr="";
        Gson gson=new Gson();
        ExperienceArticleData experienceArticleData=null;

        try {
            jsonStr=experienceArticleTask.execute(Common.URL+"/ExperienceArticleServlet").get();

            experienceArticleData=gson.fromJson(jsonStr.toString(), ExperienceArticleData.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return experienceArticleData;
    }
}
