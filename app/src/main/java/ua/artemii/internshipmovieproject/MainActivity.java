package ua.artemii.internshipmovieproject;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import ua.artemii.internshipmovieproject.databinding.ActivityMainBinding;
import ua.artemii.internshipmovieproject.services.VideoPlayer;

/**
 * Main activity which contain all fragments, going after splash screen
 */
public class MainActivity extends AppCompatActivity {

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
        VideoPlayer.getInstance().getPlayer().setPlayWhenReady(false);
    }
}
