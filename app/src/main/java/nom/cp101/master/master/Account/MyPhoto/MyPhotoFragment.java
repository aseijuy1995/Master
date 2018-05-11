package nom.cp101.master.master.Account.MyPhoto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

import nom.cp101.master.master.R;

/**
 * Created by chunyili on 2018/4/17.
 */

public class MyPhotoFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_my_photo_frag,container,false);
        recyclerView = view.findViewById(R.id.photoRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4, GridLayoutManager.VERTICAL,false));
        List<String> strings = new ArrayList<String>();
        strings = getStrings();
        recyclerView.setAdapter(new rvAdapter(getContext(),strings));
        return view;
    }

    private List<String> getStrings() {
        String string = "123";
        List<String> strings = new ArrayList<String>();
        for(int i = 0;i < 50;i++){
            strings.add(string);
        }
        return strings;
    }

    public class rvAdapter extends RecyclerView.Adapter<rvAdapter.MyViewHolder>{

        private Context context;
        private List<String> strings;

        public rvAdapter(Context context, List<String> strings) {
            this.context = context;
            this.strings = strings;
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
            String string = strings.get(position);
            holder.imageView.setImageResource(R.drawable.account_bulldog);
        }


        @Override
        public int getItemCount() {
            return strings.size();
        }
    }


}
