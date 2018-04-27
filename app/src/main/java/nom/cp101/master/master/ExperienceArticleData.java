package nom.cp101.master.master;

import java.util.Date;

/**
 * Created by yujie on 2018/4/27.
 */
//各心得文章將包成此一類別
public class ExperienceArticleData {

    //發文者頭貼
    int experienceHeadImg;
    //發文者名
    String experienceHeadName;
    //發文者圖片
    int experienceImg;
    //發文時間
    Date experienceTime;
    //文章讚
    int experienceLaud;
    //文章內容
    String experienceContent;

    public ExperienceArticleData(int experienceHeadImg, String experienceHeadName, int experienceImg, Date experienceTime, int experienceLaud, String experienceContent) {
        this.experienceHeadImg = experienceHeadImg;
        this.experienceHeadName = experienceHeadName;
        this.experienceImg = experienceImg;
        this.experienceTime = experienceTime;
        this.experienceLaud = experienceLaud;
        this.experienceContent = experienceContent;
    }

    public int getExperienceHeadImg() {
        return experienceHeadImg;
    }

    public void setExperienceHeadImg(int experienceHeadImg) {
        this.experienceHeadImg = experienceHeadImg;
    }

    public String getExperienceHeadName() {
        return experienceHeadName;
    }

    public void setExperienceHeadName(String experienceHeadName) {
        this.experienceHeadName = experienceHeadName;
    }

    public int getExperienceImg() {
        return experienceImg;
    }

    public void setExperienceImg(int experienceImg) {
        this.experienceImg = experienceImg;
    }

    public Date getExperienceTime() {
        return experienceTime;
    }

    public void setExperienceTime(Date experienceTime) {
        this.experienceTime = experienceTime;
    }

    public int getExperienceLaud() {
        return experienceLaud;
    }

    public void setExperienceLaud(int experienceLaud) {
        this.experienceLaud = experienceLaud;
    }

    public String getExperienceContent() {
        return experienceContent;
    }

    public void setExperienceContent(String experienceContent) {
        this.experienceContent = experienceContent;
    }
}
