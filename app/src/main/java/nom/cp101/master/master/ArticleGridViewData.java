package nom.cp101.master.master;

/**
 * Created by yujie on 2018/4/22.
 */

//此類別為將專業類別名稱與圖示
public class ArticleGridViewData {
    int projectImg;
    int projectName;

    public ArticleGridViewData(int projectImg, int projectName) {
        this.projectImg = projectImg;
        this.projectName = projectName;
    }

    public int getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(int projectImg) {
        this.projectImg = projectImg;
    }

    public int getProjectName() {
        return projectName;
    }

    public void setProjectName(int projectName) {
        this.projectName = projectName;
    }
}
