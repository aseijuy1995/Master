package nom.cp101.master.master.Account.MyCourse.CLASS;

/**
 * Created by chunyili on 2018/5/1.
 */

public class Photo {
    private int photo_id;
    private String user_id;
    private String photo;


    public Photo(int photo_id, String user_id, String photo) {
        super();
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.photo = photo;
    }

    public int getImage_id() {
        return photo_id;
    }

    public void setImage_id(int image_id) {
        this.photo_id = image_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return photo;
    }

    public void setImage(String image) {
        this.photo = image;
    }


}

