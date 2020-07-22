package ua.artemii.internshipmovieproject;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import ua.artemii.internshipmovieproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    /**
     * ViewBinding for MainActivity
     */
    private ActivityMainBinding binding;

    /**
     * Default class TAG
     */
    public static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeAppFullScreen();
        binding
                = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Navigation.findNavController(this, R.id.video_list_nav_host_fragment);
    }


    private void makeAppFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
    }
}
