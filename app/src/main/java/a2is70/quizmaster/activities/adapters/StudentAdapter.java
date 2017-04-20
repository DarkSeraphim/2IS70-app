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
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.SubmittedQuiz;

/**
 * Created by s141663 on 3-4-2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.QuestionHolder>{
    private List<SubmittedQuiz.Answer> listData;
    private LayoutInflater inflater;

    public StudentAdapter (SubmittedQuiz quiz, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = new ArrayList<>(quiz.getAnswers());
    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_student_review_question,parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionHolder holder, final int position) {
        final Question quest = listData.get(position).getQuestion();
        final SubmittedQuiz.Answer answ = listData.get(position);
        holder.title.setText(quest.getText());
        holder.correctAnswer.setText("Correct answer: "+quest.getCorrectAnswer().getText());
        holder.yourAnswer.setText("Your answer: "+answ.getAnswerText());
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

    class QuestionHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView yourAnswer;
        private TextView correctAnswer;
        private ImageView correctOrNah;

        public QuestionHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.review_question);
            yourAnswer= (TextView)itemView.findViewById(R.id.review_given_answer);
            correctAnswer = (TextView)itemView.findViewById(R.id.review_correct_answer);
            correctOrNah = (ImageView)itemView.findViewById(R.id.review_image);
        }
    }

}
