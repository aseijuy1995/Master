package nom.cp101.master.master.CourseArticleActivity;

/**
 * Created by yujie on 2018/4/29.
 */

//專業類別中之項目class
public class CourseArticleCategoryData {
    int projectImg;
    String projectName;

    public CourseArticleCategoryData(int projectImg, String projectName) {
        this.projectImg = projectImg;
        this.projectName = projectName;
    }

    public CourseArticleCategoryData(int projectImg) {
        this.projectImg = projectImg;
    }

    public int getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(int projectImg) {
        this.projectImg = projectImg;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
