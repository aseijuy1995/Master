package nom.cp101.master.master;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yujie on 2018/4/26.
 */

//各課程文章將包成此一類別
public class ArticleCourseData {
    //發文者頭貼
    int articleHeadImg;
    //發文者名
    String articleHeadName;
    //文章類別
    String articleProject;

    //發文者圖片
    int articleImg;
    //文章內容
    String articleContent;
    //文章讚
    int articleLaud;

    public ArticleCourseData(int articleHeadImg, String articleHeadName, String articleProject, int articleImg, String articleContent, int articleLaud) {
        this.articleHeadImg = articleHeadImg;
        this.articleHeadName = articleHeadName;
        this.articleProject = articleProject;
        this.articleImg = articleImg;
        this.articleContent = articleContent;
        this.articleLaud = articleLaud;
    }

    public int getArticleHeadImg() {
        return articleHeadImg;
    }

    public void setArticleHeadImg(int articleHeadImg) {
        this.articleHeadImg = articleHeadImg;
    }

    public String getArticleHeadName() {
        return articleHeadName;
    }

    public void setArticleHeadName(String articleHeadName) {
        this.articleHeadName = articleHeadName;
    }

    public String getArticleProject() {
        return articleProject;
    }

    public void setArticleProject(String articleProject) {
        this.articleProject = articleProject;
    }

    public int getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(int articleImg) {
        this.articleImg = articleImg;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public int getArticleLaud() {
        return articleLaud;
    }

    public void setArticleLaud(int articleLaud) {
        this.articleLaud = articleLaud;
    }
}
