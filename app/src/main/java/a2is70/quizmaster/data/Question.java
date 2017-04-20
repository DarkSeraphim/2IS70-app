package a2is70.quizmaster.data;

import android.util.Log;

import java.io.File;

import a2is70.quizmaster.utils.FileDownload;
import a2is70.quizmaster.utils.function.Consumer;

/**Object to describe a Question in a Quiz.*/
public class Question {
    private final int id;

    /**String with the actual question to be asked.*/
    private String text;

    private boolean isOpen;

    /**Array of possible answers.*/
    private Answer[] answers;

    /**String with the correct correctAnswer.*/
    private Answer correctAnswer;

    /**Weight of this question in the Quiz.*/
    private int weight;

    private transient Image image;

    private transient Audio audio;

    /**Default constructor.*/
    public Question(String text, Answer[] answers, Answer correctAnswer, int weight, File imageFile, File audioFile) {
        this(-1, text, answers, correctAnswer, weight, imageFile, audioFile);
    }

    public Question(int id, String text, Answer[] answers, Answer correctAnswer, int weight, File imageFile, File audioFile) {
        this.id = id;
        this.text = text;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.weight = weight;
        if (imageFile != null) {
            this.image = new Image(imageFile);
        }
        if (audioFile != null) {
            this.audio = new Audio(audioFile);
        }
    }

    public void setText(String in){
        text = in;
    }

    public String getText(){
        return text;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setAnswers(Answer[] answers) {
        if (this.answers.length == 0) {
            throw new IllegalArgumentException("We need answers!");
        }
        this.isOpen = answers.length == 1;
        this.answers = answers;
    }

    public Answer[] getAnswers(){
        return answers;
    }

    public void setCorrectAnswer(Answer answer){
        this.correctAnswer = answer;
    }

    public Answer getCorrectAnswer(){
        return correctAnswer;
    }

    public double getWeight(){
        return weight;
    }

    public Image getImage() {
        return image;
    }

    public Audio getAudio() {
        return audio;
    }

    public static class Answer {
        private final int id;

        private final String text;

        public Answer(String text) {
            this(-1, text);
        }

        public Answer(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }

    public class Image {
        private final int id;

        private final String path;

        private transient File file;

        private volatile FileDownload downloader;

        public Image(File file) {
            this.id = -1;
            this.path = "";
            this.file = file;
        }

        public Image(int id, String path) {
            this.id = id;
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public String getPath() {
            return path;
        }

        public File getFile(final Consumer<File> updateIfRemote) {
            if (this.file != null && this.file.exists()) {
                return this.file;
            }

            if (this.path == null || this.path.isEmpty()) {
                return null;
            }

            synchronized (this) {
                if (this.downloader != null) {
                    return null;
                }
                File file = null; // App file root + '/' + path
                downloader = FileDownload.startDownload(this.file, this.path, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean value) {
                        if (value) {
                            updateIfRemote.accept(Image.this.file);
                        } else {
                            Log.d("FileDownloader", "Failed to download file " + Image.this.path);
                        }
                    }
                });
                return null;
            }
        }
    }

    public class Audio {
        private final int id;

        private final String path;

        private transient File file;

        private volatile FileDownload downloader;

        public Audio(File file) {
            this.id = -1;
            this.path = "";
            this.file = file;
        }

        public Audio(int id, String path) {
            this.id = id;
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public String getPath() {
            return path;
        }

        public File getFile(final Consumer<File> updateIfRemote) {
            if (this.file != null && this.file.exists()) {
                return this.file;
            }

            if (this.path == null || this.path.isEmpty()) {
                return null;
            }

            synchronized (this.path) {
                if (this.downloader != null) {
                    return null;
                }
                File file = null; // App file root + '/' + path
                downloader = FileDownload.startDownload(this.file, this.path, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean value) {
                        if (value) {
                            updateIfRemote.accept(Audio.this.file);
                        } else {
                            Log.d("FileDownloader", "Failed to download file " + Audio.this.path);
                        }
                    }
                });
                return null;
            }
        }
    }
}
