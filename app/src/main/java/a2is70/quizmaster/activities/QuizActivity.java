package a2is70.quizmaster.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.data.SubmittedQuiz;
import a2is70.quizmaster.database.DBInterface;
import a2is70.quizmaster.utils.JsonConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    SubmittedQuiz.Answer[] submittedAnswers;
    List<SubmittedQuiz.Answer> subAnswers = new ArrayList<>();
    RadioGroup answerbuttons;
    Boolean giventimelimit;
    ProgressBar prgrbar;
    int[] checkedAnswers;

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
        prevQuestionButton.setVisibility(View.GONE);


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

        if (extras != null) { //if extras were passed
            //key of json moet met 'quiz' string gepassed worden
            quiz = JsonConverter.fromJson(extras.getString("quiz"), Quiz.class);

            Log.d("kei mooi",extras.getString("quiz"));

            questions = quiz.getQuestions();
            submittedAnswers = new SubmittedQuiz.Answer[questions.size()];
            checkedAnswers = new int[questions.size()];

            //first question
            track = 0;
            currentQ = questions.get(track);

            if (currentQ.getImage() != null) {
                //@todo test what this does
                View imView = new ImageView(this.getBaseContext());
                RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.activity_quiz);
                rLayout.addView(imView);
            }
            else if (currentQ.getAudio() != null) {//@todo audio?
                //MediaPlayer mediaPlayer= MediaPlayer.create(this, currentQ.getAudio().getFile(@todo what goes in here ));
            }

            //set all text
            questiontext.setText(currentQ.getText());

            Question.Answer[] answers = currentQ.getAnswers();

            answerA.setText(answers[0].getText());
            if(answers.length>1) {
                answerB.setText(answers[1].getText());
                if (answers.length>2) {
                    answerC.setText(answers[2].getText());
                    if(answers.length>3){
                    answerD.setText(answers[3].getText());
                }
            }
            }

            //progress bar
            prgrbar = (ProgressBar) findViewById(R.id.question_closed_progress);

            //configure progress bar
            if(quiz.getTimeLimit() < 0){ //no time limit means it's set to -1
                giventimelimit = false;
                prgrbar.setMax(quiz.getQuestions().size());
                prgrbar.setProgress(track);
            } else { //time limit given
                giventimelimit = true;
                int timelimit = quiz.getTimeLimit();
                prgrbar.setMax(timelimit * 60);
                new ProgressTask().execute();
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
                            prevTrack=track;
                            track++;
                            reload();
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
        //if this is not the first question, make previousQuestionButton appear again.
        if (track == 0) {
            Button prevQuestionButton = (Button) findViewById(R.id.question_closed_previous);
            prevQuestionButton.setVisibility(View.GONE);
        } else {
            Button prevQuestionButton = (Button) findViewById(R.id.question_closed_previous);
            prevQuestionButton.setVisibility(View.VISIBLE);
        }


        //save the current answer to array
        int checkedAnswer = answerbuttons.getCheckedRadioButtonId();
        checkedAnswers[prevTrack]=checkedAnswer;
        RadioButton checkedButton = (RadioButton) findViewById(checkedAnswer);
        if(checkedButton != null) {
            for (Question.Answer answer : currentQ.getAnswers()) {
                if (answer.getText().equals(checkedButton.getText())) {
                    //submittedAnswers[prevTrack] = new SubmittedQuiz.Answer(1, currentQ, answer, answer.getText());
                    subAnswers.add(prevTrack,new SubmittedQuiz.Answer(1, currentQ, answer, answer.getText()));
                    break;
                }
            }
        }else{
            subAnswers.add(prevTrack,new SubmittedQuiz.Answer(1, currentQ, null, "You didn't answer this one"));
        }

        //reload everything with new data
        if(!(track>(questions.size()-1))) {
            currentQ = questions.get(track);
        }else{
            currentQ = questions.get(prevTrack);
        }

        //set all text
        questiontext.setText(currentQ.getText());

        Question.Answer[] answers = currentQ.getAnswers();

        answerA.setText(answers[0].getText());
        if(answers.length>1) {
            answerB.setText(answers[1].getText());
            if (answers.length>2) {
                answerC.setText(answers[2].getText());
                if(answers.length>3){
                    answerD.setText(answers[3].getText());
                }
            }
        }

        //uncheck all boxes
        answerbuttons.clearCheck();

        //recheck if already answered
        if(!(track>(questions.size()-1))) {
            if (checkedAnswers[track] != 0) {
                switch (checkedAnswers[track]) {
                    case R.id.question_closed_A:
                        answerA.setChecked(true);
                        break;
                    case R.id.question_closed_B:
                        answerB.setChecked(true);
                        break;
                    case R.id.question_closed_C:
                        answerC.setChecked(true);
                        break;
                    case R.id.question_closed_D:
                        answerD.setChecked(true);
                        break;
                }
            }
        }

        //update progress bar if no time limit is given

        if (!giventimelimit){
            prgrbar.setProgress(track);
        }
    }



    private void toResults(){
        //create submittedquiz objects with given answers
        final SubmittedQuiz submission = new SubmittedQuiz(quiz);
        submission.setAnswers(subAnswers);

        //@todo test
        final DBInterface dbi = AppContext.getInstance().getDBI();
        dbi.submitQuiz(submission).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    //everything is gucci
                    //go to results activity
                    //pass submittedquiz as extra
                    dbi.reviewStudentQuiz(quiz.getID()).enqueue(new Callback<SubmittedQuiz>() {
                        @Override
                        public void onResponse(Call<SubmittedQuiz> call, Response<SubmittedQuiz> response) {
                            if (response.code() == 200) {
                                Intent intent = new Intent(QuizActivity.this, ReviewActivity.class);
                                intent.putExtra("quiz", JsonConverter.toJson(quiz));
                                intent.putExtra("subQuiz", JsonConverter.toJson(response.body()));
                                startActivity(intent);
                            } else {
                                Toast.makeText(QuizActivity.this, "reviewStudentQuiz: http" + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmittedQuiz> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(QuizActivity.this, "submitQuiz: http" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("submitquiz", "error: ", t);
            }
        });
    }



    class ProgressTask extends AsyncTask<Void, Void, Void>{
        int currentProgress = 0;
        int maxProgress;

        @Override
        protected void onPreExecute() {
            maxProgress = prgrbar.getMax();
            prgrbar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            do{
                //update progress bar : onprogressupdate
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            currentProgress++;
            publishProgress();
        } while (currentProgress < maxProgress);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            prgrbar.setProgress(currentProgress);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            toResults();
        }
    }
}
