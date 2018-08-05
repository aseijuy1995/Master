package nom.cp101.master.master.Account.MyAccount;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.R;

public class UserModifyProfessionAdapter extends RecyclerView.Adapter<UserModifyProfessionAdapter.ViewHolder> {
    private static List<User> listUserProfession;
    private static final int TYPE_PROFESSION = 0;
    private static final int TYPE_SELECT_PROFESSION = 1;
    int typeNumber = -1;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView showProfession;

        public ViewHolder(View view) {
            super(view);
            showProfession = view.findViewById(R.id.user_tv_profession_item);
        }
    }

    // 建構子
    public UserModifyProfessionAdapter(List<User> list, int typeNumber) {
        listUserProfession = list;
        this.typeNumber = typeNumber;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (typeNumber == TYPE_PROFESSION)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profession_item, parent, false);
        else if (typeNumber == TYPE_SELECT_PROFESSION)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profession_select_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = listUserProfession.get(position);
        holder.showProfession.setText(user.getUserProfession());
    }

    @Override
    public int getItemCount() {
        return listUserProfession.size();
    }

}
