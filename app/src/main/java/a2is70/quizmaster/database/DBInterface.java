package a2is70.quizmaster.database;
import a2is70.quizmaster.data.*;
import java.util.List;

/**Interface to facilitate communication with database.*/
public interface DBInterface {

    /**Method to retrieve account data for authentication.*/
    //@POST("user/login")
    Call<Account> loadAccount(String email, String password);

    /**Method to add new account to database.*/
    //@POST("user/register")
    Call<Account> addAccount(String email, String password);

    /**Method to edit existing account in database.
     * Possible could have.*/
    //Call editAccount(Account a);

    /**Method to delete the calling account from database.*/
    Call deleteAccount();

    /**Method to retrieve group data for one account (to list in groupActivity).*/
    //@GET("/groups")
    Call<List<Group>> loadGroups(Account a);

    /**Method to retrieve Quiz data for one account (to list in overviewActivity).*/
    //@GET("/tests")
    Call<List<Quiz>> loadQuizzes(Account a);

    /**Method to join group data.*/
    //@POST("/group/subscription")
    Call joinGroup(Group g);

    /**Method to edit group.
     * Possible could have.*/
    //Call editGroup(Group g);

    //@DELETE("/group/subscription")
    /**Method to leave group.*/
    Call leaveGroup(Group g);

    /**Method to create a new Quiz.*/
    //@POST("/test")
    Call addQuiz(Quiz q);

    /**Method to edit an existing Quiz.
     * Possible could have.*/
    //Call editQuiz(Quiz q);

    /**Method to delete an existing Quiz.*/
    //@DELETE("/test")
    Call deleteQuiz(Quiz q);

    /**Method to submit a completed Quiz.*/
    //@POST("/test/submit")
    Call submitQuiz(Quiz q);

    /**Method to request a student's test review.*/
    //@GET("/review/as_teacher?test_id=id")
    Call reviewStudentQuiz(Quiz q);

    /**Method to request a teacher's test review.*/
    //@GET("/review/as_student?test_id=id")
    Call reviewTeacherQuiz(Quiz q);
}

/**Placeholder class so that this stuff compiles.*/
class Call<T>{
}