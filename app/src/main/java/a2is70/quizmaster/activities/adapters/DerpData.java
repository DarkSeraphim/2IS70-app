package a2is70.quizmaster.activities.adapters;

import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;

/**
 * Created by Jasper on 01/04/2017.
 */

public class DerpData {


    static Account account1 = new Account(1,"Jasper", Account.Type.TEACHER,"Jasper@tue.nl");
    static Account account2 = new Account(2,"Thijs", Account.Type.TEACHER,"Thijs@tue.nl");
    static Group groepje1 = new Group(1,"Vet mooi groepje 1","GR1EP");
    static Group groepje2 = new Group(1,"Vet mooi groepje 2","GR2EP");
    static Question.Answer[] a = new Question.Answer[]{new Question.Answer("met Kalium")};
    static Question q1 = new Question("Hoe werken bananen?",a,new Question.Answer("met Kalium"),1);
    static Question q2 = new Question("Hoe werken appels?",a,new Question.Answer("met Cyanide"),1);
    static Question q3 = new Question("Hoe werken magneten",a,new Question.Answer("Zuigen"),1);
    static int compRate1 = 56;
    static int compRate2 = 33;


    private static final Question[] questions = new Question[]{q1,q2};
    private static final Question[] questions2 = new Question[]{q2,q3};

    static Quiz quizje1 = new Quiz("Quiz 1",new Group[]{groepje1},account1,getListData());
    static Quiz quizje2 = new Quiz("Quiz 2",new Group[]{groepje2},account2,getListData2());

    public static Quiz getQuizje1(){
        return quizje1;
    }
    public static Quiz getQuizje2(){
        return quizje2;
    }

    public static List<Question> getListData2(){
        List<Question> data = new ArrayList<>();

        for(int x=0;x<10;x++){

            for(int i=0; i <questions2.length;i++){
                data.add(questions2[i]);

            }
        }
        return data;
    }

    public static List<Question> getListData(){
        List<Question> data = new ArrayList<>();

        for(int x=0;x<10;x++){

            for(int i=0; i <questions.length;i++){
                data.add(questions[i]);

            }
        }
        return data;
    }

    public static int[] getCompletionRate(){
        List<Question> data = getListData();
        int[] comples = new int[data.size()];

        for (int x=0; x< data.size();x++){
            if(data.get(x)==q1){
                comples[x]=compRate1;
            }
            if(data.get(x)==q2){
                comples[x]=compRate2;
            }
        }
        return comples;
    }

    public static boolean[] getCorrect(){
        List<Question> data = getListData();
        boolean[] comples = new boolean[data.size()];

        for (int x=0; x< data.size();x++){
            if(data.get(x)==q1){
                comples[x]=true;
            }
            if(data.get(x)==q2){
                comples[x]=false;
            }
        }
        return comples;
    }
}
