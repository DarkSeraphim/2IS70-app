package a2is70.quizmaster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.adapters.DerpData;
import a2is70.quizmaster.activities.adapters.StudentAdapter;
import a2is70.quizmaster.activities.adapters.TeacherAdapter;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.data.SubmittedQuiz;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView recView;
    private TeacherAdapter tAdapter;
    private StudentAdapter sAdapter;
    private SubmittedQuiz subQuiz;
    private Quiz quiz;
    private Account.Type typ = AppContext.getInstance().getAccount().getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        RelativeLayout rLay = (RelativeLayout)findViewById(R.id.activity_review);
        TextView title = (TextView)findViewById(R.id.review_title);

        //Quiz data comes from Quiz activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) { //if extras were passed
            quiz = new Gson().fromJson(extras.getString("quiz"), Quiz.class);
        } else {
            //@todo fill
            quiz = QuizAdapter.getQuiz();
        }
        recView = (RecyclerView)findViewById(R.id.rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));

        tAdapter = new TeacherAdapter(quiz,DerpData.getSuccRate(),this);
        sAdapter = new StudentAdapter(DerpData.getListData(),this);
        //title.setText(quiz.getQuiz().getName());
        title.setText(quiz.getName());
        switch (typ) {
            case STUDENT:
                recView.setAdapter(sAdapter);
                rLay.removeView(findViewById(R.id.textViewCompletedLabel));
                rLay.removeView(findViewById(R.id.textViewAverageLabel));
                rLay.removeView(findViewById(R.id.textViewMaximumLabel));
                rLay.removeView(findViewById(R.id.textViewMinimumLabel));
                rLay.removeView(findViewById(R.id.textViewCompleted));
                rLay.removeView(findViewById(R.id.textViewAverage));
                rLay.removeView(findViewById(R.id.textViewMaximum));
                rLay.removeView(findViewById(R.id.textViewMinimum));
                break;
            case TEACHER:
                recView.setAdapter(tAdapter);
                TextView temp0 = (TextView)findViewById(R.id.textViewCompleted);
                temp0.setText("10%");
                TextView temp1 = (TextView)findViewById(R.id.textViewAverage);
                temp1.setText("1.0");
                TextView temp2 = (TextView)findViewById(R.id.textViewMinimum);
                temp2.setText("0.1");
                TextView temp3 = (TextView)findViewById(R.id.textViewMaximum);
                temp3.setText("10.0");

                break;
        }
    }
}
