package a2is70.quizmaster.data;

import java.util.Collections;
import java.util.List;
import java.util.Collection;

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
            if (question.getWeight() == 0) {
                return 0;
            }
            return (double) ((points / question.getWeight()) / answers);
        }
    }

    private List<QuestionStatistics> statistics;
        
    private int totalStudents;
        
    private int submissions;
    
    private int minScore;
    
    private int averageScore;
    
    private int maxScore;
    
    public List<QuestionStatistics> getStatistics() {
        return Collections.unmodifiableList(this.statistics);
    }

    public int[] getCorrectRate(){
        int[] temp = new int[statistics.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (int) Math.round(statistics.get(i).getCorrectRate());
        }
        return temp;
    }

    public int getOverallCompletionRate(){
        return (int) Math.round((double) submissions / totalStudents);
    }

    public int getMaxScore(){
        return maxScore;
    }

    public int getMinScore(){
        return minScore;
    }

    public int getAverageScore(){
        return averageScore;
    }
}
