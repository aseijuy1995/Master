package nom.cp101.master.master.Notification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import nom.cp101.master.master.R;

public class GetPersonImageTask extends AsyncTask<Object, Integer, Bitmap> {
    private String url, user_Name;
    private int imageSize;
    private WeakReference<ImageView> imageViewWeakReference;
    private final static String TAG = "GetPersonImageTask";


    public GetPersonImageTask(String url, String user_Name, int imageSize, ImageView imageView) {
        this.url = url;
        this.user_Name = user_Name;
        this.imageSize = imageSize;
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ImageView imageView = imageViewWeakReference.get();
        imageView.setImageResource(R.drawable.picture);
    }

    @Override
    protected Bitmap doInBackground(Object... objects) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getPersonImage");
        jsonObject.addProperty("user_Name", user_Name);
        jsonObject.addProperty("imageSize", imageSize);
        return getRemoteImage(url, jsonObject.toString());
    }

    private Bitmap getRemoteImage(String url, String jsonOut) {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(jsonOut);
            bw.close();
            Log.d(TAG, "Output : " + jsonOut);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                bitmap = BitmapFactory.decodeStream(new BufferedInputStream(connection.getInputStream()));
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }

        } catch (Exception e) {
            Log.d(TAG, "Error : " + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = imageViewWeakReference.get();
        if (imageView == null) {
            return;
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

    }

}

