package nom.cp101.master.master.CourseArticleActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.R;

//rvCategory搭載在CourseArticleCategoryFragment上與之橋接器
class CourseProfessionAdapter extends RecyclerView.Adapter<CourseProfessionAdapter.ViewHolder> {
    Context context;
    FragmentManager fm;
    //指定類別的所有項目
    List<String> professionItemList;

    public CourseProfessionAdapter(Context context, FragmentManager fm, List<String> professionItemList) {
        this.context = context;
        this.fm = fm;
        this.professionItemList = professionItemList;
    }

    @NonNull
    @Override
    public CourseProfessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_profession_item_item, parent, false);
        View view=null;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseProfessionAdapter.ViewHolder holder, int position) {
        //取得點選之各類別專業項目
        String professionItemName = professionItemList.get(position);

//        holder.layoutCategory.setBackgroundResource(courseArticleCategoryData.getProjectImg());
        holder.tvItemCourse.setText(professionItemName);
        //取得一半的螢幕寬度,並將數據給予搭載的cardView設定
//        int w = (context.getResources().getDisplayMetrics().widthPixels / 2);
//        ViewGroup.LayoutParams params = holder.cvItemCourse.getLayoutParams();
//        params.height = w;
//        params.width = w;

        //點擊指定之專業項目,顯示關於項目之所有課程
        holder.cvItemCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //若想切換至指定項目之所有課程頁面,需透過setArguments()將項目名稱帶入bundle傳遞
//                CourseProfessionItemFragment courseProfessionItemFragment = new CourseProfessionItemFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("projectName", courseArticleCategoryData.getProjectName());
//                courseProfessionItemFragment.setArguments(bundle);
//                //並要讓他可返回上一個CourseArticleCategoryFragment
//                FragmentTransaction ft = fm.beginTransaction();
//                //fragment轉場動畫
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//                ft.addToBackStack(null);
//                ft.replace(R.id.layoutCategory, courseProfessionItemFragment).commit();

            }
        });
    }

    //實作該類別需顯示之項目筆數
    @Override
    public int getItemCount() {
        return professionItemList.size();
    }

    //使用到之view包裝成ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItemCourse;
        TextView tvItemCourse;

        public ViewHolder(View itemView) {
            super(itemView);
//            cvItemCourse = (CardView) itemView.findViewById(R.id.cvItemCourse);
//            tvItemCourse = (TextView) itemView.findViewById(R.id.tvItemCourse);
        }
    }


}
