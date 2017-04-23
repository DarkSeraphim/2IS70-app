package a2is70.quizmaster.data;
import java.util.List;

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
    private final String name;

    /**ID of this Quiz.*/
    private int ID;

    /**String identifying the owner of this Quiz.*/
    private Account creator;

    private Group[] groups;

    /**String representing this Quiz' start date.*/
    private long startAt;

    /**String representing this Quiz' due date.*/
    private long closeAt;

    /**Duration of this Quiz (in minutes).*/
    private int timeLimit;

    /**Quotient of total points required to pass this Quiz.*/
    private int passThreshold;

    /**List of Questions that comprise this Quiz.*/
    private List<Question> questions;

    /**Database Interface Object.*/
    private DBInterface dbi;

    public Quiz(String name, Group[] groups, Account owner, List<Question> questions){
        this.name = name;
        setGroups(groups);
        this.creator = owner;
        this.questions = questions;
        this.ID = 0;
        this.timeLimit = -1; //default timelimit value used in createactivity
        dbi = AppContext.getInstance().getDBI();
    }

    /**Method to delete this quiz.
     * Should also delete quiz on database.
     */
    public void delete(Callback c){
        dbi.deleteQuiz(getID()).enqueue(c);
    }

    public void submitQuiz(Callback c){
        dbi.submitQuiz(new SubmittedQuiz(this)).enqueue(c);
    }

    public void reviewAsStudent(Callback c){
        dbi.reviewStudentQuiz(getID()).enqueue(c);
    }

    public void reviewAsTeacher(Callback c){
        dbi.reviewTeacherQuiz(getID()).enqueue(c);
    }

    public String getName(){
        return name;
    }

    public Account getCreator(){
        return creator;
    }

    public Group[] getGroup(){
        return this.groups;
    }

    public void setGroups(Group[] groups){
        this.groups = groups;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(long closeAt) {
        this.closeAt = closeAt;
    }

    public int getTimeLimit(){
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit){
        this.timeLimit = timeLimit;
    }

    public void setPassThreshold(int passThreshold) {
        this.passThreshold = passThreshold;
    }

    public int getPassThreshold() {
        return passThreshold;
    }

    public List<Question> getQuestions(){
        return questions;
    }

    public void setQuestions(List<Question> in){
        questions = in;
    }

    public int getID(){
        return ID;
    }

    public void setID(int id){
        ID = id;
    }
}
