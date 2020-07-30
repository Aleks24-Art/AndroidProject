package ua.artemii.internshipmovieproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;
import ua.artemii.internshipmovieproject.services.DisposableService;

public class VideoListInfoViewModel extends ViewModel {

    private static final String TAG = VideoListInfoViewModel.class.getCanonicalName();
    private MutableLiveData<List<VideoListInfoModel>> videos = new MutableLiveData<>();
    private MutableLiveData<Throwable> throwable = new MutableLiveData<>();

    public LiveData<List<VideoListInfoModel>> getVideos() {
        return videos;
    }

    public LiveData<Throwable> getThrowable() {
        return throwable;
    }

    public void loadVideoList(String keyWord) {
        if (videos.getValue() == null) {
            Log.i(TAG, "Calling repository load method from VideoListInfoViewModel");
            VideoRepository.getInstance()
                    .loadVideoListInfo(keyWord)
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<VideoListInfoModel>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    DisposableService.add(d);
                    Log.d(TAG, "onSubscribe: " + Thread.currentThread().getName());
                }

                @Override
                public void onNext(List<VideoListInfoModel> videoList) {
                    videos.setValue(videoList);
                    Log.d(TAG, "onNext: "  + Thread.currentThread().getName());
                }

                @Override
                public void onError(Throwable t) {
                    throwable.setValue(t);
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: " + Thread.currentThread().getName());
                }
            });
        }
    }
}
