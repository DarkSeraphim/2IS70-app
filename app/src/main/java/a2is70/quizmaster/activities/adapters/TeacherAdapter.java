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

/**
 * Created by Jasper on 01/04/2017.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.questionHolder>{

    private List<Question> listData;
    private LayoutInflater inflater;
    private int[] compData;
    private Account.Type acc = Account.Type.STUDENT;

    public TeacherAdapter (List<Question> listData,int[] compData,Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.compData = compData;
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
        holder.compRate.setText(""+compData[position]+"%");
        holder.title.setOnClickListener(new View.OnClickListener(){
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
                                            + "Completion rate: "
                                            + compData[position]
                                            + "%"
                            )
                            .setPositiveButton("terug", new DialogInterface.OnClickListener() {
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
        private TextView compRate;

        public questionHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.review_question_text);
            compRate = (TextView)itemView.findViewById(R.id.review_success_rate);
        }
    }

}
