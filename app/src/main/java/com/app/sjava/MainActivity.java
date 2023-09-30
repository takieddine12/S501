package com.app.sjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ConstraintLayout constraintLayout;
    private CircularProgressIndicator progressBar;
    private FloatingActionButton fab;
    private TextView toolTipTxt;
    private final Timer mTimer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab);
        constraintLayout = findViewById(R.id.main);
        progressBar = findViewById(R.id.progress_bar);
        toolTipTxt  = findViewById(R.id.toolTipText);


        // toolTipTxt.setText("Tap to start recording.\nTap again to stop.");
        mediaPlayer = MediaPlayer.create(this, R.raw.file);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolTipTxt.setVisibility(View.GONE);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Recording..", Toast.LENGTH_SHORT).show();
                fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_stop_24));
                pulsateView(view);
                pulsateView1(progressBar);
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    startUpdatingProgressBar();
                }
            }
        });

    }

    private void startUpdatingProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        int duration = mediaPlayer.getDuration();
        progressBar.setMax(duration);

        AtomicLong lastUpdateTime = new AtomicLong(System.currentTimeMillis());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                runOnUiThread(() -> {
                    int currentTime = mediaPlayer.getCurrentPosition();
                    long elapsedTime = System.currentTimeMillis() - lastUpdateTime.get();

                    if (progressBar.getProgress() < duration) {
                        progressBar.setProgress(progressBar.getProgress() + (int) elapsedTime);
                        lastUpdateTime.set(System.currentTimeMillis());
                    }
                });
            }
        }, 0, 100); // Update the progress bar every 100 milliseconds (0.1 second)
    }
    private void pulsateView(View iv) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(iv,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        );
        scaleDown.setInterpolator(new BounceInterpolator());
        scaleDown.setDuration(2000);
        scaleDown.start();
    }
    private void pulsateView1(View iv) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        );
        scaleDown.setInterpolator(new BounceInterpolator());
        scaleDown.setDuration(2000);
        scaleDown.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}