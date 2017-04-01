package a2is70.quizmaster.activities.adapters;

import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.data.Question;

/**
 * Created by Jasper on 01/04/2017.
 */

public class DerpData {

    static Question.Answer[] a = new Question.Answer[]{new Question.Answer("met Kalium")};
    static Question q1 = new Question("Hoe werken bananen 1?",a,new Question.Answer("met Kalium"),1);
    static Question q2 = new Question("Hoe werken bananen 2?",a,new Question.Answer("met Kalium"),1);

    private static final Question[] questions = new Question[]{q1,q2};

    public static List<Question> getListData(){
        List<Question> data = new ArrayList<>();

        for(int x=0;x<10;x++){

            for(int i=0; i <questions.length;i++){
                data.add(questions[i]);

            }
        }
        return data;
    }
}
