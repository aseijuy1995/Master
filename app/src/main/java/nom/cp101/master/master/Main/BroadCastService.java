package nom.cp101.master.master.Main;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by chunyili on 2018/5/9.
 */

public class BroadCastService extends Service {
    private final IBinder binder = new ServiceBinder();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "onBind", Toast.LENGTH_SHORT).show();
        return binder;
    }

    public class ServiceBinder extends Binder {
        public BroadCastService getService() {
            return BroadCastService.this;
        }
    }

}
