package nom.cp101.master.master.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import nom.cp101.master.master.Master.Master;
import nom.cp101.master.master.R;

public class NotificationReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;

    private final static int NOTIFICATON_ID = 0;
    public static final String PRIMARY_CHANNEL = "default";


    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent nf_intent = new Intent(context, Master.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "Notification");
        nf_intent.putExtras(bundle);
        nf_intent.setAction(Intent.ACTION_MAIN);
        nf_intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nf_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL, "channel1", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLockscreenVisibility(Notification.DEFAULT_VIBRATE);
            notificationChannel.setLightColor(Color.GREEN);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = new Notification.Builder(context, PRIMARY_CHANNEL)
                    .setTicker("New Notification") // ticker text is no longer displayed in Android 5.0
                    .setContentTitle("Master")
                    .setContentText(context.getString(R.string.nf_newnotificaiton))
                    .setSmallIcon(R.drawable.notification)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            notification = new Notification.Builder(context)
                    .setTicker("New Notification") // ticker text is no longer displayed in Android 5.0
                    .setContentTitle("Master")
                    .setContentText(context.getString(R.string.nf_newnotificaiton))
                    .setSmallIcon(R.drawable.notification)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();
        }
        notificationManager.notify(NOTIFICATON_ID, notification);
    }

}
