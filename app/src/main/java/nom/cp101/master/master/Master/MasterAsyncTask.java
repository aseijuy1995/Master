package nom.cp101.master.master.Master;

import android.os.AsyncTask;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.Log;
import android.widget.TableRow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import nom.cp101.master.master.Common;

/**
 * Created by yujie on 2018/5/1.
 */

public class MasterAsyncTask extends AsyncTask<String, Integer, List<String>> {
    String outPutStr;
    static final String tag = "MasterAsyncTask";

    public MasterAsyncTask(String outPutStr) {
        this.outPutStr = outPutStr;
    }

    @Override
    protected List<String> doInBackground(String... strings) {

        HttpURLConnection conn = null;


        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();

        List<String> projectNameList = null;


        try {
            conn = (HttpURLConnection) new URL(strings[0]).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setChunkedStreamingMode(0);
            conn.setUseCaches(false);
            conn.setRequestProperty("charset", "UTF-8");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            bw.write(outPutStr);
            Log.d(tag, "MasterOutput:" + outPutStr);

            bw.close();

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(tag, "MasterInput:" + sb.toString());

                projectNameList = gson.fromJson(sb.toString(), new TypeToken<List<String>>() {
                }.getType());


            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }


        return projectNameList;
    }
}
