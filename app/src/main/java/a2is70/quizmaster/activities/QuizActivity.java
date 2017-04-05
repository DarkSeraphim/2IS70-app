package a2is70.quizmaster.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;

public class QuizActivity extends AppCompatActivity {

    Quiz quiz;
    List<Question> questions;
    int track;
    RadioButton answerA;
    RadioButton answerB;
    RadioButton answerC;
    RadioButton answerD;
    TextView questiontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_closed);

        Button nextQuestionButton = (Button) findViewById(R.id.question_closed_next);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        Button prevQuestionButton = (Button) findViewById(R.id.question_closed_previous);
        prevQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousQuestion();
            }
        });

        //if image/audio: differentiate

        //radiobuttons
        answerA = (RadioButton) findViewById(R.id.question_closed_A);
        answerB = (RadioButton) findViewById(R.id.question_closed_B);
        answerC = (RadioButton) findViewById(R.id.question_closed_C);
        answerD = (RadioButton) findViewById(R.id.question_closed_D);

        //question
        questiontext = (TextView) findViewById(R.id.question_closed_text);

        //Quiz data comes from overview activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //key of json moet met 'quiz' string gepassed worden
            quiz = new Gson().fromJson(extras.getString("quiz"), Quiz.class);

            questions = quiz.getQuestions();

            //first question
            track = 0;
            Question q1 = questions.get(track);

            //set all text
            questiontext.setText(q1.getText());

            Question.Answer[] answers = q1.getAnswers();

            answerA.setText(answers[0].getText());
            answerB.setText(answers[1].getText());
            answerC.setText(answers[2].getText());
            answerD.setText(answers[3].getText());

            //TODO configure progress bar
            //if-else on timelimit given

            //progress bar
            ProgressBar prgrbar = (ProgressBar) findViewById(R.id.question_closed_progress);

        } else {
            //popup no data found
        }
    }

    private void nextQuestion(){
        //if last question: popup and redirect to results activity
        if (track == questions.size()) {       //last question
            AlertDialog.Builder exitpopup = new AlertDialog.Builder(this);
            exitpopup.setMessage("This was the last question. Click Submit to continue or click Cancel if you want to change some answers");
            exitpopup.setCancelable(true);

            exitpopup.setPositiveButton(
                    "Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //save data and submit to DB, refer to results activity
                        }
                    });

            exitpopup.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = exitpopup.create();
            alert.show();
        } else {
            //not last question
            track++;
            reload();
        }
    }

    private void previousQuestion(){
        //if first question do nothing
        if (track == 0) {
            //popup
            AlertDialog.Builder exitpopup = new AlertDialog.Builder(this);
            exitpopup.setMessage("This is the first question, you can not go to a previous question. Click cancel to continue looking at the questions. Click exit to go to your overview screen");
            exitpopup.setCancelable(true);

            exitpopup.setPositiveButton(
                    "Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //exit to the overview screen
                        }
                    });

            exitpopup.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = exitpopup.create();
            alert.show();
        } else {
            track--;
            reload();
        }
    }

    private void reload(){
        //TODO save the current answer to ...?

        //reload everything with new data

        Question currentQ = questions.get(track);

        //set all text
        questiontext.setText(currentQ.getText());

        Question.Answer[] answers = currentQ.getAnswers();

        answerA.setText(answers[0].getText());
        answerB.setText(answers[1].getText());
        answerC.setText(answers[2].getText());
        answerD.setText(answers[3].getText());
    }
}
