package nom.cp101.master.master.CourseArticle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.R;

//接指定類別之所有項目名
public class CourseNiceSpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> professionItemList;

    public CourseNiceSpinnerAdapter(Context context, List<String> professionItemList) {
        this.context = context;
        this.professionItemList = professionItemList;
    }

    @Override
    public int getCount() {
        return professionItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return professionItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_profession_spinner_item, parent, false);
        }
        ImageView ivItemCourse = convertView.findViewById(R.id.ivItemCourse);
        TextView tvItemCourse = convertView.findViewById(R.id.tvItemCourse);

//        iv.setImageResource(i[position]);
        tvItemCourse.setText(professionItemList.get(position));

        return convertView;
    }
}

