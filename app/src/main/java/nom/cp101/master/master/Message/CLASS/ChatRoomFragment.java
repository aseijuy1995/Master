package nom.cp101.master.master.Message.CLASS;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/5/9.
 */

public class ChatRoomFragment extends Fragment implements ValueEventListener, View.OnClickListener {
    private Context context;
    private ImageView didNotSignIn;
    private TextView tvEmpty;
    private RecyclerView rvRoomList;
    private LinearLayoutManager linearLayoutManager;
    private RoomListAdapter roomListAdapter;

    private String TAG = "ChatRoomFragment";
    private DatabaseReference root;
    //get user all chat_room
    private ArrayList<ChatRoom> roomList;
    //user all chat_room_name, that,s a friend_name
    private ArrayList<String> rooms;
    private String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_chat_room_frag, container, false);
        context = getContext();
        roomList = new ArrayList<>();
        findView(view);

        return view;
    }

    private void findView(View view) {
        rvRoomList = (RecyclerView) view.findViewById(R.id.rvRoomList);
        didNotSignIn = (ImageView) view.findViewById(R.id.didNotSignIn);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
    }

    @Override
    public void onResume() {
        super.onResume();
        user_id = Common.getUserName(context);

        didNotSignIn.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        rvRoomList.setVisibility(View.GONE);

        if (user_id != null) {
            didNotSignIn.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            rootAddValue();
            initRecyclerView();
        }
    }

    private void rootAddValue() {
        root = FirebaseDatabase.getInstance().getReference().getRoot();
        root.addValueEventListener(this);
    }

    private void initRecyclerView() {
        if (roomList.size() > 0 || !roomList.isEmpty()) {
            tvEmpty.setVisibility(View.GONE);
            rvRoomList.setVisibility(View.VISIBLE);

            linearLayoutManager = new LinearLayoutManager(context);
            rvRoomList.setLayoutManager(linearLayoutManager);
            rvRoomList.setItemAnimator(new DefaultItemAnimator());
            roomListAdapter = new RoomListAdapter(context, roomList, user_id);
            rvRoomList.setAdapter(roomListAdapter);
        }
    }

    //當fireDataBase內其Database有數據變動時則觸發
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<String> list = new ArrayList<>();
        //迭代器,近於Cursor用法
        Iterator iterator = dataSnapshot.getChildren().iterator();
        //check have chat room
        rooms = new ArrayList<>();
        roomList = findRoom(user_id);
        if (roomList.size() > 0 || rvRoomList != null) {
            tvEmpty.setVisibility(View.GONE);
            rvRoomList.setVisibility(View.VISIBLE);
        }
        while (iterator.hasNext()) {
            String key = ((DataSnapshot) iterator.next()).getKey();
            if (roomList != null) {
                for (int i = 0; i < roomList.size(); i++) {
                    String position = roomList.get(i).getRoom_position();
                    String room_name = roomList.get(i).getRoom_name();
                    if (key.equals(position)) {
                        list.add(room_name);
                    }
                }
            }
        }
        rooms.clear();
        if (list.size() > 0 || !list.isEmpty()) {
            rooms.addAll(list);
            initRecyclerView();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.didNotSignIn:
                user_id = Common.getUserName(getContext());
                didNotSignIn.setVisibility(View.INVISIBLE);
                rvRoomList.setVisibility(View.VISIBLE);
                rootAddValue();
                initRecyclerView();
                break;
        }
    }

    public ArrayList<ChatRoom> findRoom(String user_id) {
        if (Common.networkConnected(context)) {
            ArrayList<ChatRoom> roomList = new ArrayList<>();
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findRoomByUserId");
            jsonObject.addProperty("user_id", user_id);
            String jsonOut = jsonObject.toString();
            MyTask courseGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<ChatRoom>>() {
                }.getType();
                roomList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (roomList == null) {
                Log.d(TAG, "無聊天室");
                return null;
            } else {
                Log.d(TAG, "搜尋成功");
                Log.d(TAG, roomList.toString());
                return roomList;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(getContext(), "no network");
            return null;
        }
    }

    public int createRoom(String chat_room_position) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "createChatRoom");
            jsonObject.addProperty("chat_room_position", chat_room_position);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0) {
                Log.d(TAG, "create room 失敗");
                return id;
            } else {
                Log.d(TAG, "create room 成功");
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(getContext(), "no network");
            return 0;
        }
    }

    public int connectUserRoom(String user_id, String room_name, int chat_room_id) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "connectUserToRoom");
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("room_name", room_name);
            jsonObject.addProperty("chat_room_id", chat_room_id);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0) {
                Log.d(TAG, "connect room 失敗");
                return id;
            } else {
                Log.d(TAG, "connect room 成功");
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(getContext(), "no network");
            return 0;
        }
    }

}