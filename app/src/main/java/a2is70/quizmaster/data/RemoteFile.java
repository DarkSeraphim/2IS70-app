package a2is70.quizmaster.data;
import a2is70.quizmaster.database.DBInterface;
import java.io.File;

import a2is70.quizmaster.data.FileManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemoteFile implements FileManager {
    DBInterface dbi;

    public RemoteFile(){
        try {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBInterface.server_url)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(new OkHttpClient.Builder().build()).build();

            DBInterface dbi = retrofit.create(DBInterface.class);
        } catch (Exception e){

        }
    }

    public void create(String path, Quiz data, Callback c){

        dbi.addQuiz(data).enqueue(c);
    }

    public File load(String path){
        /**Not implemented; not applicable. Use dbi.loadQuizzes().*/
        return null;
    }

    public void delete(String path, Callback c){

        dbi.deleteQuiz(new Quiz(path, null, null, null)).enqueue(c);
    }

}
