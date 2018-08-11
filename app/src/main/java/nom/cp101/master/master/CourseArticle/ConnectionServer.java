package nom.cp101.master.master.CourseArticle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;

import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.Account.MyCourse.Course;
import nom.cp101.master.master.Account.MyPhoto.MyPhotoFragment;
import nom.cp101.master.master.ExperienceArticle.Experience;
import nom.cp101.master.master.ExperienceArticle.ExperienceComment;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

/**
 * Created by yujie on 2018/5/16.
 */

public class ConnectionServer {
    private static final String CourseArticleServlet = "/CourseArticleServlet";
    private static final String ExperienceArticleServlet = "/ExperienceArticleServlet";

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

    //取最新3筆課程courseId,photoId
    public static final List<Course> getCourseNewPhotoId() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "getCourseNewPhotoId");
        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String jsonStr = "";
        List<Course> courseList = null;
        try {
            jsonStr = myTask.execute().get();
            courseList = new Gson().fromJson(jsonStr, new TypeToken<List<Course>>() {
            }.getType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return courseList;
    }

    //取photo對應photoId
    public static final Bitmap getPhotoByPhotoId(String photoId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "getPhotoByPhotoId");
        jsonObject.addProperty("photoId", photoId);
        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String photoStr = null;
        Bitmap bitmap = null;
        try {
            String jsonIn = myTask.execute().get();
            photoStr = new Gson().fromJson(jsonIn, String.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (photoStr != null) {
            byte[] userPhoto = Base64.decode(photoStr, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length);
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return bitmap;
    }

    //取profession_category, profession_item
    public static final List<Profession> getProfession() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "getProfession");
        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String jsonStr = "";
        List<Profession> professionList = null;
        try {
            jsonStr = myTask.execute().get();
            professionList = new Gson().fromJson(jsonStr, new TypeToken<List<Profession>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return professionList;
    }

    //取所有使用者userId,userName
    public static final List<User> getCourseUsers(String userId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "getCourseUsers");
        if (userId == null || userId.isEmpty() || userId == "") {
            jsonObject.addProperty("userId", "");
        } else {
            jsonObject.addProperty("userId", userId);
        }

        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String jsonStr = "";
        List<User> userList = null;
        try {
            jsonStr = myTask.execute().get();
            userList = new Gson().fromJson(jsonStr, new TypeToken<List<User>>() {
            }.getType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return userList;
    }

    //取photo對應userId
    public static Bitmap getPhotoByUserId(String userId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "getPhotoByUserId");
        jsonObject.addProperty("userId", userId);
        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String photoStr = null;
        Bitmap bitmap = null;
        try {
            String jsonIn = myTask.execute().get();
            photoStr = new Gson().fromJson(jsonIn, String.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (photoStr != null) {
            byte[] userPhoto = Base64.decode(photoStr, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length);
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return bitmap;
    }

    //取profession_item相關的course
    public static final List<Course> getCourseByProfessionItem(String professionItem) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "getCourseByProfessionItem");
        jsonObject.addProperty("professionItem", professionItem);
        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String jsonStr = "";
        List<Course> courseList = null;
        try {
            jsonStr = myTask.execute().get();
            courseList = new Gson().fromJson(jsonStr, new TypeToken<List<Course>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return courseList;
    }

    //取course的join人數
    public static final int getCourseJoin(int courseId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("courseArticle", "courseJoin");
        jsonObject.addProperty("courseId", courseId);
        MyTask myTask = new MyTask(Common.URL + CourseArticleServlet, jsonObject.toString());
        String jsonStr = "";
        int courseJoin = 0;
        try {
            jsonStr = myTask.execute().get();
            courseJoin = new Gson().fromJson(jsonStr, Integer.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return courseJoin;
    }

    //取experience所有數據,依時間由後到前排序
    public static final List<Experience> getExperiences(String userId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "getExperiences");
        //傳userId為判斷每篇post當前user是否有按過讚
        jsonObject.addProperty("userId", userId);
        MyTask myTask = new MyTask(Common.URL + ExperienceArticleServlet, jsonObject.toString());
        String jsonStr = "";
        List<Experience> experienceDataList = null;
        try {
            jsonStr = myTask.execute().get();
            experienceDataList = new Gson().fromJson(jsonStr.toString(), new TypeToken<List<Experience>>() {
            }.getType());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return experienceDataList;
    }

    //取photo對應postId
    public static Bitmap getPhotoByPostId(String postId) {
        String url = Common.URL + MyPhotoFragment.URL_INTENT;
        String photoStr = null;
        Bitmap bitmap = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserPostPhoto");
        jsonObject.addProperty("postId", postId);
        MyTask myTask = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = myTask.execute().get();
            photoStr = new Gson().fromJson(jsonIn, String.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (photoStr != null || photoStr != "") {
            byte[] userPhoto = Base64.decode(photoStr, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length);
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return bitmap;
    }

    //刷新postLikes並回傳讚數
    public static int getExperiencePostLikeRefresh(String userId, int postId, boolean checked) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "getExperiencePostLikeRefresh");
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("postId", postId);
        jsonObject.addProperty("checked", checked);

        MyTask myTask = new MyTask(Common.URL + ExperienceArticleServlet, jsonObject.toString());
        String jsonStr = "";
        int postLikes = 0;

        try {
            jsonStr = myTask.execute().get();
            postLikes = new Gson().fromJson(jsonStr.toString(), Integer.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return postLikes;
    }

    //取experience指定數據
    public static Experience getExperience(String userId, int postId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "getExperience");
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("postId", postId);
        MyTask myTask = new MyTask(Common.URL + ExperienceArticleServlet, jsonObject.toString());
        String jsonStr = "";
        Experience experienceData = null;

        try {
            jsonStr = myTask.execute().get();
            experienceData = new Gson().fromJson(jsonStr.toString(), Experience.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return experienceData;
    }

    //取experience留言數
    public static int getExperienceCommentCount(int postId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "getExperienceCommentCount");
        jsonObject.addProperty("postId", postId);

        MyTask myTask = new MyTask(Common.URL + ExperienceArticleServlet, jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        int commentCount = 0;
        try {
            jsonStr = myTask.execute().get();
            commentCount = gson.fromJson(jsonStr.toString(), Integer.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return commentCount;
    }

    //取experience留言數據
    public static List<ExperienceComment> getExperienceComment(int postId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "getExperienceComment");
        jsonObject.addProperty("postId", postId);
        MyTask myTask = new MyTask(Common.URL + ExperienceArticleServlet, jsonObject.toString());
        String jsonStr = "";
        List<ExperienceComment> commentList = null;
        try {
            jsonStr = myTask.execute().get();
            commentList = new Gson().fromJson(jsonStr.toString(), new TypeToken<List<ExperienceComment>>() {
            }.getType());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return commentList;
    }

    //新增experience留言
    public static int setExperienceComment(String userName, int postId, String commentStr) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "setExperienceComment");
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("postId", postId);
        jsonObject.addProperty("commentStr", commentStr);
        MyTask myTask = new MyTask(Common.URL + ExperienceArticleServlet, jsonObject.toString());
        String jsonStr = "";
        int insertOK = 0;
        try {
            jsonStr = myTask.execute().get();
            insertOK = new Gson().fromJson(jsonStr.toString(), Integer.class);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (myTask != null) {
            myTask.cancel(true);
        }
        return insertOK;
    }

    public static String findUserNameById(String user_id) {
        String url = Common.URL + "/chatRoomServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findUserNameById");
        jsonObject.addProperty("user_id", user_id);
        String name = "";
        try {
            name = new MyTask(url, jsonObject.toString()).execute().get();
        } catch (Exception e) {
        }
        if (name != null && !name.equals("")) {
            return name;
        } else {
            return "";
        }
    }


    //server取出指定user_id的頭貼.名字
    public static User getUserData(String userId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("experienceArticle", "experienceUserData");
        jsonObject.addProperty("userId", userId);

        MyTask myTask = new MyTask(Common.URL + "/ExperienceArticleServlet", jsonObject.toString());
        String jsonStr = "";
        Gson gson = new Gson();
        User user = null;

        try {
            jsonStr = myTask.execute().get();

            user = gson.fromJson(jsonStr.toString(), User.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;

    }

}
