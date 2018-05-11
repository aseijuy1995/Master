package nom.cp101.master.master.ExperienceArticle;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by yujie on 2018/5/4.
 */

public class ExperienceArticleTask extends AsyncTask<String, Integer, String> {
    static final String tag = "ExperienceArticleTask";
    String outStr="";

    public ExperienceArticleTask(String outStr) {
        this.outStr = outStr;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();

        try {
            conn = (HttpURLConnection) new URL(strings[0]).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("charset", "UTF-8");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(outStr);
            Log.d(tag, "outPut:" + outStr);
            bw.close();

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(tag, "ExperienceArticleTextInput:" + sb.toString());

            } else {
                Log.d(tag, "responseCode:" + conn.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
        return sb.toString();
    }
}
