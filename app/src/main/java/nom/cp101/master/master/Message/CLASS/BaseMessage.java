package nom.cp101.master.master.Message.CLASS;

/**
 * Created by chunyili on 2018/5/6.
 */

public class BaseMessage {
    private String message;
    private String userId;

    public BaseMessage(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}