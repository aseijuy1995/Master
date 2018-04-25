package nom.cp101.master.master;


//message recyclerview 用到的屬性
public class Message_rv_item {
    //對方姓名
    String name;
    //message內容
    String content;
    //對方圖片
    int pictureID;
    //訊息時間
    String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Message_rv_item(String name, String content, int pictureID, String time) {

        this.name = name;
        this.content = content;
        this.pictureID = pictureID;
        this.time = time;
    }
}
