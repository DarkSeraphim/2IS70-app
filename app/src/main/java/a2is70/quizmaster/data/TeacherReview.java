package a2is70.quizmaster.data;

import java.util.List;

public class TeacherReview {
    public class QuestionStatistics {
        
        private Question question;
        
        private int answers;
        
        private int points;
        
        public Question getQuestion() {
            return question;
        }
        
        public int getAnswerCount() {
            return answers;
        }
        
        public double getTotalPoints() {
            return points;
        }
        
        /**
         * @return the percentage of correct answers, within range 0.0-1.0
         */
        public double getCorrectRate() {
            if (question.getWeight()) {
                return 0;
            }
            return (double) ((points / question.getWeight()) / answers;
        }
    }

    private List<QuestionStatistics> statistics;
        
    private int totalStudents;
        
    private int submissions;
    
    private int minScore;
    
    private int averageScore;
    
    private int maxScore;
    
    public List<QuestionStatistics> getStatistics() {
        return Collection.unmodifiableList(this.statistics);
    }
    
    public List<Question> gimmeQuestionsPls() {
        throw new UnsupportedOperationException("Call getStatistics() for a List of question statistics, which contains Question objects");
    }

    public int[] gimmeSuccesRatesPls(){
        int[] temp = int[statistics.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (int) Math.round(statistics.get(i).getCorrectRate());
        }
        return null;
    }

    public int gimmeOverallCompletionPls(){
        return (int) Math.round((double) submissions / totalStudents);
    }

    public int gimmeMaxPls(){
        return maxScore;
    }
    public int gimmeMinPls(){
        return minScore;
    }

    public int gimmeAveragePls(){
        return averageScore;
    }
}
