package nom.cp101.master.master.Main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Locale;

/**
 * Created by chunyili on 2018/5/9.
 */

public class ChatWebSocket extends WebSocketClient {

    private static final String TAG = "ChatWebSocketClient";
    private LocalBroadcastManager broadcastManager;
    private Gson gson;

    ChatWebSocket(URI serverURI, Context context) {
        // Draft_17是連接協議，就是標準的RFC 6455（JSR256）
        super(serverURI, new Draft_17());
        broadcastManager = LocalBroadcastManager.getInstance(context);
        gson = new Gson();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        String text = String.format(Locale.getDefault(),
                "onOpen: Http status code = %d; status message = %s",
                handshakedata.getHttpStatus(),
                handshakedata.getHttpStatusMessage());
        Log.d(TAG,"onOpen "+text);
    }

    @Override
    public void onMessage(String message) {
        JsonObject jsonObject = gson.fromJson(message,JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        sendMessageBroadcast(type,message);
        Log.d(TAG,"onMessage: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        String text= String.format(Locale.getDefault(),"code = %d, reason = %s, remote = %b",
                code,reason,remote);
        Log.d(TAG,"onClose: " + text);
    }

    @Override
    public void onError(Exception ex) {
        Log.d(TAG, "onError: " + String.valueOf(ex));
    }

    private void sendMessageBroadcast(String messageType, String message) {
        Intent intent = new Intent(messageType);
        intent.putExtra("message", message);
        broadcastManager.sendBroadcast(intent);
    }
}
