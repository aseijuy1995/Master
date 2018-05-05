package nom.cp101.master.master.ExperienceArticleActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import nom.cp101.master.master.R;

public class ExperienceArticleActivity extends AppCompatActivity {
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_article_tool);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbEAA);
        setSupportActionBar(toolbar);


        rv=(RecyclerView)findViewById(R.id.rvLeaveMsg);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ExperienceArticleRecyclerViewAdapter(this));

    }
}
