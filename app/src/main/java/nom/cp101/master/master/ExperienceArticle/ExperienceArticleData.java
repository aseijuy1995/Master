package nom.cp101.master.master.ExperienceArticle;

import android.graphics.Bitmap;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import nom.cp101.master.master.ExperienceArticleActivity.Comment;
import nom.cp101.master.master.ExperienceArticleActivity.ExperienceArticleLeaveMsgData;

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

    byte[] imgPictureByte;
    boolean isCheckable;
    byte[] imgHeadByte;
    List<Comment> commentList;


    //單一心得文章之所有數據
    public ExperienceArticleData(byte[] imgPictureByte, boolean isCheckable, byte[] imgHeadByte, String postUserName, String postTime,
                                 String postContent , List<Comment> commentList) {
        super();
        this.imgPictureByte = imgPictureByte;
        this.isCheckable = isCheckable;
        this.imgHeadByte = imgHeadByte;
        this.userName = postUserName;
        this.postTime = postTime;
        this.postContent = postContent;
        this.commentList = commentList;
    }

    String user_name="";
    byte[] user_portrait=null;

    public ExperienceArticleData(String user_name, byte[] user_portrait) {
        this.user_name = user_name;
        this.user_portrait = user_portrait;
    }






    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public byte[] getUser_portrait() {
        return user_portrait;
    }

    public void setUser_portrait(byte[] user_portrait) {
        this.user_portrait = user_portrait;
    }

    public byte[] getImgPictureByte() {
        return imgPictureByte;
    }

    public void setImgPictureByte(byte[] imgPictureByte) {
        this.imgPictureByte = imgPictureByte;
    }

    public boolean isCheckable() {
        return isCheckable;
    }

    public void setCheckable(boolean checkable) {
        isCheckable = checkable;
    }

    public byte[] getImgHeadByte() {
        return imgHeadByte;
    }

    public void setImgHeadByte(byte[] imgHeadByte) {
        this.imgHeadByte = imgHeadByte;
    }


    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
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
