package a2is70.quizmaster.data;
import java.io.File;
import retrofit2.Callback;

public interface FileManager {

    /**Method to create a file.*/
    public void create(String path, Quiz data, Callback c);

    /**Method to load a file.*/
    public File load(String path);

    /**Method to delete a file.*/
    public void delete(String path, Callback c);
}
