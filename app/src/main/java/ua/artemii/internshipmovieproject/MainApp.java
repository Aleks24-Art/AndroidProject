package ua.artemii.internshipmovieproject;

import android.app.Application;
import android.util.Log;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.database.VideoDatabase;
import ua.artemii.internshipmovieproject.repository.VideoRepository;
import ua.artemii.internshipmovieproject.services.SimpleExoPlayerService;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleExoPlayerService.getInstance().initPlayer(getApplicationContext());
        VideoDatabase.setContext(getApplicationContext());
        /*VideoRepository.getInstance()
                .deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "Data to remove " );
            }

            @Override
            public void onError(Throwable e) {

            }
        });*/
    }
}
