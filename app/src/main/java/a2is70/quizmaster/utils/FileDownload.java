package a2is70.quizmaster.utils;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import a2is70.quizmaster.utils.function.Consumer;
import okhttp3.OkHttpClient;

/**
 * Created by s129977 on 20-4-2017.
 */

public class FileDownload extends AsyncTask<Void, Void, Void> {

    private static final String FILE_HOST = "http://quizmaster.darkseraphim.net/";

    private final File file;

    private final String path;

    private final Consumer<Boolean> done;

    private FileDownload(File file, String path, Consumer<Boolean> done) {
        this.file = file;
        this.path = path;
        this.done = done;
    }

    @Override
    protected Void doInBackground(Void[] params) {
        if (file.exists()) {
            done.accept(true);
            return null;
        }
        try {
            if (!file.createNewFile()) {
                // Already downloading? (assuming FS does the synchronization)
                // Ignore
                done.accept(true);
                return null;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(FILE_HOST + this.path).openConnection();
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {
                final int LENGTH = conn.getHeaderFieldInt("Content-Length", 0);
                if (LENGTH <= 0) {
                    done.accept(false);
                    return null;
                }
                byte[] buffer = new byte[1024];
                try (FileOutputStream writer = new FileOutputStream(this.file);
                     InputStream in = conn.getInputStream()) {
                    int len;
                    int read = 0;
                    // Don't read past the Content-Length served
                    while ((len = in.read(buffer, 0, Math.min(LENGTH - read, buffer.length))) > 0) {
                        read += len;
                        writer.write(buffer, 0, len);
                    }
                } catch (IOException ex) {
                    file.delete();
                    done.accept(false);
                }
            } else {

                done.accept(false);
            }
        } catch (IOException e) {
            done.accept(false);
        }

        return null;
    }

    public static FileDownload startDownload(File file, String path, Consumer<Boolean> done) {
        FileDownload download = new FileDownload(file, path, done);
        return (FileDownload) download.execute();
    }
}
