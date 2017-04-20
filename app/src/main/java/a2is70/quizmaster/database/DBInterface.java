package a2is70.quizmaster.database;
import java.util.List;
import java.util.Map;

import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.data.Question;
import a2is70.quizmaster.data.Quiz;
import a2is70.quizmaster.data.SubmittedQuiz;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**Interface to facilitate communication with database.*/
public interface DBInterface {

    /**URL of the server to connect to.*/
    String SERVER_URL = "http://quizmaster.darkseraphim.net:8000/";

    String JSON_CONTENT = "Content-Type: application/json";

    String MULTIPART_CONTENT = "Content-Type: ";

    /**Method to retrieve account data for authentication.*/
    @POST("user/login")
    @FormUrlEncoded
    Call<Account> loadAccount(@Field("email") String email, @Field("password") String password);

    /**Method to add new account to database.*/
    @POST("user/register")
    @FormUrlEncoded
    Call<Account> addAccount(@Field("name") String name, @Field("email") String email,
                             @Field("password") String password, @Field("type") Account.Type type);

    /**Method to edit existing account in database.
     * Possible could have.*/
    @PUT("/user")
    @Headers(JSON_CONTENT)
    Call<Account> editAccount(@Body Account a);

    /**Method to delete the calling account from database.*/
    @DELETE("/user")
    Call<Void> deleteAccount();

    /**Method to retrieve group data for one account (to list in groupActivity).*/
    @GET("/groups")
    Call<List<Group>> getGroups();

    /**Method to retrieve Quiz data for one account (to list in overviewActivity).*/
    @GET("/tests")
    Call<List<Quiz>> getQuizzes();

    /**Method to join group data.*/
    @POST("/group/subscription")
    @FormUrlEncoded
    Call<Group> joinGroup(@Field("access_code") String accessCode);

    /**Method to create a new group.*/
    @POST("/group")
    @Headers(JSON_CONTENT)
    Call<Void> createGroup(@Body Group g);

    /**Method to kick a student from a group.*/
    @DELETE("/group/subscription")
    Call<Void> kickMember(@Query("group_id") int group_id, @Query("account_id") int acc_id);

    /**Method to leave group.*/
    @DELETE("/group/subscription")
    Call<Void> leaveGroup(@Query("group_id") int id);

    /**Method to completely delete a group.*/
    @DELETE("/group")
    Call<Void> deleteGroup(@Query("group_id") int id);

    /**Method to create a new Quiz.*/
    @POST("/quiz")
    @Multipart
    Call<Quiz> addQuiz(@Part("data") Quiz q, @PartMap Map<String, RequestBody> resources);

    /**Method to edit an existing Quiz.
     * Possible could have.*/
    /*@PUT("/quiz")
    @Multipart
    Call<Quiz> editQuiz(@Query("quiz_id") int id, @Body Quiz q, @Part("audio") Question.Audio audio, @Part("image") Question.Image image);
*/
    /**Method to delete an existing Quiz.*/
    @DELETE("/quiz")
    Call<Void> deleteQuiz(@Query("quiz_id") int id);

    /**Method to submit a completed Quiz.*/
    @POST("/test/submit")
    @Headers(JSON_CONTENT)
    Call<Quiz> submitQuiz(@Body SubmittedQuiz q);

    /**Method to request a student's test review.*/
    @GET("/review/as_teacher")
    // TODO: create StudentReview object
    Call<Object> reviewStudentQuiz(@Query("test_id") int testId);

    /**Method to request a teacher's test review.*/
    @GET("/review/as_student")
    // TODO: create TeacherReview object
    Call<Object> reviewTeacherQuiz(@Query("test_id") int testId);
}