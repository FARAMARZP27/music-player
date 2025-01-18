package com.example.playmusic;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
   Button playBtn;
   SeekBar positionBar,volumeBar;
   TextView elapsedTimeLabel,remainingTimeLabel;
   MediaPlayer mp;
   int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        playBtn=findViewById(R.id.playBtn);
        elapsedTimeLabel=findViewById(R.id.elapsidTimeLabel);
        remainingTimeLabel=findViewById(R.id.remainingTimeLabel);
        mp = MediaPlayer.create(this,R.raw.mp3);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.3f,0.3f);
        totalTime=mp.getDuration();
        positionBar=findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp!= null){
                    try {
                        Message msg = new Message();
                        msg.what=mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }

            }
        }).start();


        volumeBar=findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumNum=progress /100f;
                mp.setVolume(volumNum,volumNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @Override
        public void handleMessage (Message msg){
            int currentPosition =msg.what;
            positionBar.setProgress(currentPosition);
            String elapsedTime =createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);
            String remainingTime=createTimeLabel(totalTime -currentPosition);
            remainingTimeLabel.setText("-"+remainingTime);
        }
    };
    public String createTimeLabel(int time){
        String timeLabel="";
        int min =time/1000/60;
        int sec =time/1000%60;
        timeLabel=min+":";
        if (sec>10)timeLabel+="0";
        timeLabel+=sec;
        return timeLabel;
    }
    public void PlayBtnClick(View view){
     if (!mp.isPlaying()){
         mp.start();
         playBtn.setBackgroundResource(R.drawable.ic_pause);

     } else {
         mp.pause();
         playBtn.setBackgroundResource(R.drawable.ic_play);
     }
    }
}