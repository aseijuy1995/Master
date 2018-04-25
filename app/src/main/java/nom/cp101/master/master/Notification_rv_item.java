package nom.cp101.master.master;


//notificaiton recycerview 用到的屬性
public class Notification_rv_item {
    //留言回覆者,發文者的姓名
    String name;
    //留言內容,發文內容
    String content;
    //留言回覆者,發文者的圖片
    int pictureID;
    //通知類型
    int nf_type;
    //通知時間
    String time;

    public Notification_rv_item(String name, String content, int pictureID, String time) {
        this.name = name;
        this.content = content;
        this.pictureID = pictureID;
        this.time = time;
    }

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
        String text = "";
        this.content = content;
        switch (nf_type) {
            case 0:
                text = getName() + "發表了新的文章";
                break;
            case 1:
                text = getName() + "回應了你的貼文";
                break;
            case 2:
                text = getName() + "也回應了你關注的貼文";
            default:
                break;

        }

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
}
