package nom.cp101.master.master.Notification;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

// notificaiton recycerview 用到的屬性
public class Notification_rv_item implements Serializable {
    // 留言回覆者,發文者的姓名
    private String name;
    // 留言內容,發文內容
    private String content;
    // 通知類型
    public int nf_type;
    // 通知時間
    private Date time;
    //通知對應的文章
    private int post_id;

    public Notification_rv_item(String name, String content, Date time, int post_id) {
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
        switch (nf_type) {
            case 1:
                text = name + " 發出了新的課程文章";
                break;
            case 2:
                text = name + " 發出了新的心得文";
                break;
            case 3:
                text = name + " 回覆了你的貼文";
                break;
            case 4:
                text = name + " 的文章有了新的留言";
                break;
        }

        return text;
    }

    public void setNf_type(int nf_type) {
        this.nf_type = nf_type;
    }

    public String getTime() {
        String Time = null;
        SimpleDateFormat FormatDate = new SimpleDateFormat("MM/dd");
        SimpleDateFormat FormatTime = new SimpleDateFormat("HH:mm");
        Date nowdate = new Date();
        //目前時間
        String formatNowDate = FormatDate.format(nowdate);
        //通知時間
        String formatNFDate = FormatDate.format(time);
        //比對目前時間與通知時間
        if (formatNowDate.equals(formatNFDate)) {
            Time = FormatTime.format(time);
        } else {
            Time = formatNFDate;
        }
        return Time;
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