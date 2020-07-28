package ua.artemii.internshipmovieproject;

import android.app.Application;

import ua.artemii.internshipmovieproject.services.SimpleExoPlayerService;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleExoPlayerService.getInstance().initPlayer(getApplicationContext());
    }
}
