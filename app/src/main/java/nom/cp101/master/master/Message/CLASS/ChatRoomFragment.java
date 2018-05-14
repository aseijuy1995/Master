package nom.cp101.master.master.Message.CLASS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/5/9.
 */

public class ChatRoomFragment extends Fragment {
    private ArrayList<String> rooms = new ArrayList<String>();
    private static String friend_id;
    public static String user_id,friend_name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ArrayList<ChatRoom> roomList = new ArrayList<ChatRoom>();
    private String TAG = "ChatRoomFragment";
    private View view;
    private ImageView didNotSignIn;
    private RecyclerView rvRoomList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_chat_room_frag,container,false);

        findView();

        return view;
    }

    private void buildRecyclerView() {
        rvRoomList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRoomList.setAdapter(new RoomListAdapter(getContext(),roomList));
        rvRoomList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        user_id = Common.getUserName(getContext());
        if(user_id.isEmpty()){
            didNotSignIn.setVisibility(View.VISIBLE);
            rvRoomList.setVisibility(View.INVISIBLE);
        }else{
            rootAddValue();

            buildRecyclerView();
        }
    }

    private void findView() {
        rvRoomList = view.findViewById(R.id.rvRoomList);
        didNotSignIn = view.findViewById(R.id.didNotSignIn);
    }


    public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.myViewHolder>{

        Context context;
        List<ChatRoom> chatRooms;

        public RoomListAdapter(Context context, List<ChatRoom> chatRooms) {
            this.context = context;
            this.chatRooms = chatRooms;
        }

        public class myViewHolder extends RecyclerView.ViewHolder{
            ImageView user_property;
            TextView user_name,last_message;
            CardView chatRoomItem;

            public myViewHolder(View itemView) {
                super(itemView);
                user_property = itemView.findViewById(R.id.user_property);
                user_name = itemView.findViewById(R.id.user_name);
                last_message = itemView.findViewById(R.id.last_message);
                chatRoomItem = itemView.findViewById(R.id.chatRoomItem);
            }
        }

        @Override
        public RoomListAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.message_chat_room_item,parent,false);
            return new myViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, final int position) {
            ChatRoom chatRoom = roomList.get(position);
            holder.user_name.setText(chatRoom.getRoom_name());
            String lastMessage = null;
            if(chatRoom.getLast_message().equals(null)){
                holder.last_message.setText("");
            }else{
                if(chatRoom.getLast_message().length()>50){
                    lastMessage = chatRoom.getLast_message().substring(0,50);
                }else{
                    lastMessage = chatRoom.getLast_message();
                }
                holder.last_message.setText(lastMessage);
            }
            holder.chatRoomItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    friend_name = roomList.get(position).getRoom_name();
                    String room_position = roomList.get(position).getRoom_position();
                    enterChatRoom(room_position,user_id);

                }
            });

        }

        @Override
        public int getItemCount() {
            return roomList.size();
        }
    }

    private void enterChatRoom(String room_position,String user_id) {
        Bundle bundle = new Bundle();
        bundle.putString("room_position",room_position);
        bundle.putString("userName",user_id);
        bundle.putString("friendName",friend_name);
        Intent intent = new Intent(getContext(),MessageActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    private void rootAddValue() {
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                roomList = findRoom(user_id);
                while(iterator.hasNext()){
                    String key1 = ((DataSnapshot)iterator.next()).getKey();
                    if(roomList != null){
                        for (int i = 0;i<roomList.size();i++){
                            String position = roomList.get(i).getRoom_position();
                            String room_name = roomList.get(i).getRoom_name();
                            if(key1.equals(position)){
                                list.add(room_name);
                            }
                        }
                    }
                }
                rooms.clear();
                if(!list.isEmpty()){
                    rooms.addAll(list);
                    rvRoomList.getAdapter().notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public int createRoom(String chat_room_position){
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
            if (id == 0 ) {
                Log.d(TAG,"create room 失敗");
                return id;
            } else {
                Log.d(TAG,"create room 成功");
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getContext(), "no network");
            return 0;
        }
    }

    public int connectUserRoom(String user_id, String room_name, int chat_room_id){
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "connectUserToRoom");
            jsonObject.addProperty("user_id", user_id );
            jsonObject.addProperty("room_name",room_name);
            jsonObject.addProperty("chat_room_id",chat_room_id);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"connect room 失敗");
                return id;
            } else {
                Log.d(TAG,"connect room 成功");
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getContext(), "no network");
            return 0;
        }
    }

    public ArrayList<ChatRoom> findRoom(String user_id){
        if (Common.networkConnected(getActivity())) {
            ArrayList<ChatRoom> roomList = new ArrayList<ChatRoom>();
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findRoomByUserId");
            jsonObject.addProperty("user_id", user_id );
            String jsonOut = jsonObject.toString();
            MyTask courseGetAllTask = new MyTask(url,jsonOut);
            try {
                String jsonIn = courseGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<ChatRoom>>(){ }.getType();
                roomList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (roomList == null ) {
                Log.d(TAG,"無聊天室");
                return null;
            } else {
                Log.d(TAG,"搜尋成功");
                return  roomList;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getContext(), "no network");
            return null;
        }
    }
}
