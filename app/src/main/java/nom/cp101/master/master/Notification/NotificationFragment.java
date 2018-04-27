package nom.cp101.master.master.Notification;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.R;

public class NotificationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().setTitle(getString(R.string.nf_title));
        //啟用notificaiton_fragment
        View view = inflater.inflate(R.layout.notification_frag, container, false);
        //取得notificaiton_recycerview 需要的資料
        List<Notification_rv_item> items = getitems();
        //初始化notificaiton_recycerview
        RecyclerView nf_rv = view.findViewById(R.id.nf_recyclerview);
        //設定notificaiton_recycerview layout類型
        nf_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //載入notificaiton_recycerview adapter
        nf_rv.setAdapter(new nf_rv_adapter(items, getActivity()));
        return view;

    }

    private class nf_rv_adapter extends RecyclerView.Adapter<nf_rv_adapter.nf_rv_viewholder> {
        List<Notification_rv_item> items;
        Context context;
        //notificaiton_recycerview的position為0時,顯示notificaiton_title
        static final int TYPE_TITLE = 0;
        //notificaiton_recycerview的position為1時,顯示notificaiton_rv的內容
        static final int TYPE_ITEM = 1;

        public nf_rv_adapter(List<Notification_rv_item> items, Context context) {
            this.items = items;
            this.context = context;
        }


        public class nf_rv_viewholder extends RecyclerView.ViewHolder {
            //初始化view中的元件
            ImageView item_picture;
            TextView item_time, item_content;

            public nf_rv_viewholder(View itemView) {
                super(itemView);
                item_picture = itemView.findViewById(R.id.nf_itemview_picture);
                item_time = itemView.findViewById(R.id.nf_itemview_time);
                item_content = itemView.findViewById(R.id.nf_itemview_content);


            }
        }

        @Override
        public int getItemCount() {
            //position為0時為title, recyclerview內容從1開始顯示, itemcount需往後加1
            return items.size() + 1;
        }

        //定義recyclerview的viewtype,position等於0顯示title,其他為recyclerview內容
        @Override
        public int getItemViewType(int position) {

            if (position == 0) {
                return TYPE_TITLE;
            } else {
                return TYPE_ITEM;
            }
        }

        public nf_rv_viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemview;
            //判斷recyclerview的viewType
            if (viewType == TYPE_TITLE) {
                itemview = layoutInflater.inflate(R.layout.notification_rv_title, parent, false);
            } else {
                itemview = layoutInflater.inflate(R.layout.notification_rv_item, parent, false);
            }
            return new nf_rv_viewholder(itemview);
        }

        @Override
        public void onBindViewHolder(nf_rv_viewholder viewholder, int position) {


            if (position == 0) {

            } else {
                //將對應的position資料塞入view中
                Notification_rv_item item = items.get(position - 1);
                viewholder.item_picture.setImageResource(item.getPictureID());
                viewholder.item_time.setText(item.getTime());
                viewholder.item_content.setText(item.getContent());
//                click itemview 轉頁至文章
//                viewholder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });


            }


        }


    }

    //假資料,之後將會替換成database內容
    private List<Notification_rv_item> getitems() {
        List<Notification_rv_item> items = new ArrayList<>();
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "昨天"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        items.add(new Notification_rv_item("邊緣人", "某某某學員也想要參加你的揪團", R.drawable.picture, "五分鐘前"));
        return items;
    }
}
