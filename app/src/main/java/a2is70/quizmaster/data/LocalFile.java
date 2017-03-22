package a2is70.quizmaster.data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class LocalFile implements FileManager{
    public void create(String path, Quiz data){
        File f;
        FileOutputStream fo;
        String contents = data.toString();

        try {
            f = new File(path);
            fo = new FileOutputStream(f);
            f.createNewFile();
            fo.write(contents.getBytes());
            fo.flush();
            fo.close();
        }   catch (Exception e){

        }
    }

    public File load(String path){
        File f = new File(path);
        try {
            if (!f.exists()){
                throw new FileNotFoundException("Incorrect path. File not found.");
            }
        }   catch (Exception e){

        }
        return f;
    }

    public void delete(String path){
        File f = new File(path);
        try {
            if (!f.exists()){
                throw new FileNotFoundException("Incorrect path. File not found.");
            }
            if (!f.delete()){
                throw new Exception("File was not deleted succesfully.");
            }
        }   catch (Exception e){
            return;
        }
    }
}
