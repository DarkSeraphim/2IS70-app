package a2is70.quizmaster.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        Button mAddQuizButton = (Button) findViewById(R.id.overview_add_quiz);
        if (AppContext.getInstance().getAccount().getType() == Account.Type.TEACHER) {
            mAddQuizButton.setText("%$#{HACKED}%#$@%");
            mAddQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(OverviewActivity.this, ReviewActivity.class));
                }
            });
        } else {
            mAddQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(OverviewActivity.this, CreateActivity.class));
                }
            });
        }
    }
    /*TODO pass Quiz object to QuizActivity through intents
    * Intent i = new Intent(getApplicationContext(), QuizActivity.class);
    * i.putExtra("quiz",new Gson().toJson(Quiz Object));
    * startActivity(i);
    */
}
