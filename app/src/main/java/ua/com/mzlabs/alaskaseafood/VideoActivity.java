package ua.com.mzlabs.alaskaseafood;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VideoActivity extends AppCompatActivity {
    AlertDialog alert;
    VideoView videoPlayer;
    String myVideoUri;
    Button start;
    String vm;
    String textmsg;
    int countFiles = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);

        videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
        start = findViewById(R.id.buttonStart);
        CheckFolders();
        vm = Environment.getExternalStorageDirectory() +
                File.separator + "Alaska" + File.separator + "Video" + File.separator;
        myVideoUri = vm + "vid1.mp4";


        //Воспроизведение видео
        VidPlay();
    }

    public void VidPlay() {
        final int[] c = {1};
        c[0] = 1;
        File vids = new File(myVideoUri);
        if (vids.exists()) {
            videoPlayer.setVideoPath(myVideoUri);
        }
        videoPlayer.setOnPreparedListener(PreparedListener);
        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                if (c[0] == 1) {
                    myVideoUri = vm + "vid2.mp4";
                    c[0] -= 1;
                } else {
                    myVideoUri = vm + "vid1.mp4";
                    c[0] += 1;
                }
                File vids = new File(myVideoUri);
                if (vids.exists()) {
                    videoPlayer.setVideoPath(myVideoUri);
                }
                videoPlayer.setOnPreparedListener(PreparedListener);
                Log.i("***", "Check");
            }
        });
    }

    public void DownloadFile(String src, final String dst)
    {
        new FileLoadingTask(
                src,
                new File(dst),
                new FileLoadingListener() {
                    @Override
                    public void onBegin() {
                        Log.i("***", "Start!");
                        if (alert != null)
                            alert.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                        builder.setTitle("Info")
                                .setMessage(((textmsg != null) ? textmsg : "") + "Подождите, идет загрузка файла " + dst)
                                .setIcon(R.drawable.as)
                                .setCancelable(false);
                        alert = builder.create();
                        alert.show();
                        textmsg = "Подождите, идет загрузка файла " + dst + "\n";
                        countFiles++;
                    }

                    @Override
                    public void onSuccess() {

                        Log.i("***", "Success!");
                        countFiles--;
                        Log.v("Files", " " + countFiles);
                        if (countFiles == 0) {
                            alert.cancel();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Throwable cause) {
                        alert.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
                        builder.setTitle("Ошибка загрузки!")
                                .setMessage("Проверьте соединение с интернетом")
                                .setIcon(R.drawable.as)
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onEnd() {
                        Log.i("***", "End");
                    }
                }).execute();
    }

    public void CheckFolders() {
        File MainFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Alaska");
        File VidFolder = new File(MainFolder + File.separator + "Video");
        File PicFolder = new File(MainFolder + File.separator + "Img");
        if (!MainFolder.exists()) {
            MainFolder.mkdirs();
        }
        if (!VidFolder.exists()) {
            if (VidFolder.mkdirs())
                CheckVid();
        }
        if (!PicFolder.exists()) {
            if (PicFolder.mkdirs())
                CheckImg();
        }
        CheckVid();
        CheckImg();
    }

    public void CheckVid() {
        File vid = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Alaska" + File.separator + "Video");
        File vid1 = new File(vid + File.separator + "vid1.mp4");
        File vid2 = new File(vid + File.separator + "vid2.mp4");
        File vid3 = new File(vid + File.separator + "vid3.mp4");
        if (!vid1.exists())
        {
            DownloadFile("http://mzlabs.com.ua/wp-content/themes/twentysixteen/BeringSeaPollock02648.mp4", vid1.toString());

        }
        if (!vid2.exists())
        {
            DownloadFile("http://mzlabs.com.ua/wp-content/themes/twentysixteen/Juneau_nature2.mp4", vid2.toString());

        }
    }

    public void CheckImg() {

    }

    @Override
    public void onBackPressed() {

    }

    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer m) {
            try {
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                }
                m.setVolume((float) 0, (float) 0);
                m.setLooping(false);
                m.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void onClickStart(View view) {
        Intent intent = new Intent(VideoActivity.this, MainMenu.class);
        startActivity(intent);
    }

}
