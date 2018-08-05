package nom.cp101.master.master.Message.CLASS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.Master.MasterActivity;
import nom.cp101.master.master.R;

public class ChatReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;
    private final static int NOTIFICATON_ID = 1;
    private final static String TAG = "ChatReceiver";
    public static int atRoom = 0;
    public static final String SECOND_CHANNEL = "SECOND";


    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        String message = intent.getStringExtra("message");
        ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
        String lastMessage = chatMessage.getMessage();
        String sender = chatMessage.getSender();
        String friend_name = findRoomName(Common.getUserName(context), sender, context);
        Log.d(TAG, String.valueOf(atRoom) + "  " + friend_name);
        Intent nf_intent = new Intent(context, MasterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nf_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (atRoom == 0 && sender.equals(friend_name)) {


            Notification notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(SECOND_CHANNEL, "channel2", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLockscreenVisibility(Notification.DEFAULT_VIBRATE);
                notificationChannel.setLightColor(Color.BLUE);
                notificationManager.createNotificationChannel(notificationChannel);
                notification = new Notification.Builder(context, SECOND_CHANNEL)
                        .setTicker("New Notification") // ticker text is no longer displayed in Android 5.0
                        .setContentText(lastMessage)
                        .setContentTitle(friend_name)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
            } else {
                notification = new Notification.Builder(context)
                        .setTicker("New Notification") // ticker text is no longer displayed in Android 5.0
                        .setContentText(lastMessage)
                        .setContentTitle(friend_name)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_MAX)
                        .build();
            }
            notificationManager.notify(NOTIFICATON_ID, notification);
        }
    }


    public String findRoomName(String user_id, String room_name, Context context) {
        if (Common.networkConnected(context)) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findRoomName");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("room_name", room_name);
            String result = null;
            try {
                result = new MyTask(url, jsonObject.toString()).execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (result.isEmpty()) {
                Log.d(TAG, "Find room name 失敗");
                return null;
            } else {
                Log.d(TAG, "Find room name 成功");
                return result;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(context, "no network");
            return null;
        }
    }

}
