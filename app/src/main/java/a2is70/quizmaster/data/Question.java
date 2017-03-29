package a2is70.quizmaster.data;

/**Object to describe a Question in a Quiz.*/
public class Question {
    private final int id;

    /**String with the actual question to be asked.*/
    private String text;

    private boolean isOpen;

    /**Array of possible answers.*/
    private Answer[] answers;

    /**String with the correct correctAnswer.*/
    private Answer correctAnswer;

    /**Weight of this question in the Quiz.*/
    private int weight;

    private transient Image image;

    private transient Audio audio;

    /**Default constructor.*/
    public Question(int id, String text, Answer[] answers, Answer correctAnswer, int weight){
        this.id = id;
        this.text = text;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.weight = weight;
    }

    public void setText(String in){
        text = in;
    }

    public String getText(){
        return text;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setAnswers(Answer[] answers) {
        if (this.answers.length == 0) {
            throw new IllegalArgumentException("We need answers!");
        }
        this.isOpen = answers.length == 1;
        this.answers = answers;
    }

    public Answer[] getAnswers(){
        return answers;
    }

    public void setCorrectAnswer(Answer answer){
        this.correctAnswer = answer;
    }

    public Answer getCorrectAnswer(){
        return correctAnswer;
    }

    public double getWeight(){
        return weight;
    }

    public Image getImage() {
        return image;
    }

    public Audio getAudio() {
        return audio;
    }

    public static class Answer {
        private final int id;

        private final String text;

        public Answer(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }

    public class Image {
        private final int id;

        private final String path;

        public Image(int id, String path) {
            this.id = id;
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public String getPath() {
            return path;
        }
    }

    public class Audio {
        private final int id;

        private final String path;

        public Audio(int id, String path) {
            this.id = id;
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public String getPath() {
            return path;
        }
    }
}
