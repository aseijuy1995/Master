package nom.cp101.master.master;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yujie on 2018/4/26.
 */

public class ArticleExperienceData {
    int position;
    int img;
    List<Integer> imgList;

    public ArticleExperienceData(int position) {
        this.position = position;
        imgList=takeImgList();
    }

    public int getImg() {
        return imgList.get(position);
    }

    public void setImg(int img) {
        this.img = img;
    }

    private List<Integer> takeImgList() {
        imgList=new ArrayList<>();

        imgList.add(R.drawable.a1);
        imgList.add(R.drawable.a2);
        imgList.add(R.drawable.a3);
        imgList.add(R.drawable.a1);
        imgList.add(R.drawable.a2);

        return imgList;
    }

}
