package a2is70.quizmaster.data;
import a2is70.quizmaster.database.DBInterface;
import java.io.File;

import a2is70.quizmaster.data.FileManager;


public class RemoteFile implements FileManager {
    DBInterface dbi;

    public void create(String path, Quiz data){
        dbi.addQuiz(data);
    }

    public File load(String path){
        /**Not implemented; not applicable. Use dbi.loadQuizzes().*/
        return null;
    }

    public void delete(String path){
        dbi.deleteQuiz(new Quiz(path, null, null, null));
    }

}
