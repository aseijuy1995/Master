package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nom.cp101.master.master.Account.MyAccount.User;
import nom.cp101.master.master.ExperienceArticle.ExperienceDialogFragment;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/7/20.
 */

public class CourseUsersAdapter extends RecyclerView.Adapter<CourseUsersAdapter.ViewHolder> {
    private final String TAG = "CourseUsersAdapter";
    private Context context;
    private FragmentManager fm;
    private List<User> userList;
    private LruCache<String, Bitmap> lruCache;

    private final String DIALOG_TAG = "dialog";

    public CourseUsersAdapter(Context context, FragmentManager fm, List<User> userList) {
        this.context = context;
        this.fm = fm;
        this.userList = userList;
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

    @NonNull
    @Override
    public CourseUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_users_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseUsersAdapter.ViewHolder holder, int position) {
        final User user = userList.get(position);
        Bitmap bitmap = null;

        bitmap = lruCache.get(user.getUserId());
        if (bitmap != null) {
            holder.civCourse.setImageBitmap(bitmap);
            Log.d(TAG, "load from cache " + user.getUserId());

        } else {
            //因解決RecyclerView錯位問題,在加載user圖片時,給予初始照
            holder.civCourse.setImageResource(R.drawable.user);

            bitmap = ConnectionServer.getPhotoByUserId(user.getUserId());
            if (bitmap != null) {
                holder.civCourse.setImageBitmap(bitmap);
                lruCache.put(user.getUserId(), bitmap);
                Log.d(TAG, "load from server " + user.getUserId());

            }
        }
        holder.tvCourse.setText(user.getUserName());

        holder.civCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentByTag(DIALOG_TAG);
                if (fragment != null) {
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new ExperienceDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", user.getUserId());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(ft, DIALOG_TAG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civCourse;
        TextView tvCourse;

        public ViewHolder(View itemView) {
            super(itemView);
            civCourse = (CircleImageView) itemView.findViewById(R.id.civCourse);
            tvCourse = (TextView) itemView.findViewById(R.id.tvCourse);
        }
    }
}
