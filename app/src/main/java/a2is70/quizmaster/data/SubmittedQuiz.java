package a2is70.quizmaster.data;

import java.util.LinkedHashSet;
import java.util.Set;

public class SubmittedQuiz {
    private final Quiz quiz;

    private long submittedAt = -1;

    private Set<Answer> answers = new LinkedHashSet<>();

    SubmittedQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        this.answers.remove(answer);
    }

    public Set<Answer> getAnswers(){ return answers;}

    public class Answer {
        private final int id;

        private final Question question;

        private final Question.Answer answer;

        private final String answerText;

        public Answer(int id, Question question, Question.Answer answer, String answerText) {
            this.id = id;
            this.question = question;
            this.answer = answer;
            this.answerText = answerText;
        }

        public int getId() {
            return id;
        }
        
        public boolean isCorrect() {
            return this.answer != null && this.answer.getId() == this.question.getCorrectAnswer().getId();
        }

        public Question.Answer getAnswer() {
            return answer;
        }

        public String getAnswerText() {
            return answerText;
        }

        public Question getQuestion (){return question;}
    }
}
