package nom.cp101.master.master.Main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nom.cp101.master.master.Account.AccountMain.LoginActivity;
import nom.cp101.master.master.Notification.NotificationSocket;
import nom.cp101.master.master.R;

import static android.content.Context.MODE_PRIVATE;

public class Common {
//    public static String URL = "http://10.0.2.2:8080/Master";

    public static String URL = "http://172.20.10.2:8080/Master";

//    public static String user_id = "abc123";

    public final static int COACH_ACCESS = 1; // 教練權限
    public final static int STUDENT_ACCESS = 2; // 學員權限

    public static boolean networkConnected(Context context) {
        ConnectivityManager conManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static final String SOCKET_URI =
            "ws://10.0.2.2:8080/MasterActivity/NotificationSocket/";
    public static NotificationSocket notificationSocket;


    public static void connectSocket(Context context) {
        URI uri = null;
        try {
            uri = new URI(SOCKET_URI + getUserName(context));
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
    public static final String SERVER_URI = "ws://10.0.2.2:8080/MasterActivity/TwoChatServer/";
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


    public static int insertUpdateCourseServlet(Activity activity, String TAG, String servletStr, String actionValue, Gson gson, Object course) {
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
            if (id == 0) {
                Log.d(TAG, "失敗，From " + servletStr + actionValue);
                return id;
            } else {
                Log.d(TAG, "成功，From " + servletStr + actionValue);
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            showToast(activity, R.string.msg_NoNetwork);
            return 0;
        }
    }


    public static String contectUser(String user_id, String friend_id, Context context, Activity activity) {
        String temp_key = contectRoot.push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(temp_key, "");
        contectRoot.updateChildren(map);
        int chat_room_id = createRoom(temp_key, context, activity);
        connectUserRoom(user_id, friend_id, chat_room_id, activity, context);
        connectUserRoom(friend_id, user_id, chat_room_id, activity, context);
        return temp_key;
    }

    public static int createRoom(String chat_room_position, Context context, Activity activity) {
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
            if (id == 0) {
                Log.d(TAG, "create room 失敗");
                return id;
            } else {
                Log.d(TAG, "create room 成功");
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(context, "no network");
            return 0;
        }
    }

    public static int connectUserRoom(String user_id, String room_name, int chat_room_id, Activity activity, Context context) {
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "connectUserToRoom");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("room_name", room_name);
            jsonObject.addProperty("chat_room_id", chat_room_id);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0) {
                Log.d(TAG, "connect room 失敗");
                return id;
            } else {
                Log.d(TAG, "connect room 成功");
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(context, "no network");
            return 0;
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    峻亦
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 取出權限
    public static int getUserAccess(Context context, String userId) {
        SharedPreferences preferences =
                context.getSharedPreferences("preference", MODE_PRIVATE);
        int userAccess = preferences.getInt("userAccess" + userId, 0);
        Log.d(TAG, "userAccess "+userId+" =" + userAccess);
        return userAccess;
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        preferences.edit().putString("userName", userName).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        Log.d(TAG, "userName = " + userName);
        return userName;
    }

    //檢查有無帳號
    public static Boolean checkUserName(Context context, String userName) {
        if (userName == "" || userName == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    //檢查多個權限-for activity
    public static void askPermissionByActivity(Activity activity, String[] permissions, int requestCode) {
        Set<String> permissionRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ActivityCompat.checkSelfPermission(activity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionRequest.add(permission);
            }
        }
        if (!permissionRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionRequest.toArray(new String[permissionRequest.size()]), requestCode);
        }
    }

    //檢查多個權限-for fragment
    public static void askPermissionByFragment(Context context, Fragment fragment, String[] permissions, int requestCode) {
        Set<String> permissionRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionRequest.add(permission);
            }
        }
        if (!permissionRequest.isEmpty()) {
            fragment.requestPermissions(permissionRequest.toArray(new String[permissionRequest.size()]), requestCode);
        }
    }

    //取當前時間,可作為file_name
    public static String getTimeAsName() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        return rel;
    }

    //network
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

    public static void showDatePicker(Context context, final EditText editText) {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                editText.setText(year + "-" + month + "-" + day);
            }
        }, year, month, day);
        datePicker.setTitle(context.getResources().getString(R.string.date_title));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }

}
