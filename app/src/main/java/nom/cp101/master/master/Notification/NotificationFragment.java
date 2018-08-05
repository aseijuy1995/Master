package nom.cp101.master.master.Notification;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvNotification;
    private TextView tvEmpty;

    private Context context;


    MyTask getItemsTask;
    GetPostImageTask getPostImageTask;
    GetPersonImageTask getPersonImageTask;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    List<NotificationItem> notificationItemList = new ArrayList<>();
    private final static String TAG = "Notification";

    String user_id;

    LinearLayoutManager linearLayoutManager;
    NotificationAdapter notificationAdapter;
    private int lastVisibleItemPosition = 0;
    private final int PAGE_COUNT = 10;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //啟用notification_fragment
        View view = inflater.inflate(R.layout.notification_frag, container, false);
        findViews(view);
        context = getContext();
        user_id = Common.getUserName(context);
        //取得notificaiton_recycerview 需要的資料
        notificationItemList = getNotificationItemList();
        return view;
    }

    private void findViews(View view) {
        //初始化notificaiton_recycerview
        rvNotification = (RecyclerView) view.findViewById(R.id.rvNotification);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
    }

    //從database取得通知的資料傳入list中
    private List<NotificationItem> getNotificationItemList() {
        if (Common.networkConnected(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            jsonObject.addProperty("user_id", user_id);
            List<NotificationItem> notificationItemList = null;
            getItemsTask = new MyTask(Common.URL + "/NotificationServlet", jsonObject.toString());
            try {
                String jsonin = getItemsTask.execute().get();
                notificationItemList = gson.fromJson(jsonin, new TypeToken<List<NotificationItem>>() {
                }.getType());

            } catch (Exception e) {
            }
            if (notificationItemList != null && notificationItemList.size() > 0) {
                return notificationItemList;
            }
        } else {
            showToast(context, R.string.NoConnection);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        rvNotification.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);

        initRecyclerView();
        initSwipeRefresh();
    }

    private void initRecyclerView() {
        if (notificationItemList.size() > 0) {
            rvNotification.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            //載入notificaiton_recycerview adapter
            notificationAdapter = new NotificationAdapter(context,
                    loadNotificationDatas(0, PAGE_COUNT),
                    loadNotificationDatas(0, PAGE_COUNT).size() > 0 ? true : false);
            //設定notificaiton_recycerview layout類型
            linearLayoutManager = new LinearLayoutManager(context);
            rvNotification.setLayoutManager(linearLayoutManager);
            rvNotification.setAdapter(notificationAdapter);
            rvNotification.setItemAnimator(new DefaultItemAnimator());
            rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (notificationAdapter.isHiddenHint() == false && lastVisibleItemPosition + 1 == notificationAdapter.getItemCount()) {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateRecyclerView(notificationAdapter.getLastPosition(), notificationAdapter.getLastPosition() + PAGE_COUNT);
                                }
                            }, 500);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                }
            });
        }
    }

    //自訂method,分頁加載
    private List<NotificationItem> loadNotificationDatas(final int firstIndex, final int lastIndex) {
        List<NotificationItem> loadList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < notificationItemList.size()) {
                loadList.add(notificationItemList.get(i));
            }
        }
        return loadList;
    }

    //自訂method,加載數據
    private void updateRecyclerView(int fromIndex, int toIndex) {
        // 获取从fromIndex到toIndex的数据
        List<NotificationItem> updateList = loadNotificationDatas(fromIndex, toIndex);
        if (updateList.size() > 0) {
            notificationAdapter.updateList(updateList, true);

        } else {
            notificationAdapter.updateList(null, false);
        }
    }

    private void initSwipeRefresh() {
        swipeRefresh.setSize(SwipeRefreshLayout.LARGE);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark);
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);

        if (this.notificationItemList.size() > 0) {
            rvNotification.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            //get comments and check size
            List<NotificationItem> notificationItems = getNotificationItemList();
            //依據當前experience的比數判斷是否做刷新
            if (this.notificationItemList.size() == notificationItems.size()) {
                notificationAdapter.notifyDataSetChanged();

            } else {
                notificationAdapter.resetDatas();
                notificationItemList = notificationItems;
                updateRecyclerView(0, PAGE_COUNT);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(false);
                }
            }, 500);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getItemsTask != null) {
            getItemsTask.cancel(true);
        }
    }

}
