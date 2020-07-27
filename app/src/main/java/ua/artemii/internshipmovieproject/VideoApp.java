package ua.artemii.internshipmovieproject;

import android.app.Application;

import ua.artemii.internshipmovieproject.services.SimpleExoPlayerService;

public class VideoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleExoPlayerService.setContext(getApplicationContext());
    }
}
