package a2is70.quizmaster.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;

public class CreateActivity extends AppCompatActivity {

    private List<Question> questions = new ArrayList<>();

    private EditText quizName;

    private CheckBox hasDeadline;

    private EditText date;

    private EditText time;

    private EditText timeLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Button addQuestionButton = (Button) findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddQuestion();
            }
        });

        Button publishButton = (Button) findViewById(R.id.button8);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: store list
                publish();
            }
        });

        date = (EditText) findViewById(R.id.editText4);
        time = (EditText) findViewById(R.id.editText5);

        CheckBox deadline = (CheckBox) findViewById(R.id.checkBox3);
        deadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                date.setEnabled(state);
                time.setEnabled(state);
            }
        });

        timeLimit = (EditText) findViewById(R.id.editText7);
    }

    private void showAddQuestion() {
        new AlertDialog.Builder(CreateActivity.this)
                .setTitle("Add Quiz")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Fetch fields
                        // Create question
                        String question = "";
                        String[] answers = new String[0];
                        String correct = "";
                        double weight = 0;
                        CreateActivity.this.questions.add(new Question(question, answers, correct, weight));
                    }
                })
                .setNegativeButton("Cancel", null)
                //.setView()
                .show();
    }

    private void publish() {
        new AlertDialog.Builder(this)
                .setTitle("Publish quiz?")
                .setMessage("Warning: This quiz cannot be edited once published")
                .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(CreateActivity.this, CreateActivity.class);
                String group = "";
                Quiz quiz = new Quiz(quizName.getText().toString(), group, null, questions);
                intent.putExtra("questions", new Gson().toJson(quiz));
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", null);
    }
}
