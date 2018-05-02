package nom.cp101.master.master.CourseArticle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nom.cp101.master.master.CourseArticleActivity.CourseArticleActivity;
import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/22.
 */
//文章列表內專業類別選項與GridView的橋接器
public class CourseArticleGridViewAdapter extends BaseAdapter {
    //MasterActivity.this
    Context context;
    //專業類別陣列,其用於類別點選之圖片與名稱
    List<CourseArticleGridViewData> courseArticleGridViewDataList;

    public CourseArticleGridViewAdapter(Context context) {
        this.context = context;
        this.courseArticleGridViewDataList = CourseArticleAllData.takeArticleViewPagerDataList();
    }

    //依照專業類別筆數實作次數
    @Override
    public int getCount() {
        return courseArticleGridViewDataList.size();
    }


    @Override
    public Object getItem(int position) {
        return courseArticleGridViewDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //當convertView為null時,實作置入gridView的item-xml檔
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_article_recyclerview_gridview_item, parent, false);
        }
        //依照position抓取顯示的專業類別內之數據
        CourseArticleGridViewData courseArticleGridViewData = courseArticleGridViewDataList.get(position);

        LinearLayout layoutGridView = (LinearLayout) convertView.findViewById(R.id.layoutGridView);

        ImageView ivGridView = (ImageView) convertView.findViewById(R.id.ivGridView);
        TextView tvGridView = (TextView) convertView.findViewById(R.id.tvGridView);
        //取得螢幕寬度的四分之一,並將此item的長同樣設置
        int w = context.getResources().getDisplayMetrics().widthPixels / 4;
        convertView.setMinimumHeight(w);
        //設置專業類別的圖與名稱
        ivGridView.setImageResource(courseArticleGridViewData.getCategorytImg());
        tvGridView.setText(courseArticleGridViewData.getCategoryName());
        //取得放置item的ViewGroup-GridView
        //取得LayoutParams,可獲得關於GridView的訊息內容
        ViewGroup.LayoutParams params = parent.getLayoutParams();
        //將gridView的高度設為從螢幕解析出來寬度/4設給各item使用的高度,取得＊2給予GirdView使用
        //直接將抓取的高度社給params.height即可採用
        //不太優的用法
        params.height = w * 2;
        //自訂點擊專業類別method
        setCategoryClick(context, layoutGridView, context.getResources().getString(courseArticleGridViewData.getCategoryName()), convertView);

        return convertView;
    }

    //自訂點擊專業類別method
    private void setCategoryClick(final Context context, LinearLayout layoutGridView, final String categoryName, final View convertView) {
        //將選擇的專業類別名稱一併帶入CourseArticleActivity頁面
        layoutGridView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseArticleActivity.class);
                intent.putExtra("categoryName", categoryName);
                context.startActivity(intent);
                //設置轉場動畫,又出左進
                ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

}
