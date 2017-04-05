package a2is70.quizmaster.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import a2is70.quizmaster.R;

public class OverviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private List quizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Button mAddQuizButton = (Button) findViewById(R.id.overview_add_quiz);
        mAddQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OverviewActivity.this, CreateActivity.class));
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.overview_quizzes);
        emptyView = (TextView) findViewById(R.id.empty_view);

        if (quizzes == null || quizzes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

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
