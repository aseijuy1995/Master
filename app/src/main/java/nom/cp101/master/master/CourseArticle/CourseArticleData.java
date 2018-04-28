package nom.cp101.master.master.CourseArticle;

/**
 * Created by yujie on 2018/4/26.
 */

//各課程文章將包成此一類別
public class CourseArticleData {
    //發文者頭貼
    int courseArticleHeadImg;
    //發文者名
    String courseArticleHeadName;
    //文章類別
    String courseArticleProject;

    //發文者圖片
    int courseArticleleImg;
    //文章內容
    String courseArticleContent;
    //文章讚
    int courseArticleLaud;

    public CourseArticleData(int courseArticleHeadImg, String courseArticleHeadName, String courseArticleProject, int courseArticleImg, String courseArticleContent, int courseArticleLaud) {
        this.courseArticleHeadImg = courseArticleHeadImg;
        this.courseArticleHeadName = courseArticleHeadName;
        this.courseArticleProject = courseArticleProject;
        this.courseArticleleImg = courseArticleImg;
        this.courseArticleContent = courseArticleContent;
        this.courseArticleLaud = courseArticleLaud;
    }

    public int getCourseArticleHeadImg() {
        return courseArticleHeadImg;
    }

    public void setCourseArticleHeadImg(int courseArticleHeadImg) {
        this.courseArticleHeadImg = courseArticleHeadImg;
    }

    public String getCourseArticleHeadName() {
        return courseArticleHeadName;
    }

    public void setCourseArticleHeadName(String courseArticleHeadName) {
        this.courseArticleHeadName = courseArticleHeadName;
    }

    public String getCourseArticleProject() {
        return courseArticleProject;
    }

    public void setCourseArticleProject(String courseArticleProject) {
        this.courseArticleProject = courseArticleProject;
    }

    public int getCourseArticleImg() {
        return courseArticleleImg;
    }

    public void setCourseArticleleImg(int courseArticleleImg) {
        this.courseArticleleImg = courseArticleleImg;
    }

    public String getCourseArticleContent() {
        return courseArticleContent;
    }

    public void setCourseArticleContent(String courseArticleContent) {
        this.courseArticleContent = courseArticleContent;
    }

    public int getCourseArticleLaud() {
        return courseArticleLaud;
    }

    public void setCourseArticleLaud(int courseArticleLaud) {
        this.courseArticleLaud = courseArticleLaud;
    }
}
