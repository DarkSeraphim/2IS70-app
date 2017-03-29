package a2is70.quizmaster.data;
import java.util.ArrayList;
import java.util.List;

import a2is70.quizmaster.database.DBInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**Object to represent a group of users (students and teachers combined).*/
public class Group {
    /**ID identifying this group (for database).*/
    private final int id;

    /**Name identifying this group.*/
    private final String name;

    /**List of all members that are part of this group.*/
    private List<Account> members = new ArrayList<>();

    /**Access code for new users to join this group.*/
    private final String accessCode;

    public Group(int id, String name, String accessCode){
        this.name = name;
        this.id = id;
        this.accessCode = accessCode;

        try {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBInterface.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient.Builder().build()).build();

            DBInterface dbi = retrofit.create(DBInterface.class);
        } catch (Exception e){

        }
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAccessCode(){
        return accessCode;
    }

    public void joinGroup(Callback c){
        dbi.joinGroup(this).enqueue(c);
    }

    public void leaveGroup(Callback c){
        dbi.leaveGroup(this).enqueue(c);
    }
}
