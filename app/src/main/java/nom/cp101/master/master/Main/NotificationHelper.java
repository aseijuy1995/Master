package nom.cp101.master.master.Main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import nom.cp101.master.master.R;


/**
 * Created by chunyili on 2018/5/10.
 */

public class NotificationHelper extends ContextWrapper {

    private static final String CHANEL_ID = "com.example.chunyili.mynotification.BROADCAST";
    private static final String CHANNEL_NAME = "CHAT CHANNEL";
    private NotificationManager manager;
    public NotificationHelper(Context base){
        super(base);
        createChannel();
    }

    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.GREEN);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public Notification.Builder getChannelNotification(String title, String body){
        return new Notification.Builder(getApplicationContext(),CHANEL_ID)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);
    }
}
