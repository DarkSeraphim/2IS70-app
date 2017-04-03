package a2is70.quizmaster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.adapters.DerpData;
import a2is70.quizmaster.activities.adapters.RecyclerAdapter;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recView = (RecyclerView)findViewById(R.id.rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerAdapter(DerpData.getListData(),DerpData.getCompletionRate(),this);
        recView.setAdapter(adapter);

    }
}
