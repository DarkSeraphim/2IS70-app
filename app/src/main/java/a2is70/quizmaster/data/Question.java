package a2is70.quizmaster.data;

/**Object to describe a Question in a Quiz.*/
public class Question {
    /**String with the actual question to be asked.*/
    private String contents;

    /**Array of possible answers.*/
    private String[] answers;

    /**String with the correct answer.*/
    private String correctAnswer;

    /**String with the answer chosen by student (if any).*/
    private String chosenAnswer;

    /**Weight of this question in the Quiz.*/
    private double weight;

    /**Default constructor.*/
    public Question(String contents, String[] answers, String correctAnswer, double weight){
        this.contents = contents;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.weight = weight;
    }

    public void setContents(String in){
        contents = in;
    }

    public String getContents(){
        return contents;
    }

    public void setAnswers(String[] in){
        answers = in;
    }

    public String[] getAnswers(){
        return answers;
    }

    public void setCorrectAnswer(String in){
        correctAnswer = in;
    }

    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public void setChosenAnswer(String in){
        chosenAnswer = in;
    }

    public String getChosenAnswer(){
        return chosenAnswer;
    }
}
