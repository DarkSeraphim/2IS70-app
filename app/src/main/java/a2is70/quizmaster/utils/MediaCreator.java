package a2is70.quizmaster.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import a2is70.quizmaster.utils.function.Consumer;

public abstract class MediaCreator {

    public interface ResultListener {
        class Result {
            private final int resultCode;

            private final Intent data;

            public Result(int resultCode, Intent data) {
                this.resultCode = resultCode;
                this.data = data;
            }

            public int getResultCode() {
                return resultCode;
            }

            public Intent getData() {
                return data;
            }
        }

        void startActivityForResult(Intent intent, Consumer<Result> resultConsumer);
    }

    protected final Activity activity;

    public MediaCreator(Activity activity) {
        this.activity = activity;
    }

    protected File createAudioFile() throws IOException {
        return createFile("MP3", ".mp3");
    }

    protected File createImageFile() throws IOException {
        return createFile("JPEG", ".jpg");
    }

    protected File createFile(String prefix, String ext) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = prefix + "_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ext, storageDir);
    }

    public abstract void start();

    public static class AudioRecorder extends MediaCreator {

        public State getState() {
            return state;
        }

        public enum State {
            EMPTY, RECORDING, DONE
        }

        private State state = State.EMPTY;

        private File audioPath;

        private MediaRecorder recorder;

        private Consumer<File> fileConsumer;

        public AudioRecorder(Activity activity, Consumer<File> result) {
            super(activity);
            this.fileConsumer = result;
        }

        @Override
        public void start() {
            if (state != State.RECORDING) {

                try {
                    audioPath = createAudioFile();
                } catch (IOException ex) {
                    Log.e("Mark", ex.getMessage(), ex);
                    return;
                }

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setOutputFile(audioPath.getPath());
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                try {
                    recorder.prepare();
                } catch (IOException ex) {
                    Log.e("Mark", "Failed to prepare", ex);
                    try {
                        recorder.release();
                    } finally {
                        recorder = null;
                    }
                    return;
                }
                recorder.start();
                Toast.makeText(activity, "Recording audio...", Toast.LENGTH_LONG).show();
                state = State.RECORDING;
            } else {
                state = State.DONE;
                recorder.stop();
                Toast.makeText(activity, "Added audio to question", Toast.LENGTH_SHORT).show();
                this.fileConsumer.accept(this.audioPath);
            }
        }

        public File getAudioPath() {
            return audioPath;
        }
    }

    public static class PhotoCreator extends MediaCreator {

        private final Consumer<File> imageConsumer;

        private File imagePath;

        private final ResultListener resultListener;

        private PhotoCreator(Activity activity, Consumer<File> imageConsumer) {
            super(activity);
            this.resultListener = (ResultListener) activity;
            this.imageConsumer = imageConsumer;
        }

        @Override
        public void start() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                try {
                    imagePath = createImageFile();
                } catch (IOException ex) {
                    return;
                }
                Uri uri = FileProvider.getUriForFile(activity, "a2is70.quizmaster.fileprovider", imagePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                resultListener.startActivityForResult(intent, new Consumer<ResultListener.Result>() {
                    @Override
                    public void accept(ResultListener.Result value) {
                        if (value.getResultCode() != Activity.RESULT_OK) {
                            Log.w("Mark", "That was not good");
                        } else {
                            imageConsumer.accept(imagePath);
                            Toast.makeText(activity, "Added image to question", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        public static PhotoCreator of(Activity activity, Consumer<File> imageConsumer) {
            if (!(activity instanceof ResultListener)) {
                throw new RuntimeException("Activity needs to implement ResultListener");
            }
            return new PhotoCreator(activity, imageConsumer);
        }
    }
}
