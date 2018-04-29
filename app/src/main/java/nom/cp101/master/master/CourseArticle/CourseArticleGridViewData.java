package nom.cp101.master.master.CourseArticle;

/**
 * Created by yujie on 2018/4/22.
 */

//此類別為將專業類別名稱與圖示
public class CourseArticleGridViewData {
    int categorytImg;
    int categoryName;

    public CourseArticleGridViewData(int categorytImg, int categoryName) {
        this.categorytImg = categorytImg;
        this.categoryName = categoryName;
    }

    public int getCategorytImg() {
        return categorytImg;
    }

    public void setCategorytImg(int categorytImg) {
        this.categorytImg = categorytImg;
    }

    public int getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(int categoryName) {
        this.categoryName = categoryName;
    }
}
