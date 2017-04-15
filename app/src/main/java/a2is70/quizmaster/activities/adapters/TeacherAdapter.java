package a2is70.quizmaster.activities.adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.ReviewActivity;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;

/**
 * Created by Jasper on 01/04/2017.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.questionHolder>{

    private List<Question> listData;
    private LayoutInflater inflater;
    private int[] succData;

    public TeacherAdapter (Quiz quiz, int[] succData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = quiz.getQuestions();
        this.succData = succData;
    }

    @Override
    public questionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review_question,parent, false);
        return new questionHolder(view);
    }

    @Override
    public void onBindViewHolder(final questionHolder holder, final int position) {
        final Question quest = listData.get(position);
        holder.title.setText(quest.getText());
        holder.succRate.setText(""+succData[position]+"%");
        if(succData[position]>50) {
            holder.check.setImageResource(R.drawable.ic_correct_color_20dp);
        }else if(succData[position]<50) {
            holder.check.setImageResource(R.drawable.ic_incorrect_color_20dp);
        }
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle("Question " + (position + 1))
                            .setMessage("\""
                                            + quest.getText()
                                            + "\""
                                            + System.lineSeparator()
                                            + System.lineSeparator()
                                            + "Correct answer:"
                                            + System.lineSeparator()
                                            + quest.getCorrectAnswer().getText()
                                            + System.lineSeparator()
                                            + System.lineSeparator()
                                            + "Succes rate: "
                                            + succData[position]
                                            + "%"
                            )
                            .setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class questionHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView succRate;
        private ImageView check;
        private LinearLayout layout;

        public questionHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout)itemView.findViewById(R.id.review_question_layout);
            title=(TextView)itemView.findViewById(R.id.review_question_text);
            succRate = (TextView)itemView.findViewById(R.id.review_success_rate);
            check = (ImageView)itemView.findViewById(R.id.review_image);
        }
    }

}
