package nom.cp101.master.master.ExperienceArticle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import nom.cp101.master.master.R;

/**
 * Created by yujie on 2018/4/20.
 */

public class ExperienceArticleViewPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //將Article列表內的ViewPager每一頁面呈現的frag實體化
        View view=inflater.inflate(R.layout.experience_article_recyclerview_viewpager_frag,container,false);
        //取得傳進的每一張圖片id,將置入ViewPager內
        int experienceViewPagerDataImg=getArguments().getInt("experienceArticleViewPagerDataImg");

        ImageView iv_experience=view.findViewById(R.id.ivExperienceArticle);

        iv_experience.setImageResource(experienceViewPagerDataImg);
        return view;
    }


}
