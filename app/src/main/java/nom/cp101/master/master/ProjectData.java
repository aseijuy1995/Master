package nom.cp101.master.master;

/**
 * Created by yujie on 2018/4/22.
 */

public class ProjectData {
    int imgId;
    String tv;

    public ProjectData(int imgId, String tv) {
        this.imgId = imgId;
        this.tv = tv;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }
}
