package a2is70.quizmaster.activities.adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import a2is70.quizmaster.R;
import a2is70.quizmaster.data.Question;

/**
 * Created by Jasper on 01/04/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.questionHolder>{

    private List<Question> listData;
    private LayoutInflater inflater;

    public RecyclerAdapter (List<Question> listData,Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }



    @Override
    public questionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review_question,parent, false);
        return new questionHolder(view);
    }

    @Override
    public void onBindViewHolder(questionHolder holder, int position) {
        Question quest = listData.get(position);
        holder.title.setText(quest.getText());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class questionHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private View container;

        public questionHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.review_question_text);
            container = itemView.findViewById(R.id.review_question_text);
        }
    }

}
