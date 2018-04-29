package nom.cp101.master.master.CourseArticle;

/**
 * Created by yujie on 2018/4/26.
 */

//各課程文章將包成此一類別
public class CourseArticleData {
    //課程名稱
    String courseArticleName;
    //課程人數比
    String courseArticleNumber;
    //課程地點
    String courseArticleAddress;
    //課程時間
    String courseArticleTime;

    public CourseArticleData(String courseArticleName, String courseArticleNumber, String courseArticleAddress, String courseArticleTime) {
        this.courseArticleName = courseArticleName;
        this.courseArticleNumber = courseArticleNumber;
        this.courseArticleAddress = courseArticleAddress;
        this.courseArticleTime = courseArticleTime;
    }

    public String getCourseArticleName() {
        return courseArticleName;
    }

    public void setCourseArticleName(String courseArticleName) {
        this.courseArticleName = courseArticleName;
    }

    public String getCourseArticleNumber() {
        return courseArticleNumber;
    }

    public void setCourseArticleNumber(String courseArticleNumber) {
        this.courseArticleNumber = courseArticleNumber;
    }

    public String getCourseArticleAddress() {
        return courseArticleAddress;
    }

    public void setCourseArticleAddress(String courseArticleAddress) {
        this.courseArticleAddress = courseArticleAddress;
    }

    public String getCourseArticleTime() {
        return courseArticleTime;
    }

    public void setCourseArticleTime(String courseArticleTime) {
        this.courseArticleTime = courseArticleTime;
    }
}
