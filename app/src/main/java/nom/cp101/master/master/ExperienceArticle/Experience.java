package nom.cp101.master.master.ExperienceArticle;

import java.util.Date;

//各心得文章類別
public class Experience {
    public int postId;
    String userId;
    String postContent;
    Date postTime;
    int photoId;

    String userName;
    String userPortraitStr;
    String photoImgStr;
    boolean postLike;
    int postLikes;


    public Experience(int postId, String userId, String postContent, Date postTime, int photoId,
                      String userName, boolean postLike, int postLikes) {
        super();
        this.postId = postId;
        this.userId = userId;
        this.postContent = postContent;
        this.postTime = postTime;
        this.photoId = photoId;
        this.userName = userName;
        this.userPortraitStr = userPortraitStr;
        this.photoImgStr = photoImgStr;
        this.postLike = postLike;
        this.postLikes = postLikes;
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

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isPostLike() {
        return postLike;
    }

    public void setPostLike(boolean postLike) {
        this.postLike = postLike;
    }

    public int getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(int postLikes) {
        this.postLikes = postLikes;
    }


}
