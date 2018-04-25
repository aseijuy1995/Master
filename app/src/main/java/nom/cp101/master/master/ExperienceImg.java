package nom.cp101.master.master;

import java.io.Serializable;

/**
 * Created by yujie on 2018/4/25.
 */

public class ExperienceImg implements Serializable {
    int Img;

    public ExperienceImg(int img) {
        Img = img;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }



}
