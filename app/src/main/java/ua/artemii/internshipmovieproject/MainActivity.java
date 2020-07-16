package ua.artemii.internshipmovieproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

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
     * Navigation Controller for navigate between fragments
     */
    private NavController navController;

    /**
     * Toast for back button
     * @see MainActivity#onBackPressed()
     */
    private Toast backToast;
    /**
     * Timer for count pause between clicking
     * @see MainActivity#onBackPressed()
     */
    private long backPressedTime;
    /**
     * Default class TAG
     */
    public static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding
                = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.video_list_nav_host_fragment);
    }

    /**
     * Navigate from videoListFragment to detailVideoInfoFragment
     */
    public void goToDetailVideoInfoFragment() {
        navController.navigate(R.id.action_videoListFragment_to_detailVideoInfoFragment);
    }

    /**
     * Navigate from detailVideoInfoFragment to descriptionVideoFragment
     */
    public void goToDescriptionVideoFragment() {
        navController.navigate(R.id.action_detailVideoInfoFragment_to_descriptionVideoFragment);
    }

    /**
     * Asks navController about current fragment
     * If it is a start fragment, toast message offer user to tep again for exit
     * Else method calls popOnBackStack to back on previous fragment
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
       if (navController
               .getCurrentDestination()
                    .getDisplayName()
                        .equals("ua.artemii.internshipmovieproject:id/videoListFragment")) {
           if (backPressedTime + 2000 > System.currentTimeMillis()) {
               backToast.cancel();
               super.onBackPressed();
               return;
           } else {
               backToast = Toast.makeText(getBaseContext(), "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT);
               backToast.show();
           }
           backPressedTime = System.currentTimeMillis();
       } else {
           navController.popBackStack();
       }
    }
}
