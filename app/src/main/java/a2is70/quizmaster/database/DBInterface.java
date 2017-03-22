package a2is70.quizmaster.database;
import a2is70.quizmaster.data.*;
import java.util.List;

/**Interface to facilitate communication with database.*/
public interface DBInterface {

    /**Method to retrieve account data for authentication.*/
    @POST("user/login")
    DBCallback<Account> loadAccount(String email, String password);

    /**Method to add new account to database.*/
    @POST("user/register")
    DBCallback<Account> addAccount(String email, String password);

    /**Method to edit existing account in database.*/
    DBCallback editAccount(Account a);

    /**Method to delete an existing account from database.*/
    DBCallback deleteAccount(Account a);

    /**Method to retrieve group data for one account (to list in groupActivity).*/
    @GET("/groups")
    DBCallback<List<Group>> loadGroups(Account a);

    /**Method to retrieve Quiz data for one account (to list in overviewActivity).*/
    @GET("/tests")
    DBCallback<List<Quiz>> loadQuizzes(Account a);

    /**Method to join group data.*/
    @POST("/group/subscription")
    DBCallback joinGroup(Group g);

    /**Method to edit group.*/
    DBCallback editGroup(Group g);

    @DELETE("/group/subscription")
    /**Method to leave group.*/
    DBCallback leaveGroup(Group g);

    /**Method to create a new Quiz.*/
    DBCallback addQuiz(Quiz q);

    /**Method to edit an existing Quiz.*/
    DBCallback editQuiz(Quiz q);

    /**Method to delete an existing Quiz.*/
    @DELETE("/test")
    DBCallback deleteQuiz(@Path("test") Quiz q);

    /**Method to submit a completed Quiz.*/
    @POST("/test/submit")
    DBCallback submitQuiz(Quiz q);

    /**Method to request a student's test review.*/
    @GET("/review/as_teacher?test_id=id")
    DBCallback reviewStudentQuiz(Quiz q);

    /**Method to request a teacher's test review.*/
    @GET("/review/as_student?test_id=id")
    DBCallback reviewTeacherQuiz(Quiz q);
}
