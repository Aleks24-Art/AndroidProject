package ua.artemii.internshipmovieproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ua.artemii.internshipmovieproject.databinding.ActivitySplashScreenBinding;

/**
 * Activity to show splash screen
 */
public class SplashScreenActivity extends AppCompatActivity {
    public static final int SPLASH_SCREEN_DURATION = 2000;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(ActivitySplashScreenBinding.inflate(getLayoutInflater()).getRoot());
        makeAppFullScreen();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_DURATION);
    }

    @Override
    public void onBackPressed() {
    }

    private void makeAppFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
