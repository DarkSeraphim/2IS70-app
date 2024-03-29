package a2is70.quizmaster.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.data.StudentReview;
import a2is70.quizmaster.data.SubmittedQuiz;
import a2is70.quizmaster.data.TeacherReview;
import a2is70.quizmaster.database.DBInterface;
import a2is70.quizmaster.utils.JsonConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBInterface dbi;

    private TextView emptyView;


    private List<Quiz> quizzes = new ArrayList<>();

    private Group[] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // @todo replace with actual quizzes

        //quizzes = Collections.emptyList();
        refreshQuizList();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("groups") == null) {
            throw new IllegalStateException("Missing 'groups' in extra bundle, serialized Group[] required");
        }
        groups = JsonConverter.fromJson(bundle.getString("groups"), Group[].class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final Button mAddQuizButton = (Button) findViewById(R.id.overview_add_quiz);

        //if you somehow skipped login
        if(AppContext.getInstance().getAccount() == null) {
            startActivity(new Intent(OverviewActivity.this, LoginActivity.class));
        }
        // Only show Add quiz button when a teacher is logged in
        if(AppContext.getInstance().getAccount().getType()== Account.Type.TEACHER) {
            mAddQuizButton.setVisibility(View.VISIBLE);
            mAddQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OverviewActivity.this, CreateActivity.class);
                    intent.putExtra("groups", JsonConverter.toJson(groups));
                    startActivity(intent);
                }
            });
            mAddQuizButton.setEnabled(groups != null && groups.length > 0);
        } else {
            mAddQuizButton.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.overview_quizzes);
        emptyView = (TextView) findViewById(R.id.empty_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new QuizAdapter(this, quizzes);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mAdapter);
        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // The list is empty, trust me, we haven't loaded shit
        mRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        // create a quizzes list
        AppContext.getInstance().getDBI().getQuizzes().enqueue(new Callback<List<Quiz>>() {
            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                // TODO: Stop progress
                quizzes.clear();
                HashMap<Integer, Boolean> checked = new HashMap<Integer, Boolean>();

                for (Quiz q : response.body()) {
                    if (!checked.containsKey(q.getID())) {
                        checked.put(q.getID(), false);
                    }
                }

                for (Quiz q : response.body()) {
                    if (!checked.get(q.getID())) {
                        quizzes.add(q);
                        checked.put(q.getID(), true);
                    }
                }


                //quizzes.addAll(toAdd);
                if (quizzes == null || quizzes.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                // TODO: stop progress
                Toast.makeText(OverviewActivity.this, "Failed to load quizzes", Toast.LENGTH_SHORT).show();
            }
        });


        // That's all!
    }

    private void refreshQuizList() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview, menu);

        // Check if there are quiz items; if not disable the menu option
        if (quizzes == null || quizzes.isEmpty()) {
            MenuItem item = menu.findItem(R.id.action_sort);
            item.setEnabled(false);
            item.getIcon().setAlpha(130);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_groups:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, GroupActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_sort_date:
                Collections.sort(quizzes, new Comparator<Quiz>() {
                    // Takes the first group encountered and sorts by that
                    @Override
                    public int compare(Quiz lhs, Quiz rhs) {
                        Long diff = lhs.getCloseAt() - rhs.getCloseAt();
                        return diff.intValue();
                    }
                });
                mAdapter.notifyDataSetChanged();
                return true;

            case R.id.action_sort_group:
                Collections.sort(quizzes, new Comparator<Quiz>() {
                    // Takes the first group encountered and sorts by that
                    @Override
                    public int compare(Quiz lhs, Quiz rhs) {
                        return lhs.getGroup()[0].getName().compareTo(rhs.getGroup()[0].getName());
                    }
                });
                mAdapter.notifyDataSetChanged();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /*TODO pass Quiz object to QuizActivity through intents
    * Intent i = new Intent(getApplicationContext(), QuizActivity.class);
    * i.putExtra("quiz",JsonConverter.toJson(Quiz Object));
    * startActivity(i);
    */

    // Adapter for quiz overview
    private static class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
        private List<Quiz> mQuizzes;

        private Context mContext;

        TeacherReview tReview;
        SubmittedQuiz sReview;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView quizNameView;
            public TextView quizGroupView;
            public TextView quizDeadlineView;
            public ImageView quizButton;
    //        public Button messageButton;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                quizNameView = (TextView) itemView.findViewById(R.id.quiz_name);
                quizGroupView = (TextView) itemView.findViewById(R.id.quiz_group);
                quizDeadlineView = (TextView) itemView.findViewById(R.id.quiz_deadline);
                quizButton = (ImageView) itemView.findViewById(R.id.quiz_button);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public QuizAdapter(Context context, List<Quiz> myQuizzes) {
            mQuizzes = myQuizzes;
            mContext = context;
        }

        private Context getContext() {
            return mContext;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public QuizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.item_quiz, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Get the data model based on position
            final Quiz quiz = mQuizzes.get(position);

            // Set item views based on your views and data model
            TextView quizNameView = holder.quizNameView;
            quizNameView.setText(quiz.getName());
            TextView quizGroupView = holder.quizGroupView;

            Group[] groups = quiz.getGroup();
            String groupString = "";
            for(int i = 0; i < groups.length; i++) {
                groupString += groups[i].getName();
                if(i < groups.length-1) {
                    groupString += ", ";
                }
            }
            quizGroupView.setText(groupString);

            TextView quizDeadlineView = holder.quizDeadlineView;
            String deadline = String.valueOf(quiz.getCloseAt());
            quizDeadlineView.setText(deadline);

            // Event handler for info button
            ImageView quizButton = holder.quizButton;
            quizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open Review activity for teacher OR student
                    // @TODO pass info to the intent about which quiz is clicked
                    if(AppContext.getInstance().getAccount().getType()== Account.Type.TEACHER) {
                         // TODO: fetch data
                        AppContext.getInstance().getDBI().reviewTeacherQuiz(quiz.getID()).enqueue(new Callback<TeacherReview>() {
                            @Override
                            public void onResponse(Call<TeacherReview> call, Response<TeacherReview> response) {
                                tReview = response.body();
                                Intent intent = new Intent(getContext(), ReviewActivity.class);
                                if(tReview!=null) {
                                    intent.putExtra("quiz",JsonConverter.toJson(quiz));
                                    intent.putExtra("statistics", JsonConverter.toJson(tReview));
                                    getContext().startActivity(intent);
                                }
                                Log.d("review", "response gelukt");
                            }

                            @Override
                            public void onFailure(Call<TeacherReview> call, Throwable t) {
                                Log.d("review", "response niet gelukt");
                            }
                        });




                    } else if(AppContext.getInstance().getAccount().getType()== Account.Type.STUDENT){
                        // Make the quiz
                        // TODO: Start review activity
                        AppContext.getInstance().getDBI().reviewStudentQuiz(quiz.getID()).enqueue(new Callback<SubmittedQuiz>() {
                            @Override
                            public void onResponse(Call<SubmittedQuiz> call, Response<SubmittedQuiz> response) {
                                if (response.code() == 200) {
                                    SubmittedQuiz sReview = response.body();
                                    Intent intent = new Intent(getContext(), ReviewActivity.class);
                                    intent.putExtra("quiz", JsonConverter.toJson(quiz));
                                    intent.putExtra("subQuiz", JsonConverter.toJson(sReview));
                                    getContext().startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getContext(), QuizActivity.class);
                                    intent.putExtra("quiz", JsonConverter.toJson(quiz));
                                    getContext().startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<SubmittedQuiz> call, Throwable t) {

                            }
                        });
                    }
                }
            });



    //        Button button = viewHolder.messageButton;
    //        button.setText("Message");
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mQuizzes.size();
        }
    }
}
