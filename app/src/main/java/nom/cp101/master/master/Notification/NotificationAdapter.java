package nom.cp101.master.master.Notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.ExperienceArticle.ExperienceArticleSimple.ExperienceSimpleActivity;
import nom.cp101.master.master.R;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NotificationItem> notificationItemList;
    private boolean hasMore;
    private LruCache<String, Bitmap> lruCache;
    private final String NOTIFICATION_IMG = "NOTIFICATION_IMG";


    private final int notificationType = 0;
    private final int footType = 1;
    private boolean hiddenHint = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public NotificationAdapter(Context context, List<NotificationItem> notificationItemList, boolean hasMore) {
        this.context = context;
        this.notificationItemList = notificationItemList;
        this.hasMore = hasMore;
        initMemoryCache();
    }

    //LruCache機制
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
    }

    //refresh notification last item is footType
    @Override
    public int getItemViewType(int position) {
        if (getItemCount() > 0) {
            if (position == getItemCount() - 1) {
                return footType;

            } else {
                return notificationType;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (notificationItemList.size() > 0 && notificationItemList != null) {
            return notificationItemList.size() + 1;
        } else {
            return 0;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == notificationType) {
            view = LayoutInflater.from(context).inflate(R.layout.notification_normal_item, parent, false);
            return new NotificationViewHolder(view);

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.notification_foot_item, parent, false);
            return new FootViewHolder(view);
        }
    }

    public void resetDatas() {
        notificationItemList = new ArrayList<>();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        //初始化view中的元件
        ImageView item_picture;
        TextView item_time, item_content;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            item_picture = itemView.findViewById(R.id.nf_itemview_picture);
            item_time = itemView.findViewById(R.id.nf_itemview_time);
            item_content = itemView.findViewById(R.id.nf_itemview_content);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbNotification;

        public FootViewHolder(View itemView) {
            super(itemView);
            pbNotification = (ProgressBar) itemView.findViewById(R.id.pbNotification);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewholder, int position) {
        //將對應的position資料塞入view中
        if (viewholder instanceof NotificationViewHolder) {
            final NotificationItem notificationItem = notificationItemList.get(position);
            Bitmap bitmap = null;
            Log.e("NotificationAdapter", String.valueOf(notificationItem.nf_type));


            switch (notificationItem.nf_type) {
                case 1:
                case 2:
                    bitmap = lruCache.get(NOTIFICATION_IMG + notificationItem.getPost_id());
                    if (bitmap != null) {
                        ((NotificationViewHolder) viewholder).item_picture.setImageBitmap(bitmap);

                    } else {
                        ((NotificationViewHolder) viewholder).item_picture.setImageResource(R.drawable.master_empty_picture);
                        bitmap = ConnectionServer.getPhotoByPostId(String.valueOf(notificationItem.getPost_id()));

                        if (bitmap != null) {
                            ((NotificationViewHolder) viewholder).item_picture.setImageBitmap(bitmap);
                            lruCache.put(NOTIFICATION_IMG + notificationItem.getPost_id(), bitmap);
                        }
                    }
                    break;

                case 3:
                case 4:
                    bitmap = lruCache.get(NOTIFICATION_IMG + notificationItem.getName_id());
                    if (bitmap != null) {
                        ((NotificationViewHolder) viewholder).item_picture.setImageBitmap(bitmap);

                    } else {
                        ((NotificationViewHolder) viewholder).item_picture.setImageResource(R.drawable.user);
                        bitmap = ConnectionServer.getPhotoByUserId(notificationItem.getName_id());

                        if (bitmap != null) {
                            ((NotificationViewHolder) viewholder).item_picture.setImageBitmap(bitmap);
                            lruCache.put(NOTIFICATION_IMG + notificationItem.getName_id(), bitmap);
                        }
                    }
                    break;

                default:
                    break;
            }
            ((NotificationViewHolder) viewholder).item_time.setText(notificationItem.getTime());
            ((NotificationViewHolder) viewholder).item_content.setText(notificationItem.getNf_type());

            //click itemview 轉頁至文章
            ((NotificationViewHolder) viewholder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    Bundle bundle = new Bundle();
                    int post_id = notificationItem.getPost_id();
//                        intent = new Intent(getActivity(), ExperienceActivity.class);
                    intent = new Intent(context, ExperienceSimpleActivity.class);
                    bundle.putInt("experienceArticlePostId", post_id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            //footView
        } else {
            ((FootViewHolder) viewholder).pbNotification.setVisibility(View.VISIBLE);

            if (hasMore == true) {
                hiddenHint = false;

            } else {
                if (notificationItemList.size() > 0) {

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            ((FootViewHolder) viewholder).pbNotification.setVisibility(View.GONE);
                            hiddenHint = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }


//        switch (notificationItem.nf_type) {
//                case 1:
//                    getPostImageTask = new GetPostImageTask(Common.URL + "/NotificationServlet",
//                            item.getPost_id(), context.getResources().getDisplayMetrics().widthPixels, viewholder.item_picture);
//                    getPostImageTask.execute();
//                    break;
//                case 2:
//                    getPostImageTask = new GetPostImageTask(Common.URL + "/NotificationServlet",
//                            item.getPost_id(), context.getResources().getDisplayMetrics().widthPixels, viewholder.item_picture);
//                    getPostImageTask.execute();
//                    break;
//                case 3:
//                    getPersonImageTask = new GetPersonImageTask(Common.URL + "/NotificationServlet",
//                            item.getName(), context.getResources().getDisplayMetrics().widthPixels, viewholder.item_picture);
//
//                    getPersonImageTask.execute();
//                    break;
//
//                case 4:
//                    getPersonImageTask = new GetPersonImageTask(Common.URL + "/NotificationServlet",
//                            item.getName(), context.getResources().getDisplayMetrics().widthPixels, viewholder.item_picture);
//                    getPersonImageTask.execute();
//                    break;


    }

    public void updateList(List<NotificationItem> updateList, boolean hasMore) {
        if (updateList != null) {
            this.notificationItemList.addAll(updateList);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    public boolean isHiddenHint() {
        return hiddenHint;
    }

    public int getLastPosition() {
        return notificationItemList.size();
    }


}

