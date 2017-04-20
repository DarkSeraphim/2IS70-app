package a2is70.quizmaster.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.utils.MediaCreator;
import a2is70.quizmaster.utils.function.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: allow question deletion
public class CreateActivity extends AppCompatActivity implements MediaCreator.ResultListener {

    private static class DateTimeHolder {
        private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);

        private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);

        private final TextView dateView;

        private final TextView timeView;

        private Calendar calendar;

        private DateTimeHolder(Activity activity, TextView dateView, TextView timeView) {
            this.dateView = dateView;
            this.timeView = timeView;
            this.calendar = Calendar.getInstance();
            this.dateView.setText(DATE_FORMAT.format(this.calendar.getTimeInMillis()));
            this.timeView.setText(TIME_FORMAT.format(this.calendar.getTimeInMillis()));
            setClickDate(activity, this.dateView);
            setClickTime(activity, this.timeView);
        }

        private void setClickDate(final Activity activity, TextView view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatePicker picker = new DatePicker(activity);
                    DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            setDate(year, monthOfYear, dayOfMonth);

                        }
                    };
                    picker.init(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                listener);

                    new AlertDialog.Builder(activity)
                            .setView(picker)
                            .setPositiveButton("Set", null).show();
                }
            });
        }

        private void setClickTime(final Activity activity, TextView view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TimePicker picker = new TimePicker(activity);
                    picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            setTime(hourOfDay, minute);
                        }
                    });
                    new AlertDialog.Builder(activity)
                            .setView(picker)
                            .setPositiveButton("Set", null).show();
                }
            });
        }

        public void setTime(int hours, int minutes) {
            this.calendar.set(Calendar.HOUR_OF_DAY, hours);
            this.calendar.set(Calendar.MINUTE, minutes);
            this.timeView.setText(TIME_FORMAT.format(this.getTime()));
        }

        public void setDate(int year, int month, int day) {
            this.calendar.set(Calendar.YEAR, year);
            this.calendar.set(Calendar.MONTH, month);
            this.calendar.set(Calendar.DAY_OF_MONTH, day);
            this.dateView.setText(DATE_FORMAT.format(getTime()));
        }

        public long getTime() {
            return this.calendar.getTimeInMillis();
        }
    }

    private int requestId;

    private SparseArray<Consumer<Result>> resultHandlers = new SparseArray<>();

    private List<Question> questions = new ArrayList<>();

    private EditText quizName;

    private CheckBox hasDeadline;

    private DateTimeHolder start;

    private DateTimeHolder deadline;

    private EditText timeLimit;

    private QuestionListAdapter questionListAdapter;

    private RecyclerView questionList;

    private boolean recordPermissionGranted;

    private static final String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};

    private Group[] groupsAccessible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
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
        questionList = (RecyclerView) findViewById(R.id.create_question_list);
        hasDeadline = (CheckBox) findViewById(R.id.create_has_deadline);

        final TextView startDate = (TextView) findViewById(R.id.create_start_date);
        final TextView startTime = (TextView) findViewById(R.id.create_start_time);

        deadline = new DateTimeHolder(this, startDate, startTime);

        final TextView deadlineDate = (TextView) findViewById(R.id.create_deadline_date);
        final TextView deadlineTime = (TextView) findViewById(R.id.create_deadline_time);

        deadline = new DateTimeHolder(this, deadlineDate, deadlineTime);

        hasDeadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                deadlineDate.setEnabled(state);
                deadlineTime.setEnabled(state);
            }
        });


        timeLimit = (EditText) findViewById(R.id.create_time_limit);

        questionListAdapter = new QuestionListAdapter();
        questionList = (RecyclerView) findViewById(R.id.create_question_list);
        questionList.setLayoutManager(new LinearLayoutManager(questionList.getContext()));
        questionList.setAdapter(questionListAdapter);
        questionListAdapter.notifyDataSetChanged();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("groups") != null) {
            groupsAccessible = new Gson().fromJson(bundle.getString("groups"), Group[].class);
        } else {
            throw new IllegalStateException("Missing 'groups' data in extra, requires a serialised Group[]");
        }
    }

    @Override
    public void startActivityForResult(Intent intent, Consumer<Result> resultConsumer) {
        int theId = requestId++;
        this.resultHandlers.put(theId, resultConsumer);
        this.startActivityForResult(intent, theId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Consumer<Result> resultConsumer = resultHandlers.get(requestCode);
        if (resultConsumer == null) {
            return;
        }
        resultHandlers.remove(requestCode);
        resultConsumer.accept(new Result(resultCode, data));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 0) {
            return;
        }

        boolean result = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                result = false;
                Log.w("Mark", "Missing permission: " + permissions[i]);
            }
        }
        recordPermissionGranted = result;
    }

    private void addQuestion(Question question) {
        this.questions.add(question);
        questionListAdapter.setItems(this.questions);
    }

    private void showAddQuestion() {
        class FileBox {
            public File file;
        }

        final FileBox image = new FileBox();
        final FileBox audio = new FileBox();

        final AlertDialog dialog = new AlertDialog.Builder(CreateActivity.this)
                .setTitle("Add Question")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Fetch fields
                        // Create question


                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(R.layout.fragment_add_question)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog view = ((AlertDialog) dialog);
                        String text = ((EditText) view.findViewById(R.id.add_question_text)).getEditableText().toString();
                        boolean open = ((RadioButton)view.findViewById(R.id.add_question_radio_open)).isChecked();
                        Question.Answer[] answers = new Question.Answer[] {
                                new Question.Answer(((EditText) view.findViewById(R.id.add_question_answer1)).getText().toString()),
                                new Question.Answer(((EditText) view.findViewById(R.id.add_question_answer2)).getText().toString()),
                                new Question.Answer(((EditText) view.findViewById(R.id.add_question_answer3)).getText().toString()),
                                new Question.Answer(((EditText) view.findViewById(R.id.add_question_answer4)).getText().toString()),
                        };
                        if (open) {
                            answers = new Question.Answer[] {answers[0]};
                        }
                        Question.Answer correct = answers[0];
                        EditText weightText = (EditText) view.findViewById(R.id.add_question_weight);
                        int weight;
                        try {
                            weight = Integer.parseInt(weightText.getText().toString());
                        } catch (NumberFormatException ex) {
                            weightText.setError("Not a number");
                            return;
                        }
                        CreateActivity.this.addQuestion(new Question(text, answers, correct, weight, image.file, audio.file));
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();

        final Button takeImageButton = (Button) dialog.findViewById(R.id.add_question_image_button);
        assert takeImageButton != null;
        takeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaCreator.PhotoCreator.of(CreateActivity.this, new Consumer<File>() {
                    @Override
                    public void accept(File value) {
                        image.file = value;
                        takeImageButton.setText("Retake photo");
                    }
                }).start();
            }
        });
        final Button audioRecordButton = (Button) dialog.findViewById(R.id.add_question_audio_button);
        assert audioRecordButton != null;
        audioRecordButton.setOnClickListener(new View.OnClickListener() {

            MediaCreator.AudioRecorder recorder = new MediaCreator.AudioRecorder(CreateActivity.this, new Consumer<File>() {
                @Override
                public void accept(File value) {
                    audio.file = value;
                }
            });

            @Override
            public void onClick(View v) {
                recorder.start();
                switch (recorder.getState()) {
                    case EMPTY:
                        // Will never fire, it's only the initial state, which is already changed
                        // by the start() method
                        break;
                    case RECORDING:
                        audioRecordButton.setText("Recording...");
                        break;
                    case DONE:
                        audioRecordButton.setText("Record again");
                }
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.add_question_radio_group_openclose))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        boolean state = checkedId == R.id.add_question_radio_closed;
                        dialog.findViewById(R.id.add_question_answer2).setEnabled(state);
                        dialog.findViewById(R.id.add_question_answer3).setEnabled(state);
                        dialog.findViewById(R.id.add_question_answer4).setEnabled(state);
                    }
                });

    }

    private void publish() {
        String[] groupNames = new String[groupsAccessible.length];
        for (int i = 0; i < groupsAccessible.length; i++) {
            groupNames[i] = groupsAccessible[i].getName();
        }
        final Set<Group> enabled = new LinkedHashSet<>();

        new AlertDialog.Builder(this)
                .setTitle("Publish quiz?")
                //.setMessage("Warning: This quiz cannot be edited once published")
                .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Account accountje = new Account(2,"jan", Account.Type.TEACHER," ");
                        Toast.makeText(CreateActivity.this, "Quiz published", Toast.LENGTH_SHORT).show();
                        Group[] groups = enabled.toArray(new Group[enabled.size()]);
                        Quiz quiz = new Quiz(quizName.getText().toString(), groups, accountje, questions);

                        Map<String, RequestBody> resources = new HashMap<>();
                        List<Question> questions = quiz.getQuestions();
                        for (int j = 0; j < questions.size(); j++) {
                            Question question = questions.get(j);
                            if (question.getImage() != null) {
                                File file = question.getImage().getFile(null);
                                resources.put("question-" + j + "-image", RequestBody.create(MediaType.parse("image/jpeg"), file));
                            }
                            if (question.getAudio() != null) {
                                File file = question.getAudio().getFile(null);
                                resources.put("question-" + j + "-audio", RequestBody.create(MediaType.parse("audio/mp3"), file));
                            }
                        }
                        // TODO: start progress
                        AppContext.getInstance().getDBI().addQuiz(quiz, resources).enqueue(new Callback<Quiz>() {
                            @Override
                            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                                // TODO: Stop progress
                                Intent intent = new Intent(CreateActivity.this, OverviewActivity.class);
                                Toast.makeText(CreateActivity.this, "Quiz has been published", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Quiz> call, Throwable t) {
                                // TODO: stop progress
                                Toast.makeText(CreateActivity.this, "Failed to publish quiz", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).setNegativeButton("Cancel", null)
                .setMultiChoiceItems(groupNames, new boolean[groupNames.length], new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            enabled.add(groupsAccessible[i]);
                        } else {
                            enabled.remove(groupsAccessible[i]);
                        }
                    }
                })
                .show();

    }

    private class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.QuestionListEntry> {

        private List<Question> questions;

        private QuestionListAdapter() {
            this.questions = new ArrayList<>();
        }

        @Override
        public QuestionListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == 1) {
                view = inflater.inflate(R.layout.item_question, parent, false);
            } else {
                view = inflater.inflate(R.layout.no_questions, parent, false);
            }
            return new QuestionListEntry(view);
        }

        @Override
        public int getItemViewType(int position) {
            return this.questions.isEmpty() ? 0 : 1;
        }

        @Override
        public void onBindViewHolder(QuestionListEntry holder, int position) {
            if (!this.questions.isEmpty()) {
                holder.setData(questions.get(position));
            }
        }

        public void setItems(List<Question> questions) {
            this.questions = new ArrayList<>(questions.size());
            this.questions.addAll(questions);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            // Either the first, or the non-null ones
            return Math.max(1, questions.size());
        }

        class QuestionListEntry extends RecyclerView.ViewHolder {

            private final View view;

            public QuestionListEntry(View itemView) {
                super(itemView);
                this.view = itemView;
            }

            public void setData(Question question) {
                ((TextView) view.findViewById(R.id.item_question_name)).setText(question.getText());
            }
        }

    }
}
