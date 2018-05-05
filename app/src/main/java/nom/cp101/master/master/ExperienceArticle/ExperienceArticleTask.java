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

public class ExperienceArticleTask extends AsyncTask<String, Integer,List<ExperienceArticleData>> {
    static final String tag="ExperienceArticleTask";
    String outStr;
    Gson gson=new Gson();

    public ExperienceArticleTask(String outStr) {
        this.outStr = outStr;
    }

    @Override
    protected List<ExperienceArticleData> doInBackground(String... strings) {
        HttpURLConnection conn=null;
        StringBuilder sb=new StringBuilder();
        List<ExperienceArticleData> experienceArticleDataList;

        try {
            conn=(HttpURLConnection) new URL(strings[0]).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("charset", "UTF-8");

            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(outStr);
            Log.d(tag, "ExperienceArticleTextOutPut:"+outStr);
            bw.close();


            if(conn.getResponseCode()==200){
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inStr="";

                while((inStr=br.readLine())!=null){
                    sb.append(inStr);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.d(tag, "ExperienceArticleTextInput:" + sb.toString());

            //將json字串轉成List<ExperienceArticleData>匯出
            experienceArticleDataList = gson.fromJson(sb.toString(), new TypeToken<List<ExperienceArticleData>>() {
            }.getType());

        }


        return experienceArticleDataList;
    }
}
