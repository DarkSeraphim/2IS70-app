package a2is70.quizmaster.data;
import java.util.List;

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

    /**List of selected answers for the quiz (by the student).*/
    private List<String> answers;

    public Quiz(String name, String group, String owner, List<Question> questions){
        this.name = name;
        this.group = group;
        this.owner = owner;
        this.questions = questions;
    }

    /**Method to delete this quiz.
     * Should also delete quiz on database.
     */
    public void delete(){

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

    public void calculateScore(){

    }

    public void setAnswers(List<String> a){
        answers = a;
    }

    /**Method to list the answers to all questions in this Quiz.*/
    public List<String> getAnswers(){ return answers; }

    /**Method to turn this object into a String (for file storage).*/
    public String toString(){
        return "";
    }
}
