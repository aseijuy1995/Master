package nom.cp101.master.master.CourseArticle;

/**
 * Created by yujie on 2018/4/26.
 */

//各課程文章將包成此一類別
public class CourseArticleData {
    //課程名稱
    String courseArticleName;
    //課程招生人數
    String courseArticleNumber;
    //課程地點
    String courseArticleAddress;
    //課程時間
    String courseArticleTime;
    //課程流水編號
    int courseArticleId;

    public CourseArticleData(String courseArticleName, String courseArticleNumber, String courseArticleAddress, String courseArticleTime, int courseArticleId) {
        this.courseArticleName = courseArticleName;
        this.courseArticleNumber = courseArticleNumber;
        this.courseArticleAddress = courseArticleAddress;
        this.courseArticleTime = courseArticleTime;
        this.courseArticleId = courseArticleId;
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

    public int getCourseArticleId() {
        return courseArticleId;
    }

    public void setCourseArticleId(int courseArticleId) {
        this.courseArticleId = courseArticleId;
    }


}
