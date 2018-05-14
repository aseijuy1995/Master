package nom.cp101.master.master.Main;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.JsonObject;

import nom.cp101.master.master.Message.CLASS.ChatReceiver;
import nom.cp101.master.master.Notification.NotificationReceiver;
import nom.cp101.master.master.Notification.NotificationSocket;


public class MainService extends Service {
    private final static String TAG = "MainService";
    private LocalBroadcastManager broadcastManager;
    private Handler handler;
    private Runnable runnable;
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "handler");
                handler.postDelayed(this, 5000);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", "checkNotification");
                Common.notificationSocket.send(jsonObject.toString());
            }
        };
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MainService");
        wakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        IntentFilter notificationfilter = new IntentFilter("newNotification");
        IntentFilter chatFilter = new IntentFilter("message_chat_offline");
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(notificationReceiver, notificationfilter);
        broadcastManager.registerReceiver(chatReceiver, chatFilter);

        if (Common.notificationSocket != null){
        handler.postDelayed(runnable, 5000);
        }else{
            Common.connectSocket(this);
        }
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent ServiceIntent = new Intent(this, MainService.class);
        startService(ServiceIntent);
    }
}


