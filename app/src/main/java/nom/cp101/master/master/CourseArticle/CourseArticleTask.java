package nom.cp101.master.master.CourseArticle;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//連至server抓取db內所有課程文章之數據
public class CourseArticleTask extends AsyncTask<String, Integer, String> {
    static final String tag = "CourseArticleTask";
    String outputStr;

    public CourseArticleTask(String outputStr) {
        this.outputStr = outputStr;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();

        try {
            //開啟連接
            conn = (HttpURLConnection) new URL(strings[0]).openConnection();
            //允許寫入
            conn.setDoInput(true);
            //允許寫出
            conn.setDoOutput(true);
            conn.setRequestProperty("charset", "UTF-8");
            //請求內容長度未知時,可將數據切割多塊來傳輸,以免上傳之文件過大而遭損毀
            conn.setChunkedStreamingMode(0);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(outputStr);
            Log.d(tag, "output:" + outputStr);
            bw.close();

            //回傳responseCode=200為回傳成功
            if (conn.getResponseCode() == 200) {

                //將回傳數據撈出並用StringBuilder串起
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(tag, "input:" + sb.toString());

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
