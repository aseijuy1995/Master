package nom.cp101.master.master.ExperienceArticle;

import java.util.Date;

/**
 * Created by yujie on 2018/4/27.
 */
//各心得文章將包成此一類別
public class ExperienceArticleData {

    //發文者頭貼
    int experienceArticleHeadImg;
    //發文者名
    String experienceArticleHeadName;
    //文章類別
    String experienceArticleCategory;

    //發文者圖片
    int experienceArticleImg;
    //課程名稱
    String experienceArticleName;
    //發文時間
    Date experienceArticleTime;
    //文章內容
    String experienceArticleContent;
    //文章讚
    int experienceArticleLaud;

    public ExperienceArticleData(int experienceArticleHeadImg, String experienceArticleHeadName, String experienceArticleCategory, int experienceArticleImg, String experienceArticleName, Date experienceArticleTime, String experienceArticleContent, int experienceArticleLaud) {
        this.experienceArticleHeadImg = experienceArticleHeadImg;
        this.experienceArticleHeadName = experienceArticleHeadName;
        this.experienceArticleCategory = experienceArticleCategory;
        this.experienceArticleImg = experienceArticleImg;
        this.experienceArticleName = experienceArticleName;
        this.experienceArticleTime = experienceArticleTime;
        this.experienceArticleContent = experienceArticleContent;
        this.experienceArticleLaud = experienceArticleLaud;
    }

    public int getExperienceArticleHeadImg() {
        return experienceArticleHeadImg;
    }

    public void setExperienceArticleHeadImg(int experienceArticleHeadImg) {
        this.experienceArticleHeadImg = experienceArticleHeadImg;
    }

    public String getExperienceArticleHeadName() {
        return experienceArticleHeadName;
    }

    public void setExperienceArticleHeadName(String experienceArticleHeadName) {
        this.experienceArticleHeadName = experienceArticleHeadName;
    }

    public String getExperienceArticleCategory() {
        return experienceArticleCategory;
    }

    public void setExperienceArticleCategory(String experienceArticleCategory) {
        this.experienceArticleCategory = experienceArticleCategory;
    }

    public int getExperienceArticleImg() {
        return experienceArticleImg;
    }

    public void setExperienceArticleImg(int experienceArticleImg) {
        this.experienceArticleImg = experienceArticleImg;
    }

    public String getExperienceArticleName() {
        return experienceArticleName;
    }

    public void setExperienceArticleName(String experienceArticleName) {
        this.experienceArticleName = experienceArticleName;
    }

    public Date getExperienceArticleTime() {
        return experienceArticleTime;
    }

    public void setExperienceArticleTime(Date experienceArticleTime) {
        this.experienceArticleTime = experienceArticleTime;
    }

    public String getExperienceArticleContent() {
        return experienceArticleContent;
    }

    public void setExperienceArticleContent(String experienceArticleContent) {
        this.experienceArticleContent = experienceArticleContent;
    }

    public int getExperienceArticleLaud() {
        return experienceArticleLaud;
    }

    public void setExperienceArticleLaud(int experienceArticleLaud) {
        this.experienceArticleLaud = experienceArticleLaud;
    }
}
