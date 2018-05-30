package nom.cp101.master.master.Account.MyAccount;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import nom.cp101.master.master.Main.Common;
import nom.cp101.master.master.Main.MyTask;
import nom.cp101.master.master.R;

import static nom.cp101.master.master.Main.Common.showToast;

public class UserProfessionAdapter extends RecyclerView.Adapter<UserProfessionAdapter.ViewHolder> {
    private static String URL_INTENT = "/UserInfo";
    private static final String TAG = "UserProfessionAdapter";
    private List<User> listUserProfession;
    private String userAccount;
    private Context userContext;
    private MyTask task;
    RelativeLayout test;

    class ViewHolder extends RecyclerView.ViewHolder {
        private View myView;
        private TextView showProfession,deleteProfession;

        public ViewHolder(View view) {
            super(view);
            myView = view;
            showProfession = view.findViewById(R.id.user_tv_profession_item);
            deleteProfession = view.findViewById(R.id.user_tv_profession_delete);
            test = view.findViewById(R.id.user_layout_profession);
        }
    }

    // 刪除 Item 方法 (非DB處理)
    public void removeItem(int position){
        listUserProfession.remove(position);
        notifyDataSetChanged(); // 刷新畫面
    }

    // 增加 Item 方法 (非DB處理)
    public void addItem(User user) {
        listUserProfession.add(listUserProfession.size(),user);
        notifyItemInserted(listUserProfession.size());
    }

    // 建構子啦 = = 傳進帳號是用來加上刪除DB專業事件
    public UserProfessionAdapter(List<User> list, String account, Context context) {
        listUserProfession = list;
        userAccount = account;
        userContext = context;
    }

    // 用來拿出目前擁有的專業, 比對是否重複
    public List<User> getListUserProfession () {
        return listUserProfession;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profession_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public int getItemCount() {
        return listUserProfession.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = listUserProfession.get(position);
        holder.showProfession.setText(user.getUserProfession());
        final int itempos = position;
        final String text = user.getUserProfession();

        // 若傳進來的物件 getDelete 有接到東西則執行
        if (user.getDelete() != null) {
            // 改變專業名稱的 UI
            holder.showProfession.setGravity(Gravity.CENTER_VERTICAL);
            holder.showProfession.setPadding(50,0,0,0);
            holder.showProfession.setTextSize(20);
            holder.deleteProfession.setTextSize(20);
            // 拿出 Delete
            holder.deleteProfession.setText(user.getDelete());
            // 並將 Delete 文字加上點擊事件的刪除專業方法
            holder.deleteProfession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 刪除畫面資料(非DB資料 刪除畫面資料 DB還是存在所以 ... )
                    removeItem(itempos);
                    // 傳進會員帳號, 專業名稱, 並呼叫DB刪除該筆資料
                    int result = deleteProfession(userAccount ,text);
                    if (result != 0){
                        showToast(userContext, userContext.getResources().getString(R.string.success));
                    } else {
                        showToast(userContext, userContext.getResources().getString(R.string.failed));
                    }
                }
            });
        }
    }


    // 刪除會員專業
    public int deleteProfession(String account, String profession) {
        int editResult = 0;
        String url = Common.URL + URL_INTENT; // .........
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "deleteProfession");
        jsonObject.addProperty("account",account);
        jsonObject.addProperty("profession", profession);
        task = new MyTask(url, jsonObject.toString());
        try {
            editResult = Integer.valueOf(task.execute().get());
        } catch (Exception e) {}
        return editResult;
    }

}
