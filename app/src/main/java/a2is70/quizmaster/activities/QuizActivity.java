package a2is70.quizmaster.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.data.SubmittedQuiz;
import a2is70.quizmaster.database.DBInterface;

public class QuizActivity extends AppCompatActivity {

    Quiz quiz;
    List<Question> questions;
    int track;
    int prevTrack;
    RadioButton answerA;
    RadioButton answerB;
    RadioButton answerC;
    RadioButton answerD;
    TextView questiontext;
    Question currentQ;
    SubmittedQuiz submission;
    SubmittedQuiz.Answer[] submittedAnswers;
    RadioGroup answerbuttons;
    Boolean giventimelimit;
    ProgressBar prgrbar;

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

        //radiogroup
        answerbuttons = (RadioGroup) findViewById(R.id.answer_buttons);
        //radiobuttons
        answerA = (RadioButton) findViewById(R.id.question_closed_A);
        answerB = (RadioButton) findViewById(R.id.question_closed_B);
        answerC = (RadioButton) findViewById(R.id.question_closed_C);
        answerD = (RadioButton) findViewById(R.id.question_closed_D);

        //question
        questiontext = (TextView) findViewById(R.id.question_closed_text);

        //Quiz data comes from overview activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) { //if quiz was passed
            //key of json moet met 'quiz' string gepassed worden
            quiz = new Gson().fromJson(extras.getString("quiz"), Quiz.class);

            questions = quiz.getQuestions();

            //first question
            track = 0;
            currentQ = questions.get(track);

            //set all text
            questiontext.setText(currentQ.getText());

            Question.Answer[] answers = currentQ.getAnswers();

            answerA.setText(answers[0].getText());
            answerB.setText(answers[1].getText());
            answerC.setText(answers[2].getText());
            answerD.setText(answers[3].getText());

            //progress bar
            prgrbar = (ProgressBar) findViewById(R.id.question_closed_progress);

            //TODO configure progress bar
            if(quiz.getTimeLimit() < 0){ //no time limit means it's set to -1
                giventimelimit = false;
                prgrbar.setMax(quiz.getQuestions().size());
                prgrbar.setProgress(track);
            } else { //time limit given
                giventimelimit = true;
                int timelimit = quiz.getTimeLimit();
                prgrbar.setMax(timelimit);
                prgrbar.setProgress(0);
            }
        } else {
            //no data; pop up to go back to overview activity
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("ERROR: No quiz data found.")
                    .setCancelable(false)
                    .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //go back to overview activity
                            Intent intent = new Intent(QuizActivity.this, OverviewActivity.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void nextQuestion(){
        //if last question: popup and redirect to results activity
        if (track == questions.size()-1) {       //last question
            AlertDialog.Builder exitpopup = new AlertDialog.Builder(this);
            exitpopup.setMessage("This was the last question. Click Submit to continue or click Cancel if you want to change some answers");
            exitpopup.setCancelable(true);

            exitpopup.setPositiveButton(
                    "Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //save data and submit to DB, refer to results activity
                            toResults();
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
            prevTrack = track;
            track++;
            reload();
        }
    }

    private void previousQuestion(){
        //if first question do nothing
        if (track == 0) {
            //popup
            AlertDialog.Builder exitpopup = new AlertDialog.Builder(this);
            exitpopup.setMessage("This is the first question, you can not go to a previous question. Click cancel to continue looking at the questions. Click exit to go to your overview screen (Answers will not be saved)");
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
            prevTrack = track;
            track--;
            reload();
        }
    }

    private void reload(){
        //save the current answer to array
        int checkedAnswer = answerbuttons.getCheckedRadioButtonId();
        RadioButton checkedButton = (RadioButton) findViewById(checkedAnswer);
        for (Question.Answer answer : currentQ.getAnswers()){
            if (answer.getText().equals(checkedButton.getText())){
                submittedAnswers[prevTrack] = new SubmittedQuiz.Answer(answer.getId(), currentQ, answer, answer.getText());
                break;
            }
        }
        //reload everything with new data
        currentQ = questions.get(track);

        //set all text
        questiontext.setText(currentQ.getText());

        Question.Answer[] answers = currentQ.getAnswers();

        answerA.setText(answers[0].getText());
        answerB.setText(answers[1].getText());
        answerC.setText(answers[2].getText());
        answerD.setText(answers[3].getText());

        //uncheck all boxes
        answerbuttons.clearCheck();

        //update progress bar if no time limit is given
        if (!giventimelimit){
            prgrbar.setProgress(track);
        }
    }

    private void toResults(){
        //create submittedquiz objects with given answers
        SubmittedQuiz finalQuiz = new SubmittedQuiz(quiz);
        finalQuiz.setAnswers(submittedAnswers);

        //submit finalquiz to DB
        DBInterface dbi = AppContext.getInstance().getDBI();
        dbi.submitQuiz(finalQuiz);

        //go to results activity
        //pass submittedquiz as extra
        Intent intent = new Intent(QuizActivity.this, ReviewActivity.class);
        intent.putExtra("quiz", new Gson().toJson(finalQuiz));
        startActivity(intent);
    }
}
