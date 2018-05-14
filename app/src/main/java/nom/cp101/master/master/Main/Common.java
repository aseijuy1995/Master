package nom.cp101.master.master.Main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import nom.cp101.master.master.Account.Login;
import nom.cp101.master.master.Notification.NotificationSocket;
import nom.cp101.master.master.R;

import static android.content.Context.MODE_PRIVATE;

public class Common {
    public static String URL = "http://10.0.2.2:8080/Master";

//    public static String URL = "http://192.168.196.77:8080/Master";

//    public static String user_id = "yujie1";

//    public static String user_id = null;
    public static String user_id = "";

    public final static int COACH_ACCESS = 1; // 教練權限

    public final static int STUDENT_ACCESS = 2; // 學員權限

    public static boolean networkConnected(Context context) {
        ConnectivityManager conManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static final String SOCKET_URI =
            "ws://10.0.2.2:8080/Master/NotificationSocket/";
    public static NotificationSocket notificationSocket;



    public static void connectSocket(Context context) {
        URI uri = null;
        try {
            uri = new URI(SOCKET_URI + user_id);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (notificationSocket == null) {
            notificationSocket = new NotificationSocket(uri, context);
            notificationSocket.connect();
        }
    }

    public static void disconnectSocket() {
        if (notificationSocket != null) {
            notificationSocket.close();
            notificationSocket = null;
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    峻亦
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final static String TAG = "Common";
    public static final String SERVER_URI = "ws://10.0.2.2:8080/WSChatBasic_Web/TwoChatServer/";
    public static ChatWebSocket chatWebSocket;
    public static DatabaseReference contectRoot = FirebaseDatabase.getInstance().getReference().getRoot();

    // 建立WebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (chatWebSocket == null) {
            chatWebSocket = new ChatWebSocket(uri, context);
            chatWebSocket.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (chatWebSocket != null) {
            chatWebSocket.close();
            chatWebSocket = null;
        }
    }


    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static int insertUpdateCourseServlet(Activity activity, String TAG, String servletStr, String actionValue, Gson gson, Object course){
        if (networkConnected(activity)) {
            String url = URL + "/" + servletStr;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", actionValue);
            jsonObject.addProperty("course", gson.toJson(course));
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"失敗，From " + servletStr + actionValue);
                return id;
            } else {
                Log.d(TAG,"成功，From " + servletStr + actionValue);
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            showToast(activity, R.string.msg_NoNetwork);
            return 0;
        }
    }

    public static void showDatePicker(Context context, final EditText editText) {
        int year,month,day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                editText.setText(year+"-"+month+"-"+day);
            }
        }, year,month,day);
        datePicker.setTitle("日期");
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        preferences.edit().putString("userName", userName).apply();
        user_id = userName;

    }

    public static String getUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        Log.d(TAG, "userName = " + userName);
        return userName;
    }


    public static void contectUser(String user_id,String friend_id,Context context,Activity activity){
        String temp_key = contectRoot.push().getKey();
        Map<String,Object> map = new HashMap<>();
        map.put(temp_key,"");
        contectRoot.updateChildren(map);
        int chat_room_id = createRoom(temp_key,context,activity);
        connectUserRoom(user_id,friend_id,chat_room_id,activity,context);
        connectUserRoom(friend_id,user_id,chat_room_id,activity,context);
    }

    public static int createRoom(String chat_room_position, Context context, Activity activity){
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "createChatRoom");
            jsonObject.addProperty("chat_room_position", chat_room_position);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"create room 失敗");
                return id;
            } else {
                Log.d(TAG,"create room 成功");
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(context, "no network");
            return 0;
        }
    }

    public static int connectUserRoom(String user_id, String room_name, int chat_room_id, Activity activity, Context context){
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "connectUserToRoom");
            jsonObject.addProperty("user_id", user_id );
            jsonObject.addProperty("room_name",room_name);
            jsonObject.addProperty("chat_room_id",chat_room_id);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"connect room 失敗");
                return id;
            } else {
                Log.d(TAG,"connect room 成功");
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(context, "no network");
            return 0;
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    峻亦
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 子桓的檢查是否有帳號方法, 傳入 getUserName(getActivity()) 及 getFragmentManager() 回傳布林
    public static Boolean checkUserName(String userName, FragmentManager fragmentManager) {
        Boolean result = false;
        if (userName == "" || userName== null) {
            Fragment fragment = new Login();
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        } else {
            result = true;
        }
        return result;
    }

    // 存進權限
    public static void setUserAccess(Context context, int userAccess) {
        SharedPreferences preferences =
                context.getSharedPreferences("access", MODE_PRIVATE);
        preferences.edit().putInt("userAccess", userAccess).apply();
    }

    // 拿出權限
    public static int getUserAccess(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("access", MODE_PRIVATE);
        int userAccess = preferences.getInt("userAccess",0);
        Log.d(TAG, "userAccess = " + userAccess);
        return userAccess;
    }


}
