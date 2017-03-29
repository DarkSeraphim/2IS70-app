package a2is70.quizmaster.data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import retrofit2.Callback;

public class LocalFile implements FileManager{
    /**
     * Method to create a Local file.
     * @param path path of the file.
     * @param data contents of the file.
     * @param c callback for remote operations. Leave as null here.
     */
    public void create(String path, Quiz data, Callback c){
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

    /**
     * Method to delete a local file.
     * @param path path to the file.
     * @param c callback for remote files. Leave as null here.
     */
    public void delete(String path, Callback c){
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
