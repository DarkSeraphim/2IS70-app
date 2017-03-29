package a2is70.quizmaster.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Group;
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

        Button publishButton = (Button) findViewById(R.id.create_publish_button);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: store list
                publish();
            }
        });

        quizName = (EditText) findViewById(R.id.create_quiz_name);
        hasDeadline = (CheckBox) findViewById(R.id.create_has_deadline);

        date = (EditText) findViewById(R.id.create_deadline_date);
        time = (EditText) findViewById(R.id.create_deadline_time);

        hasDeadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                date.setEnabled(state);
                time.setEnabled(state);
            }
        });

        timeLimit = (EditText) findViewById(R.id.create_time_limit);
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
                        //CreateActivity.this.questions.add(new Question(question, answers, correct, weight));
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(R.layout.fragment_add_question)
                .show();
    }

    private void publish() {
        // TODO: grab groups
        final String[] foo = new ArrayList<String>() {{
            for (int i = 1; i <= 1000; i++) {
                add(String.format("Group %s", Integer.toHexString(i)));
            }
        }}.toArray(new String[0]);

        final Group[] groups = new Group[foo.length];
        final Set<Group> enabled = new LinkedHashSet<>();

        new AlertDialog.Builder(this)
                .setTitle("Publish quiz?")
                //.setMessage("Warning: This quiz cannot be edited once published")
                .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CreateActivity.this, "Quiz published", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateActivity.this, OverviewActivity.class);
                        Group[] groups = enabled.toArray(new Group[enabled.size()]);
                        Quiz quiz = new Quiz(quizName.getText().toString(), groups, null, questions);
                        intent.putExtra("questions", new Gson().toJson(quiz));
                        startActivity(intent);

                    }
                }).setNegativeButton("Cancel", null)
                .setMultiChoiceItems(foo, new boolean[foo.length], new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            enabled.add(groups[i]);
                        } else {
                            enabled.remove(groups[i]);
                        }
                    }
                })
                .show();

    }
}
