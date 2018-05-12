package nom.cp101.master.master.Main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Calendar;
import nom.cp101.master.master.R;

import static android.content.Context.MODE_PRIVATE;

public class Common {
    public static String URL = "http://10.0.2.2:8080/Course_MySQL_WEB";

//    public static String URL = "http://192.168.196.77:8080/Master";

//    public static String user_id = "yujie1";

    public static String user_id = "aaa123";

    public static boolean networkConnected(Context context) {
        ConnectivityManager conManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    峻亦
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/" + servletStr;
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
            Common.showToast(activity, R.string.msg_NoNetwork);
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
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        Log.d("Common", "userName = " + userName);
        return userName;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    峻亦
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
