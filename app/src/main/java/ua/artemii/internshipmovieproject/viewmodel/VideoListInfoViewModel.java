package ua.artemii.internshipmovieproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;
import ua.artemii.internshipmovieproject.values.StringValues;

public class VideoListInfoViewModel extends ViewModel {

    private static final String TAG = VideoListInfoViewModel.class.getCanonicalName();
    private MutableLiveData<List<VideoListInfoModel>> videos = new MutableLiveData<>();
    private MutableLiveData<Throwable> throwable = new MutableLiveData<>();
    private VideoRepository repository = VideoRepository.getInstance();
    private Disposable dbLoadDisposable;
    private Disposable cacheDisposable;
    private boolean throwableReadyToShown;

    public MutableLiveData<List<VideoListInfoModel>> getVideos() {
        return videos;
    }

    public MutableLiveData<Throwable> getThrowable() {
        return throwable;
    }

    public VideoListInfoViewModel() {
        loadVideoList(StringValues.DEFAULT_WORD);
    }

    /**
     *  Call repository method to get data from room database
     *  and subscribe to get it
     * @param keyWord param to download
     */
    public void loadVideoList(String keyWord) {
        if (dbLoadDisposable != null && !dbLoadDisposable.isDisposed()) {
            dbLoadDisposable.dispose();
        }
        dbLoadDisposable = repository
                .loadVideoListFromDatabase(keyWord)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(t -> {
                    if (!throwableReadyToShown) {
                        throwable.postValue(t);
                        setThrowableReadyToShown(true);
                    }
                })
                .subscribe(list -> {
                    if (list.isEmpty()) {
                        Log.d(TAG, "getFromApi " + keyWord);
                        cacheVideoList(keyWord);
                    } else {
                        Log.d(TAG, "getFromDb " + keyWord);
                        Log.d(TAG, "List size: " + list.size());
                        videos.postValue(list);
                    }
                });
    }

    /**
     *  Call repository method to download data and cache it to room database
     * @param keyWord param to download
     */
    private void cacheVideoList(String keyWord) {
        if (cacheDisposable != null && !cacheDisposable.isDisposed()) {
            cacheDisposable.dispose();
        }
        repository.loadVideoListFromApi(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        cacheDisposable = d;
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!throwableReadyToShown) {
                            throwable.postValue(e);
                            setThrowableReadyToShown(true);
                        }
                    }
                });
    }

    public boolean isThrowableReadyToShown() {
        return throwableReadyToShown;
    }

    public void setThrowableReadyToShown(boolean throwableReadyToShown) {
        this.throwableReadyToShown = throwableReadyToShown;
    }
}
