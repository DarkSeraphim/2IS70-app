package a2is70.quizmaster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        quiz = QuizAdapter.getQuiz();
        SubmittedQuiz subQuiz = DerpData.subQuizje;

        recView = (RecyclerView)findViewById(R.id.rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));




        switch (typ) {
            case STUDENT:
                sAdapter = new StudentAdapter(subQuiz,this);
                title.setText(subQuiz.getQuiz().getName());
                recView.setAdapter(sAdapter);
                rLay.removeView(findViewById(R.id.textViewCompletedLabel));
                rLay.removeView(findViewById(R.id.textViewAverageLabel));
                rLay.removeView(findViewById(R.id.textViewMaximumLabel));
                rLay.removeView(findViewById(R.id.textViewMinimumLabel));
                rLay.removeView(findViewById(R.id.textViewCompleted));
                rLay.removeView(findViewById(R.id.textViewAverage));
                rLay.removeView(findViewById(R.id.textViewMaximum));
                rLay.removeView(findViewById(R.id.textViewMinimum));
                Log.d("akjsgd",""+sAdapter.getItemCount());
                break;
            case TEACHER:
                tAdapter = new TeacherAdapter(quiz,DerpData.getSuccRate(),this);
                title.setText(quiz.getName());
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
