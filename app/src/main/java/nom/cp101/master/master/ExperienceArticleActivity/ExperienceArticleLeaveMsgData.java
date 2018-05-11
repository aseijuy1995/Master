package nom.cp101.master.master.ExperienceArticleActivity;

/**
 * Created by yujie on 2018/5/11.
 */

public class ExperienceArticleLeaveMsgData {

    byte[] imgHeadLeaveMsg;
    String tvLeaveMsgName;
    String tvLeaveMsgContent;
    String tvLeaveMsgTime;

    public ExperienceArticleLeaveMsgData(byte[] imgHeadLeaveMsg, String tvLeaveMsgName, String tvLeaveMsgContent, String tvLeaveMsgTime) {
        this.imgHeadLeaveMsg = imgHeadLeaveMsg;
        this.tvLeaveMsgName = tvLeaveMsgName;
        this.tvLeaveMsgContent = tvLeaveMsgContent;
        this.tvLeaveMsgTime = tvLeaveMsgTime;
    }


    public byte[] getImgHeadLeaveMsg() {
        return imgHeadLeaveMsg;
    }

    public void setImgHeadLeaveMsg(byte[] imgHeadLeaveMsg) {
        this.imgHeadLeaveMsg = imgHeadLeaveMsg;
    }

    public String getTvLeaveMsgName() {
        return tvLeaveMsgName;
    }

    public void setTvLeaveMsgName(String tvLeaveMsgName) {
        this.tvLeaveMsgName = tvLeaveMsgName;
    }

    public String getTvLeaveMsgContent() {
        return tvLeaveMsgContent;
    }

    public void setTvLeaveMsgContent(String tvLeaveMsgContent) {
        this.tvLeaveMsgContent = tvLeaveMsgContent;
    }

    public String getTvLeaveMsgTime() {
        return tvLeaveMsgTime;
    }

    public void setTvLeaveMsgTime(String tvLeaveMsgTime) {
        this.tvLeaveMsgTime = tvLeaveMsgTime;
    }
}
