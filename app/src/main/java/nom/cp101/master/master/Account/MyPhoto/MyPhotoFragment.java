package nom.cp101.master.master.Account.MyPhoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

public class MyPhotoFragment extends Fragment {

    public static String URL_INTENT = "/UserInfo";
    private static final String TAG = "MyPhotoFragment";
    private MyTask task;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_my_photo_frag,container,false);
        recyclerView = view.findViewById(R.id.photoRecyclerView);

        // 拿到 User 帳號
        String userAccount = Common.getUserName(getActivity());

        // 拿到照片ID...
        ArrayList<String> userAllPostId = getUserAllPostId(userAccount);


        // 判斷長度為 0 的方法


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        // 開始 Recycler View
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4, GridLayoutManager.VERTICAL,false));

        // 傳進去!!
        recyclerView.setAdapter(new rvAdapter(getContext(),userAllPostId));
        return view;
    }


    // 拿到使用者發過的所有文章ID

    private ArrayList<String> getUserAllPostId(String userAccount) {
        String url = Common.URL + URL_INTENT;
        ArrayList<String> result = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getUserAllPostId");
        jsonObject.addProperty("account", userAccount);
        task = new MyTask(url, jsonObject.toString());
        try {
            String jsonIn = task.execute().get();
            Log.d(TAG, "Input: "+jsonIn);
            result = new Gson().fromJson(jsonIn, ArrayList.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }




    // Adapter Start

    public class rvAdapter extends RecyclerView.Adapter<rvAdapter.MyViewHolder>{

        private Context context;
        private ArrayList<String> userAllPostId;

        public rvAdapter(Context context, ArrayList<String> userAllPostId) {
            this.context = context;
            this.userAllPostId = userAllPostId;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.photo_item);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.account_photo_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String postId = userAllPostId.get(position);

            // 拿到圖片
            Bitmap bitmap = getUserPostPhoto(postId);
            // 是否真的有拿到圖片
            if (bitmap != null) {
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.account_bulldog);
            }
            // 為圖片加上點擊事件
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    
                    // 點擊跳出文章資訊 ? ...


                }
            });

        }


        @Override
        public int getItemCount() {
            return userAllPostId.size();
        }


        // 拿到相片牆圖片
        private Bitmap getUserPostPhoto(String postId) {
            String url = Common.URL + URL_INTENT;
            String userPhotoBase64 = null;
            Bitmap bitmap = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getUserPostPhoto");
            jsonObject.addProperty("postId",postId);
            task = new MyTask(url, jsonObject.toString());
            try {
                String jsonIn = task.execute().get();
                Log.d(TAG, "Input: "+jsonIn);
                userPhotoBase64 = new Gson().fromJson(jsonIn, String.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (userPhotoBase64 != null || userPhotoBase64 != "") {
                byte[] userPhoto = Base64.decode(userPhotoBase64,Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length);
            }
            return bitmap;
        }

    }  // Adapter End


} // MyPhotoFragment End
