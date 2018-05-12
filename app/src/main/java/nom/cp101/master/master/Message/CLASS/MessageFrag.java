package nom.cp101.master.master.Message.CLASS;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.Master.Master;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.chatWebSocket;

/**
 * Created by chunyili on 2018/5/5.
 */

public class MessageFrag extends Fragment {
    private EditText etMessage;
    private Button btnSend;
    private DatabaseReference root;
    private String roomName,friendName,userName,temp_key,chat_msg,chat_user_name;
    private RecyclerView messageList;
    private MessageListAdapter adapter;
    private ArrayList<BaseMessage> messages = new ArrayList<BaseMessage>();
    private String TAG = "Message Fragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Master.atRoom = 1;
        View view = inflater.inflate(R.layout.message_content,container,false);
        findView(view);
        sendClick();
        rootAddChild();
        adapter = new MessageListAdapter(getContext(),messages);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        messageList.setLayoutManager(manager);
        load();
        return view;
    }

    private void findView(View view) {
        messageList = view.findViewById(R.id.messageList);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);

        Bundle bundle = getArguments();
        roomName = bundle.getString("roomName");
        userName = bundle.getString("userName");
        friendName = bundle.getString("friendName");
        root = FirebaseDatabase.getInstance().getReference().child(roomName);

    }

    private void load() {
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.setAdapter(adapter);
                messageList.scrollToPosition(messages.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Master.atRoom = 0;
        String message = messages.get(messages.size()-1).getMessage();
        String user = messages.get(messages.size()-1).getUserName();
        updateLastMessage(message,user,friendName);
        updateLastMessage(message,friendName,user);
    }



    private void rootAddChild() {

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConverSation(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConverSation(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void appendChatConverSation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            chat_user_name = (String)((DataSnapshot)i.next()).getValue();
            chat_msg = (String)((DataSnapshot)i.next()).getValue();
            BaseMessage message = new BaseMessage(chat_user_name,chat_msg);
            messages.add(message);
        }

    }

    private void sendClick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp_key = root.push().getKey();
                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map = new HashMap<String, Object>();
                String message = etMessage.getText().toString().trim();
                map.put("name",userName);
                map.put("msg",message);
                message_root.updateChildren(map);
                etMessage.setText("");
                adapter.notifyDataSetChanged();
                messageList.smoothScrollToPosition(messages.size());
                ChatMessage chatMessage = new ChatMessage("chat", userName, friendName, message);
                String chatMessageJson = new Gson().toJson(chatMessage);
                chatWebSocket.send(chatMessageJson);
                Log.d(TAG, "output: " + chatMessageJson);
            }
        });
    }

    public class MessageListAdapter extends RecyclerView.Adapter{

        private static final int VIEW_TYPE_MESSAGE_SENT = 1;
        private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

        Context context;
        List<BaseMessage> baseMessages;

        public MessageListAdapter(Context context, List<BaseMessage> baseMessages) {
            this.context = context;
            this.baseMessages = baseMessages;
        }

        @Override
        public int getItemViewType(int position) {
            BaseMessage message = (BaseMessage) baseMessages.get(position);

            String name1 = message.getUserName();
            String name2 = userName;
            Boolean isEquals = name1.equals(name2);

            if(isEquals){
                return VIEW_TYPE_MESSAGE_SENT;
            }else{
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            switch (viewType){
                case VIEW_TYPE_MESSAGE_SENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent_item,parent,false);
                    return new SentMessageHolder(view);
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received_item,parent,false);
                    return new ReceivedMessageHolder(view);
            }
            return null;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            BaseMessage message = baseMessages.get(position);

            switch (holder.getItemViewType()){
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder)holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder)holder).bind(message);
                    break;
            }

        }



        private class SentMessageHolder extends RecyclerView.ViewHolder{
            TextView sentUser,sentMessage;

            public SentMessageHolder(View itemView) {
                super(itemView);
                sentUser = itemView.findViewById(R.id.sent_name);
                sentMessage = itemView.findViewById(R.id.sent_message);
            }

            void bind(BaseMessage message){

                sentUser.setText(message.getUserName());
                sentMessage.setText(message.getMessage());

            }
        }

        private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
            TextView receivedUser,receivedMessage;

            public ReceivedMessageHolder(View itemView) {
                super(itemView);
                receivedUser = itemView.findViewById(R.id.received_name);
                receivedMessage = itemView.findViewById(R.id.received_message);
            }
            void bind(BaseMessage message){

                receivedUser.setText(message.getUserName());
                receivedMessage.setText(message.getMessage());

            }
        }



        @Override
        public int getItemCount() {
            return baseMessages.size();
        }
    }


    private int updateLastMessage(String last_message, String user_id, String room_name) {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "updateLastMessage");
            jsonObject.addProperty("last_message",last_message);
            jsonObject.addProperty("user_id", user_id );
            jsonObject.addProperty("room_name",room_name);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0 ) {
                Log.d(TAG,"新增 last message 失敗");
                return id;
            } else {
                Log.d(TAG,"新增 last message 成功");
                return  id;
            }
        } else {
            Log.d(TAG,"沒有網路");
            Common.showToast(getContext(), "no network");
            return 0;
        }
    }
}
