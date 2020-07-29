package ua.artemii.internshipmovieproject;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import ua.artemii.internshipmovieproject.databinding.ActivityMainBinding;
import ua.artemii.internshipmovieproject.services.DisposableService;
import ua.artemii.internshipmovieproject.services.SimpleExoPlayerService;

public class MainActivity extends AppCompatActivity {
    /**
     * Default class TAG
     */
    public static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());
        makeAppFullScreen();
        Navigation.findNavController(this, R.id.video_list_nav_host_fragment);
    }

    private void makeAppFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SimpleExoPlayerService.getInstance().getPlayer().setPlayWhenReady(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisposableService.dispose();
        Log.d(TAG, "Dispose in onDestroy");
    }
}
