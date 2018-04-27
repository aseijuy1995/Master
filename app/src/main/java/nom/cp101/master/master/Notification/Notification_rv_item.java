package nom.cp101.master.master.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;

// notificaiton recycerview 用到的屬性
public class Notification_rv_item {
    // 留言回覆者,發文者的姓名
    String name;
    // 留言內容,發文內容
    String content;
    // 通知類型
    int nf_type;
    // 通知時間
    Date time;
    //通知對應的文章
    int post_id;

    public Notification_rv_item(String name, String content, Date time,int post_id) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.post_id = post_id;
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
        this.content = content;
    }

    public String getNf_type() {
        String text = null;
        switch (nf_type){
            case 1 :
                text = name + " 發出了新的文章";
                break;
            case 2:
                text = name + " 回覆了你的貼文";
                break;
            case 3:
                text = name + " 也回覆了你的留言";
                break;
        }

        return text;
    }

    public void setNf_type(int nf_type) {
        this.nf_type = nf_type;
    }

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        String formatetime = simpleDateFormat.format(time);
        return formatetime;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

}