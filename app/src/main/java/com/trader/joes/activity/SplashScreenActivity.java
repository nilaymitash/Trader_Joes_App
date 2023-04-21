package com.trader.joes.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.trader.joes.R;

/**
 * This is the first screen that loads when app starts.
 * Simple shows an Image and a Circular progress bar
 */
public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mProgressBar = findViewById(R.id.splash_progress_bar);
        animateProgressBar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /** Starts the MainActivity after a 3 second delay */
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    /**
     * This method animates the ProgressBar on screen for 3 seconds
     */
    private void animateProgressBar() {
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(3000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}