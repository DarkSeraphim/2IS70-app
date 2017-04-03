package a2is70.quizmaster.data;


import a2is70.quizmaster.database.DBInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppContext {
    private static final AppContext ourInstance = new AppContext();

    private Account myAccount;

    private DBInterface dbi;

    public static AppContext getInstance() {
        return ourInstance;
    }

    private AppContext() {
        myAccount = null;

        try {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBInterface.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient.Builder().build()).build();

            dbi = retrofit.create(DBInterface.class);
        } catch (Exception e){

        }
    }

    public void setAccount(Account a){
        myAccount = a;
    }

    public Account getAccount(){
        return myAccount;
    }

    public DBInterface getDBI(){
        return dbi;
    }
}
