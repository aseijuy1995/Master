package nom.cp101.master.master.CourseArticle;

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

//用於抓取db課程文章之數據
public class CourseArticleTask extends AsyncTask<String, Integer, List<CourseArticleData>> {
    static final String TAG_COURSE_ARTICLE = "CourseArticleTask";
    String outPutCourseArticle;

    public CourseArticleTask(String outPutCourseArticle) {
        this.outPutCourseArticle = outPutCourseArticle;
    }

    @Override
    protected List<CourseArticleData> doInBackground(String... strings) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        List<CourseArticleData> courseArticleDataList;

        try {
            //開啟連接
            conn = (HttpURLConnection) new URL(strings[0]).openConnection();
            //允許寫入
            conn.setDoInput(true);
            //允許寫出
            conn.setDoOutput(true);
            //請求內容長度未知時,可將數據切割多塊來傳輸,以免上傳之文件過大而遭損毀
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("charset", "UTF-8");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(outPutCourseArticle);
            Log.d(TAG_COURSE_ARTICLE, "courseArticleOutput:" + outPutCourseArticle);
            bw.close();

            //回傳responseCode=200為回傳成功
            if (conn.getResponseCode() == 200) {
                //將回傳數據撈出並用StringBuildert串起
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                Log.d(TAG_COURSE_ARTICLE, "courseArticleResponseCode:" + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG_COURSE_ARTICLE, "courseArticleInput:" + sb.toString());

            //將json字串轉成List<CourseArticleData>匯出
            courseArticleDataList = gson.fromJson(sb.toString(), new TypeToken<List<CourseArticleData>>() {
            }.getType());

        }
        return courseArticleDataList;
    }
}
