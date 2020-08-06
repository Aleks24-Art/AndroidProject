package ua.artemii.internshipmovieproject;

import android.app.Application;

import ua.artemii.internshipmovieproject.database.VideoDatabase;
import ua.artemii.internshipmovieproject.services.VideoPlayer;

/**
 * Application class to init player and room database
 */
public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VideoPlayer.getInstance().initPlayer(getApplicationContext());
        VideoDatabase.setContext(getApplicationContext());
    }
}
