package nom.cp101.master.master;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yujie on 2018/4/22.
 */
//文章列表內專業類別選項與GridView的橋接器
public class ArticleGridViewAdapter extends BaseAdapter {
    //MasterActivity.this
    Context context;
    //專業類別陣列,其用於類別點選之圖片與名稱
    List<ArticleViewPagerData> articleViewPagerDataList;

    public ArticleGridViewAdapter(Context context) {
        this.context = context;
        this.articleViewPagerDataList = ArticleData.takeArticleViewPagerDataList();
    }

    //依照專業類別筆數實作次數
    @Override
    public int getCount() {
        return articleViewPagerDataList.size();
    }


    @Override
    public Object getItem(int position) {
        return articleViewPagerDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //當convertView為null時,實作置入gridView的item-xml檔
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.article_recyclerview_gridview_item, parent, false);
        }
        //依照position抓取顯示的專業類別內之數據
        ArticleViewPagerData articleViewPagerData = articleViewPagerDataList.get(position);


        ImageView iv_gridView = (ImageView) convertView.findViewById(R.id.iv_gridView);
        TextView tv_gridView = (TextView) convertView.findViewById(R.id.tv_gridView);
        //取得螢幕寬度的四分之一,並將此item的長同樣設置
        int w = context.getResources().getDisplayMetrics().widthPixels / 4;
        convertView.setMinimumHeight(w);
        //設置專業類別的圖與名稱
        iv_gridView.setImageResource(articleViewPagerData.getProjectImg());
        tv_gridView.setText(articleViewPagerData.getProjectName());
        //取得放置item的ViewGroup-GridView
        //取得LayoutParams,可獲得關於GridView的訊息內容
        ViewGroup.LayoutParams params = parent.getLayoutParams();
        //將gridView的高度設為從螢幕解析出來寬度/4設給各item使用的高度,取得＊2給予GirdView使用
        //直接將抓取的高度社給params.height即可採用
        //不太優的用法
        params.height = w * 2;


        return convertView;
    }
}
