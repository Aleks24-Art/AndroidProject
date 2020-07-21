package ua.artemii.internshipmovieproject;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void makeAppFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
    }
}
