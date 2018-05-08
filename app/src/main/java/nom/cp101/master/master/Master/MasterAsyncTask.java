package nom.cp101.master.master.Master;

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

//首頁執行緒-取autoCompleteTextView的提示字
public class MasterAsyncTask extends AsyncTask<String, Integer, List<String>> {
    String outputStr;
    static final String tag = "MasterAsyncTask";

    public MasterAsyncTask(String outputStr) {
        this.outputStr = outputStr;
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
            conn.setRequestProperty("charset", "UTF-8");
            conn.setChunkedStreamingMode(0);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(outputStr);
            Log.d(tag, "output:" + outputStr);

            bw.close();

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(tag, "input:" + sb.toString());

                projectNameList = gson.fromJson(sb.toString(), new TypeToken<List<String>>() {
                }.getType());

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
        return projectNameList;
    }
}
