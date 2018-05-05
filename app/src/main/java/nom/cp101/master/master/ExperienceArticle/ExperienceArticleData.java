package nom.cp101.master.master.ExperienceArticle;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by yujie on 2018/4/27.
 */
//各心得文章將包成此一類別
public class ExperienceArticleData {

    int postId;
    String userId;
    String userName;
    String postCategoryName;
    int postPhotoId;
    String postTime;
    String postContent;



    public ExperienceArticleData(int postId, String userId, String userName, String postCategoryName, int postPhotoId,
                                     String postTime, String postContent) {
        super();
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.postCategoryName = postCategoryName;
        this.postPhotoId = postPhotoId;
        this.postTime = postTime;
        this.postContent = postContent;
    }



    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostCategoryName() {
        return postCategoryName;
    }

    public void setPostCategoryName(String postCategoryName) {
        this.postCategoryName = postCategoryName;
    }

    public int getPostPhotoId() {
        return postPhotoId;
    }

    public void setPostPhotoId(int postPhotoId) {
        this.postPhotoId = postPhotoId;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

}
