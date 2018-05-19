package nom.cp101.master.master.CourseArticle;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;

import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.ImageTask;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/5/16.
 */

public class ConnectionServer {

    public static final int[] profession_img = {
            R.drawable.swim,
            R.drawable.skate,
            R.drawable.workout,
            R.drawable.ball,
            R.drawable.music,
            R.drawable.language,
            R.drawable.paint,
            R.drawable.code
    };

    //server抓course數據
    public static List<Course> getCourseDatas() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "courseDatas");
        MyTask myTask = new MyTask(Common.URL + "/CourseArticleServlet", jsonObject.toString());

        Gson gson = new Gson();
        String jsonStr = "";
        List<Course> courseList = null;

        try {
            jsonStr = myTask.execute().get();
            courseList = gson.fromJson(jsonStr, new TypeToken<List<Course>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    //server抓course對應的photo_img
    public static Bitmap getPhotoImg(List<Course> courseList, int position, int imageSize) {
        Bitmap bitmap = null;
        ImageTask imageTask = new ImageTask(Common.URL + "/photoServlet",
                courseList.get(position % courseList.size()).getCourse_image_id(), imageSize);
        try {
            bitmap = imageTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //server抓profession數據
    public static final List<ProfessionData> getProfessionData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "professionData");
        MyTask myTask = new MyTask(Common.URL + "/CourseArticleServlet", jsonObject.toString());

        Gson gson = new Gson();
        String jsonStr = "";
        List<ProfessionData> professionDataList = null;

        try {
            jsonStr = myTask.execute().get();

            professionDataList = gson.fromJson(jsonStr, new TypeToken<List<ProfessionData>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return professionDataList;
    }

    //server抓指定項目course數據
    public static List<Course> getCourseData(String professionItem) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "courseData");
        jsonObject.addProperty("professionItem", professionItem);
        MyTask myTask = new MyTask(Common.URL + "/CourseArticleServlet", jsonObject.toString());

        Gson gson = new Gson();
        String jsonStr = "";
        List<Course> courseList = null;

        try {
            jsonStr = myTask.execute().get();
            courseList = gson.fromJson(jsonStr, new TypeToken<List<Course>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return courseList;
    }
}
