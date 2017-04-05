package a2is70.quizmaster.data;
import a2is70.quizmaster.database.DBInterface;
import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemoteFile implements FileManager {
    DBInterface dbi;

    public RemoteFile(){
        dbi = AppContext.getInstance().getDBI();
    }

    public void create(String path, Quiz data, Callback c){
        dbi.addQuiz(data, null, null).enqueue(c);
    }

    public File load(String path){
        /**Not implemented; not applicable. Use dbi.loadQuizzes().*/
        return null;
    }

    public void delete(String path, Callback c){
        dbi.deleteQuiz(0).enqueue(c);
    }

}
