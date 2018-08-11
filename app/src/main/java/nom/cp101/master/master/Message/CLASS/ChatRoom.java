package nom.cp101.master.master.Message.CLASS;

/**
 * Created by chunyili on 2018/5/8.
 */

public class ChatRoom {
    String friend_user_id;
    String room_name;
    String room_position;
    String last_message;


    public ChatRoom(String friend_user_id, String room_name, String room_position, String last_message) {
        this.friend_user_id = friend_user_id;
        this.room_name = room_name;
        this.room_position = room_position;
        this.last_message = last_message;
    }

    public String getFriend_user_id() {
        return friend_user_id;
    }

    public void setFriend_user_id(String friend_user_id) {
        this.friend_user_id = friend_user_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_position() {
        return room_position;
    }

    public void setRoom_position(String room_position) {
        this.room_position = room_position;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }
}
