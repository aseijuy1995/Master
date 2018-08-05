package nom.cp101.master.master.Account.MyPhoto;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.R;

@TargetApi(Build.VERSION_CODES.M)
public class MyPhotoAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    public static final String TAG = "MyPhotoAdapter";
    private Context context;
    private ArrayList<String> userAllPostId;
    private GridView gridView;
    private LruCache<String, Bitmap> lruCache;
    private int firstVisibleItem;
    private int visiblePhotoCount;
    private boolean isFirstEnter = true;

    public MyPhotoAdapter(Context context, ArrayList<String> userAllPostId, GridView gridView) {
        this.context = context;
        this.userAllPostId = userAllPostId;
        this.gridView = gridView;
        initMemoryCache();
    }

    public void initMemoryCache() {
        //緩存記憶體空間
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        //override存於緩存中的圖片大小
        lruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        gridView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return userAllPostId.size();
    }

    //取得為圖片bitmap
    @Override
    public Object getItem(int position) {
        return ConnectionServer.getPhotoByPostId(userAllPostId.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //取得文章的id,可做為進一步取得photo條件
        final String postId = userAllPostId.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.account_photo_item, null);
            holder = new ViewHolder();
            holder.photo_item = (convertView).findViewById(R.id.photo_item);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //給予ImageView設置tag以便夾在圖片順序不錯亂
        holder.photo_item.setTag(postId);

        //讀取緩存取得,圖片bitmap,依postId作為key值
        final Bitmap bitmap = getBitmapFromMemoryCache(postId);
        if (bitmap != null) {
            holder.photo_item.setImageBitmap(bitmap);

        } else {
            holder.photo_item.setImageResource(R.drawable.master_empty_picture);
        }

        final ViewHolder finalHolder = holder;
        holder.photo_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyPhotoShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("postId", postId);
                bundle.putStringArrayList("userAllPostId", userAllPostId);
                intent.putExtras(bundle);
                context.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation((Activity)
                                        context,
                                finalHolder.photo_item,
                                "shareImg").toBundle());
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView photo_item;
    }

    //用url作為key值,存放bitmap圖片
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null)
            lruCache.put(key, bitmap);
    }

    //用url作為key值,讀取存於緩存LruCache中的圖片
    public Bitmap getBitmapFromMemoryCache(String key) {
        return lruCache.get(key);
    }

    //判斷GridView中的捲動是否停下,以便撈數據或將task取消
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmap(firstVisibleItem, visiblePhotoCount);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.visiblePhotoCount = visibleItemCount;

        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmap(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    //firstVisiblephoto為第一圖片之索引, visiblePhotoCount為可視圖片數量
    //此method為檢查緩存中的圖片是否存在,若沒有則連server撈
    public void loadBitmap(int firstVisiblePhoto, int visiblePhotoCount) {
        for (int i = firstVisiblePhoto; i < firstVisiblePhoto + visiblePhotoCount; i++) {
            String postId = userAllPostId.get(i);
            //檢測緩存機制
            Log.d(TAG, "load by cache, postId " + postId);

            Bitmap bitmap = getBitmapFromMemoryCache(postId);
            ImageView imageView = (ImageView) gridView.findViewWithTag(postId);

            if (bitmap == null) {
                bitmap = ConnectionServer.getPhotoByPostId(postId);
                if (bitmap != null) {
                    addBitmapToMemoryCache(postId, bitmap);
                }
                if (imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}
