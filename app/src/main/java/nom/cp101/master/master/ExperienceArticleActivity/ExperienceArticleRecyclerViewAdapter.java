package nom.cp101.master.master.ExperienceArticleActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/5/3.
 */

//心得文章連接留言的橋接器
public class ExperienceArticleRecyclerViewAdapter extends RecyclerView.Adapter<ExperienceArticleRecyclerViewAdapter.ViewHolder> {
    Context context;

    public ExperienceArticleRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.experience_article_act_leave_msg,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
