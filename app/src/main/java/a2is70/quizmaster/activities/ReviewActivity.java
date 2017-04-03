package a2is70.quizmaster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.adapters.DerpData;
import a2is70.quizmaster.activities.adapters.StudentAdapter;
import a2is70.quizmaster.activities.adapters.TeacherAdapter;
import a2is70.quizmaster.data.Account;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recView;
    private TeacherAdapter tAdapter;
    private StudentAdapter sAdapter;
    private Account.Type typ = Account.Type.STUDENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recView = (RecyclerView)findViewById(R.id.rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));

        tAdapter = new TeacherAdapter(DerpData.getListData(),DerpData.getCompletionRate(),this);
        sAdapter = new StudentAdapter(DerpData.getListData(),DerpData.getCorrect(),this);
        switch (typ) {
            case STUDENT:
                recView.setAdapter(sAdapter);
                break;
            case TEACHER:
                recView.setAdapter(tAdapter);
                break;
        }
    }
}
