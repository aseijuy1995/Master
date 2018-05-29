package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/22.
 */
//文章列表內專業類別選項與GridView的橋接器
public class CourseProfessionAdapter extends BaseAdapter {
    Context context;
    FragmentManager fm;
    int[] profession_img;
    List<ProfessionData> professionDataList;

    public CourseProfessionAdapter(Context context, FragmentManager fm) {
        this.context = context;
        this.fm = fm;
    }

    //依照專業類別筆數實作次數
    @Override
    public int getCount() {
        if(professionDataList != null){
            return professionDataList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return professionDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_gridview_item, parent, false);
        }
        //依照position抓取顯示的專業類別內之數據
        final ProfessionData professionData = professionDataList.get(position);

        LinearLayout llGvCourse = (LinearLayout) convertView.findViewById(R.id.llGvCourse);
        ImageView ivGvCourse = (ImageView) convertView.findViewById(R.id.ivGvCourse);
        TextView tvGvCourse = (TextView) convertView.findViewById(R.id.tvGvCourse);

        int width = 0, height = 0;
        //取得屏幕長算出所需長度,以保不失真
        width = height = context.getResources().getDisplayMetrics().widthPixels / 4;
        convertView.setMinimumWidth(width);
        convertView.setMinimumHeight(height);

        ivGvCourse.setImageResource(profession_img[position]);
        tvGvCourse.setText(professionData.getProfession_category());
        //取得gridVie設置其高度
        ViewGroup.LayoutParams params = parent.getLayoutParams();
        params.height = height * 2;

        llGvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseProfessionFragment courseProfessionFragment = new CourseProfessionFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("professionData", professionData);
                courseProfessionFragment.setArguments(bundle);
                FragmentTransaction ft = fm.beginTransaction();
                //返回上個fragment
                ft.addToBackStack(null);
                fm.popBackStack();
                ft.replace(R.id.frameMaster, courseProfessionFragment).commit();
            }
        });

        return convertView;
    }

    public void setData() {
        this.profession_img = ConnectionServer.profession_img;
        this.professionDataList = ConnectionServer.getProfessionData();
    }


}
