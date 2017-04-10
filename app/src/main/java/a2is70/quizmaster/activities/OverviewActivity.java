package a2is70.quizmaster.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.adapters.DerpData;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.data.SubmittedQuiz;

public class OverviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView emptyView;
    private List<Quiz> quizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // @todo replace with actual quizzes
        // create a quizzes list
        DerpData dd = new DerpData();
        quizzes = new ArrayList<Quiz>();
        quizzes.add(dd.getQuizje1());
        quizzes.add(dd.getQuizje2());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final Button mAddQuizButton = (Button) findViewById(R.id.overview_add_quiz);

        // Only show Add quiz button when a teacher is logged in
        if(AppContext.getInstance().getAccount().getType()== Account.Type.TEACHER) {
            mAddQuizButton.setVisibility(View.VISIBLE);
            mAddQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(OverviewActivity.this, CreateActivity.class));
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.overview_quizzes);
        emptyView = (TextView) findViewById(R.id.empty_view);

        if (quizzes == null || quizzes.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new QuizAdapter(this, quizzes);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mAdapter);
        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
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
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /*TODO pass Quiz object to QuizActivity through intents
    * Intent i = new Intent(getApplicationContext(), QuizActivity.class);
    * i.putExtra("quiz",new Gson().toJson(Quiz Object));
    * startActivity(i);
    */
}

// Adapter for quiz overview
class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private List<Quiz> mQuizzes;
    private Context mContext;
    private static Quiz clickedQuiz;
    public static Quiz getQuiz(){
        return  clickedQuiz;
    }

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
                clickedQuiz = quiz;
                // Open Review activity for teacher OR student
                // @TODO pass info to the intent about which quiz is clicked
                if(AppContext.getInstance().getAccount().getType()== Account.Type.TEACHER) {
                    getContext().startActivity(new Intent(getContext(), ReviewActivity.class));
                } else {
                    // Make the quiz
                    getContext().startActivity(new Intent(getContext(), QuizActivity.class));
                    // @TODO if/else structure that detects whether test has been taken
                    // if so, show review instead
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