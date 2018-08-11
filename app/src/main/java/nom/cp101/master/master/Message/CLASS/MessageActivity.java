package nom.cp101.master.master.Message.CLASS;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.chatWebSocket;
import static nom.cp101.master.master.Main.Common.showToast;

/**
 * Created by chunyili on 2018/5/12.
 */

public class MessageActivity extends AppCompatActivity implements ValueEventListener, ChildEventListener, View.OnClickListener {
    private ImageView ivBack;
    private TextView tvName;
    private RecyclerView rvMessage;
    private EditText etMessage;
    private ImageView ivSend;
    private MessageListAdapter messageListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String TAG = "Message Activity";
    private DatabaseReference root;
    //save talk by user and only one friend at all
    private ArrayList<BaseMessage> messages;
    private String roomName, temp_key, chat_msg, chat_user_id;

    private String user_id, friend_id;
    //    private String friendName, userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_content);
        messages = new ArrayList<>();
        findView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            roomName = bundle.getString("room_position");
            user_id = bundle.getString("user_id");
            friend_id = bundle.getString("friend_id");
            tvName.setText(ConnectionServer.findUserNameById(friend_id));

            initRecyclerView();
            load();
            root.addChildEventListener(this);
            ivSend.setOnClickListener(this);
            ivBack.setOnClickListener(this);
        }
    }

    private void findView() {
        ivBack = findViewById(R.id.ivBack);
        tvName = findViewById(R.id.tvName);
        rvMessage = findViewById(R.id.messageList);
        etMessage = findViewById(R.id.etMessage);
        ivSend = findViewById(R.id.ivSend);
    }

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

    private void appendChatConverSation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chat_user_id = (String) ((DataSnapshot) i.next()).getValue();
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            BaseMessage message = new BaseMessage(chat_user_id, chat_msg);
            messages.add(message);
        }
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        rvMessage.setLayoutManager(linearLayoutManager);
        rvMessage.setItemAnimator(new DefaultItemAnimator());
        messageListAdapter = new MessageListAdapter(this, messages);
        rvMessage.setAdapter(messageListAdapter);
    }

    private void load() {
        root = FirebaseDatabase.getInstance().getReference().child(roomName);
        root.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        messageListAdapter.notifyDataSetChanged();
        rvMessage.scrollToPosition(messages.size() - 1);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    @Override
    public void onPause() {
        super.onPause();
//        ChatReceiver.atRoom = 0;
//        String message = messages.get(messages.size() - 1).getMessage();
//        String user = messages.get(messages.size() - 1).getUserId();
//        updateLastMessage(message, user, friendName);
//        updateLastMessage(message, friendName, user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSend:
                temp_key = root.push().getKey();
                DatabaseReference message_root = root.child(temp_key);

                Map<String, Object> map = new HashMap<>();
                String message = etMessage.getText().toString().trim();
                if (message != null && !message.equals("")) {
                    map.put("name", user_id);
                    map.put("msg", message);
                    message_root.updateChildren(map);
                    messageListAdapter.notifyDataSetChanged();
                    rvMessage.smoothScrollToPosition(messages.size());
                    etMessage.setText("");


                    ChatMessage chatMessage = new ChatMessage("message_chat_offline",
                            ConnectionServer.findUserNameById(user_id),
                            ConnectionServer.findUserNameById(friend_id),
                            message);

                    String chatMessageJson = new Gson().toJson(chatMessage);
                    chatWebSocket.send(chatMessageJson);
                    Log.d(TAG, "output: " + chatMessageJson);
                    updateLastMessage(message, user_id, ConnectionServer.findUserNameById(friend_id));
                    updateLastMessage(message, friend_id, ConnectionServer.findUserNameById(user_id));

                    //close_keyboard
                    InputMethodManager imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                } else {
                    showToast(this, R.string.empty_meg);
                }

                break;


            case R.id.ivBack:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }


    private int updateLastMessage(String last_message, String user_id, String room_name) {
        if (Common.networkConnected(this)) {
            String url = Common.URL + "/chatRoomServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "updateLastMessage");
            jsonObject.addProperty("last_message", last_message);
            jsonObject.addProperty("user_id", user_id);
            jsonObject.addProperty("room_name", room_name);
            int id = 0;
            try {
                String result = new MyTask(url, jsonObject.toString()).execute().get();
                id = Integer.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (id == 0) {
                Log.d(TAG, "新增 last message 失敗");
                return id;
            } else {
                Log.d(TAG, "新增 last message 成功");
                return id;
            }
        } else {
            Log.d(TAG, "沒有網路");
            Common.showToast(this, "no network");
            return 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}