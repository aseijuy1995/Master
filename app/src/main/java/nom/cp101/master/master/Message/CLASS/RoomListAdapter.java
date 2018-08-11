package nom.cp101.master.master.Message.CLASS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.CourseArticle.ConnectionServer;
import nom.cp101.master.master.R;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.myViewHolder> {
    private Context context;
    private List<ChatRoom> chatRooms;
    private LruCache<String, Bitmap> lruCache;
    private final String IMG_PROPERTY = "IMG_PROPERTY";
    private String user_id, friend_id;

    public RoomListAdapter(Context context, List<ChatRoom> chatRooms, String user_id) {
        this.context = context;
        this.chatRooms = chatRooms;
        this.user_id = user_id;
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

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_property;
        TextView user_name, last_message;
        CardView chatRoomItem;

        myViewHolder(View itemView) {
            super(itemView);
            user_property = (CircleImageView) itemView.findViewById(R.id.user_property);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            last_message = (TextView) itemView.findViewById(R.id.last_message);
            chatRoomItem = (CardView) itemView.findViewById(R.id.chatRoomItem);
        }
    }

    @Override
    public RoomListAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_chat_room_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        final ChatRoom chatRoom = chatRooms.get(position);

        Bitmap bitmap = lruCache.get(IMG_PROPERTY + chatRoom.getFriend_user_id());
        if (bitmap != null) {
            holder.user_property.setImageBitmap(bitmap);

        } else {
            holder.user_property.setImageResource(R.drawable.user);
            bitmap = ConnectionServer.getPhotoByUserId(chatRoom.getFriend_user_id());
            if (bitmap != null) {
                holder.user_property.setImageBitmap(bitmap);
                lruCache.put(IMG_PROPERTY + chatRoom.getFriend_user_id(), bitmap);
            }
        }

        holder.user_name.setText(chatRoom.getRoom_name());

        if (chatRoom.getLast_message().equals("") || chatRoom.getLast_message() == null) {
            holder.last_message.setText("");

        } else {
            holder.last_message.setText(chatRoom.getLast_message());
        }

        holder.chatRoomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room_position = chatRoom.getRoom_position();
                friend_id = chatRoom.getFriend_user_id();
                enterChatRoom(room_position, user_id, friend_id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }


    private void enterChatRoom(String room_position, String user_id, String friend_id) {
        Bundle bundle = new Bundle();
        bundle.putString("room_position", room_position);
        bundle.putString("user_id", user_id);
        bundle.putString("friend_id", friend_id);
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}