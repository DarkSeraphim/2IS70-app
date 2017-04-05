package a2is70.quizmaster.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.SubmittedQuiz;

/**
 * Created by s141663 on 3-4-2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.questionHolder>{
    private List<SubmittedQuiz.Answer> listData;
    private LayoutInflater inflater;

    public StudentAdapter (SubmittedQuiz quiz, Context c){
        this.inflater = LayoutInflater.from(c);
        List<SubmittedQuiz.Answer> listData=new ArrayList(quiz.getAnswers());
        this.listData = listData;
    }

    @Override
    public StudentAdapter.questionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_student_review_question,parent, false);
        return new StudentAdapter.questionHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudentAdapter.questionHolder holder, final int position) {
        final Question quest = listData.get(position).getQuestion();
        holder.title.setText(quest.getText());
        holder.correctAnswer.setText(quest.getCorrectAnswer().getText());
        if(quest==DerpData.q1) {
            holder.yourAnswer.setText(quest.getCorrectAnswer().getText());
        }else if(quest==DerpData.q2) {
            holder.yourAnswer.setText("Zwaartekracht");
        }
        if(listData.get(position).isCorrect()) {
            holder.correctOrNah.setImageResource(R.drawable.ic_correct_color_20dp);
        }else if(!listData.get(position).isCorrect()) {
            holder.correctOrNah.setImageResource(R.drawable.ic_incorrect_color_20dp);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class questionHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView yourAnswer;
        private TextView correctAnswer;
        private ImageView correctOrNah;

        public questionHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.review_question);
            yourAnswer= (TextView)itemView.findViewById(R.id.review_given_answer);
            correctAnswer = (TextView)itemView.findViewById(R.id.review_correct_answer);
            correctOrNah = (ImageView)itemView.findViewById(R.id.review_image);
        }
    }

}
