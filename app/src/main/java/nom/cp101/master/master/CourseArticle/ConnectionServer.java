package nom.cp101.master.master.CourseArticle;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;

import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.ExperienceArticle.ExperienceData;
import nom.cp101.master.master.ExperienceArticle.Comment;
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
    public static final List<Course> getCourseDatas() {
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
    public static final Bitmap getPhotoImg(List<Course> courseList, int position, int imageSize) {
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
    public static final List<Course> getCourseData(String professionItem) {
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

    //server抓指定course的參加人數
    public static final int getCourseJoin(int courseId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "courseJoin");
        jsonObject.addProperty("courseId", courseId);
        MyTask myTask = new MyTask(Common.URL + "/CourseArticleServlet", jsonObject.toString());

        Gson gson = new Gson();
        String jsonStr = "";
        int courseJoin = 0;

        try {
            jsonStr = myTask.execute(Common.URL + "/CourseArticleServlet").get();
            courseJoin = gson.fromJson(jsonStr, Integer.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return courseJoin;
    }

    //server抓指定experience的所有數據
    public static final List<ExperienceData> getExperienceDatas(String userId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceDatas");
        jsonObject.addProperty("userId", userId);
        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";

        Gson gson = new Gson();
        List<ExperienceData> experienceDataList = null;

        try {
            jsonStr = myTask.execute().get();

            experienceDataList = gson.fromJson(jsonStr.toString(), new TypeToken<List<ExperienceData>>() {
            }.getType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return experienceDataList;
    }

    //server存取或刪除讚的動作並回傳刷新讚數
    public static int getExperienceLikeRefresh(String userId, int postId, boolean checked) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "postLikeRefresh");
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("postId", postId);
        jsonObject.addProperty("checked", checked);

        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        int postLikes = 0;

        try {
            jsonStr = myTask.execute().get();

            postLikes = gson.fromJson(jsonStr.toString(), Integer.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return postLikes;
    }

    //server抓指定experience數據
    public static ExperienceData getExperienceData(String userId, int postId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceData");
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("postId", postId);

        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        ExperienceData experienceData = null;

        try {
            jsonStr = myTask.execute().get();

            experienceData = gson.fromJson(jsonStr.toString(), ExperienceData.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return experienceData;
    }

    //server抓指定experience文章留言數據
    public static List<Comment> getExperienceComment(int postId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceComment");
        jsonObject.addProperty("postId", postId);

        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        List<Comment> commentList = null;

        try {
            jsonStr = myTask.execute().get();

            commentList = gson.fromJson(jsonStr.toString(), new TypeToken<List<Comment>>() {
            }.getType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return commentList;

    }

    //server對指定experience文章新增留言
    public static int setExperienceComment(String userName, int postId, String commentStr) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceInsertComment");
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("postId", postId);
        jsonObject.addProperty("commentStr", commentStr);

        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        int insertOK=0;

        try {
            jsonStr = myTask.execute().get();

            insertOK = gson.fromJson(jsonStr.toString(), Integer.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return insertOK;
    }

    public static User getUserData(String userId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceUserData");
        jsonObject.addProperty("userId", userId);

        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        User user=null;

        try {
            jsonStr = myTask.execute().get();

            user= gson.fromJson(jsonStr.toString(), User.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;

    }
}
