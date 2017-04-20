package a2is70.quizmaster.data;

import com.google.gson.annotations.SerializedName;

import a2is70.quizmaster.database.DBInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* Object to store data related to a user's account.
 */
public class Account {

    /* Unique ID given by database. */
    private final int id;

    /* Email adress of user. Used as login identifier.*/
    private final String email;

    /* Full name of user. */
    private String name;

    /* Account type of user. See enum Type*/
    private final Type type;

    /* Database Interface object.*/
    DBInterface dbi;

    /** Default constructor.
     */
    public Account(int id, String name, Type type, String email){
        this.id = id;
        this.name = name;
        this.type = type;
        this.email = email;

        dbi = AppContext.getInstance().getDBI();
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    /**Method to change name of Account.
     * Possible could have.
     * Updates GUI(?)
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


    public Type getType(){
        return type;
    }

    public enum Type {
        @SerializedName("teacher")
        TEACHER,
        @SerializedName("student")
        STUDENT
    }
}
