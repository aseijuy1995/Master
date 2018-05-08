package nom.cp101.master.master.ExperienceArticle;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by yujie on 2018/5/4.
 */

public class ExperienceArticleImgTask extends AsyncTask<String, Integer, Bitmap> {
    static final String EAIT_TAG = "ExperienceArticleImgTask";
    String outStr;
    ImageView iv;
    int imageviewSize;
    int i;

    private WeakReference<ImageView> imageViewWeakReference;

    public ExperienceArticleImgTask(String outStr, ImageView iv, int imageviewSize, int i) {
        this.outStr = outStr;
        this.iv = iv;
        this.imageviewSize = imageviewSize;
        this.i = i;
        imageViewWeakReference = new WeakReference<>(iv);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection conn = null;
        Bitmap bitmap = null;

        try {
            conn = (HttpURLConnection) new URL(strings[0]).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(outStr);
            Log.d(EAIT_TAG, "outPut:" + outStr);
            bw.close();


            if (conn.getResponseCode() == 200) {
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

                bitmap = BitmapFactory.decodeStream(bis);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        ImageView imageView = imageViewWeakReference.get();
        if (isCancelled() || imageView == null) {
            return;
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
