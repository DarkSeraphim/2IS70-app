package a2is70.quizmaster.data;
import java.util.List;
import java.util.ArrayList;

import a2is70.quizmaster.database.DBInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**Object to represent a Quiz.*/
public class Quiz {

    /**String identifying this Quiz.*/
    private String name;

    /**String identifying which group this Quiz is aimed at.*/
    private String group;

    /**String identifying the owner of this Quiz.*/
    private String owner;

    /**List of Questions that comprise this Quiz.*/
    private List<Question> questions;

    /**String representing this Quiz' due date.*/
    private String dueDate;

    /**String representing this Quiz' start date.*/
    private String startDate;

    /**Duration of this Quiz (in minutes).*/
    private int duration;

    /**Quotient of total points required to pass this Quiz.*/
    private double passTreshold;

    /**Final score of a quiz (upon completion).*/
    private double score;

    /**Database interface Object.*/
    private DBInterface dbi;

    public Quiz(String name, String group, String owner, List<Question> questions){
        this.name = name;
        this.group = group;
        this.owner = owner;
        this.questions = questions;

        try {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBInterface.server_url)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient.Builder().build()).build();

            dbi = retrofit.create(DBInterface.class);
        } catch (Exception e){

        }
    }

    /**Method to delete this quiz.
     * Should also delete quiz on database.
     */
    public void delete(Callback c){
        Call call = dbi.deleteQuiz(this);

        call.enqueue(c);
    }

    public void submitQuiz(Callback c){
        dbi.submitQuiz(this).enqueue(c);
    }

    public void reviewAsStudent(Callback c){
        dbi.reviewStudentQuiz(this).enqueue(c);
    }

    public void reviewAsTeacher(Callback c){
        dbi.reviewTeacherQuiz(this).enqueue(c);
    }

    public void setName(String in){
        name = in;
    }

    public String getName(){
        return name;
    }

    public void setGroup(String in){
        group = in;
    }

    public String getGroup(){
        return group;
    }

    public void setOwner(String in){
        owner = in;
    }

    public String getOwner(){
        return owner;
    }

    public List<Question> getQuestions(){
        return questions;
    }

    public void setQuestions(List<Question> in){
        questions = in;
    }

    public int getDuration(){
        return duration;
    }

    public void setDuration(int in){
        duration = in;
    }

    public double getScore(){ return score; }

    public double calculateScore(){
        double total_score = 0, total_weight = 0;
        for (Question q : questions) {
            total_weight += q.getWeight();
            if(q.isCorrect()){
                total_score += q.getWeight();
            }
        }
        score = (total_score/total_weight);
        return score;
    }

    /**Method to list the answers to all questions in this Quiz.*/
    public List<String> getAnswers(){
        List<String> answers = new ArrayList<String>();
        for (Question q : questions) {
            answers.add(q.getChosenAnswer());
        }
        return answers;
    }

    /**Method to turn this object into a String (for file storage).*/
    public String toString(){
        //TODO
        return "";
    }
}
