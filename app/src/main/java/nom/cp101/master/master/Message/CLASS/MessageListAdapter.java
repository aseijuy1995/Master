package nom.cp101.master.master.Message.CLASS;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.R;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<BaseMessage> baseMessages;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private LruCache<String, Bitmap> lruCache;
    private final String MSG_PROPERTY = "MSG_PROPERTY";


    public MessageListAdapter(Context context, List<BaseMessage> baseMessages) {
        this.context = context;
        this.baseMessages = baseMessages;
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

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = (BaseMessage) baseMessages.get(position);

        String name1 = message.getUserId();
        String name2 = Common.getUserName(context);

        if (name1.equals(name2)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.message_sent_item, parent, false);
            return new MessageListAdapter.SentMessageHolder(view);

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_received_item, parent, false);
            return new MessageListAdapter.ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseMessage message = baseMessages.get(position);
        Bitmap bitmap = null;

        //sent
        if (holder instanceof SentMessageHolder) {
            bitmap = lruCache.get(MSG_PROPERTY + message.getUserId());
            if (bitmap != null) {
                ((SentMessageHolder) holder).civSent.setImageBitmap(bitmap);

            } else {
                ((SentMessageHolder) holder).civSent.setImageResource(R.drawable.user);
                bitmap = ConnectionServer.getPhotoByUserId(message.getUserId());
                if (bitmap != null) {
                    ((SentMessageHolder) holder).civSent.setImageBitmap(bitmap);
                    lruCache.put(MSG_PROPERTY + message.getUserId(), bitmap);
                }
            }
            ((SentMessageHolder) holder).sentUser.setText(ConnectionServer.findUserNameById(message.getUserId()));
            ((SentMessageHolder) holder).sentMessage.setText(message.getMessage());

            //received
        } else {
            bitmap = lruCache.get(MSG_PROPERTY + message.getUserId());
            if (bitmap != null) {
                ((ReceivedMessageHolder) holder).civReceived.setImageBitmap(bitmap);

            } else {
                ((ReceivedMessageHolder) holder).civReceived.setImageResource(R.drawable.user);
                bitmap = ConnectionServer.getPhotoByUserId(message.getUserId());
                if (bitmap != null) {
                    ((ReceivedMessageHolder) holder).civReceived.setImageBitmap(bitmap);
                    lruCache.put(MSG_PROPERTY + message.getUserId(), bitmap);
                }
            }
            ((ReceivedMessageHolder) holder).receivedUser.setText(ConnectionServer.findUserNameById(message.getUserId()));
            ((ReceivedMessageHolder) holder).receivedMessage.setText(message.getMessage());
        }

    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        CircleImageView civSent;
        TextView sentUser, sentMessage;

        public SentMessageHolder(View itemView) {
            super(itemView);
            civSent = itemView.findViewById(R.id.civSent);
            sentUser = itemView.findViewById(R.id.sent_name);
            sentMessage = itemView.findViewById(R.id.sent_message);
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        CircleImageView civReceived;
        TextView receivedUser, receivedMessage;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            civReceived = itemView.findViewById(R.id.civReceived);
            receivedUser = itemView.findViewById(R.id.received_name);
            receivedMessage = itemView.findViewById(R.id.received_message);
        }
    }

    @Override
    public int getItemCount() {
        return baseMessages.size();
    }
}
