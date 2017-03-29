package a2is70.quizmaster.data;
import java.util.List;

import a2is70.quizmaster.database.DBInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**Object to represent a Quiz.*/
public class Quiz {

    /**String identifying this Quiz.*/
    private final String name;

    /**String identifying the owner of this Quiz.*/
    private Account creator;

    /**String identifying which group this Quiz is aimed at.*/
    private int[] groups;

    private transient Group[] groupObjects;

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

    public Quiz(String name, Group[] groups, Account owner, List<Question> questions){
        this.name = name;
        setGroups(groups);
        this.creator = owner;
        this.questions = questions;

        try {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBInterface.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient.Builder().build()).build();

            DBInterface dbi = retrofit.create(DBInterface.class);
        } catch (Exception e){

        }
    }

    public String getName(){
        return name;
    }

    public Account getCreator(){
        return creator;
    }

    public Group[] getGroup(){
        return this.groupObjects;
    }

    public void setGroups(Group[] groups){
        this.groups = new int[groups.length];
        for (int i = 0; i < groups.length; i++) {
            this.groups[i] = groups[i].getId();
        }
        this.groupObjects = groups;
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
}
