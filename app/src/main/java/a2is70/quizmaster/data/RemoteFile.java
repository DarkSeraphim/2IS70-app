package a2is70.quizmaster.data;
import a2is70.quizmaster.database.DBInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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

    public void create(String path, Quiz data, Callback<Quiz> c){
        List<MultipartBody.Part> resources = new ArrayList<>();
        List<Question> questions = data.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (question.getImage() != null) {
                File file = question.getImage().getFile(null);
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part part;
                part = MultipartBody.Part.createFormData("question-" + i + "-image", "image.jpg", body);
                resources.add(part);
            }
            if (question.getAudio() != null) {
                File file = question.getAudio().getFile(null);
                RequestBody body = RequestBody.create(MediaType.parse("audio/mp3"), file);
                MultipartBody.Part part;
                part = MultipartBody.Part.createFormData("question-" + i + "-audio", "audio.jpg", body);
                resources.add(part);
            }
        }
        dbi.addQuiz(data, resources).enqueue(c);
    }

    public File load(String path){
        /**Not implemented; not applicable. Use dbi.loadQuizzes().*/
        return null;
    }

    public void delete(String path, Callback c){
        dbi.deleteQuiz(0).enqueue(c);
    }

}
