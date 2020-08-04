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
import ua.artemii.internshipmovieproject.services.DisposableService;
import ua.artemii.internshipmovieproject.values.StringValues;

public class VideoListInfoViewModel extends ViewModel {

    private static final String TAG = VideoListInfoViewModel.class.getCanonicalName();
    private MutableLiveData<List<VideoListInfoModel>> videos = new MutableLiveData<>();
    private MutableLiveData<Throwable> throwable = new MutableLiveData<>();
    private VideoRepository repository = VideoRepository.getInstance();
    private Disposable dbLoadDisposable;

    public MutableLiveData<List<VideoListInfoModel>> getVideos() {
        return videos;
    }

    public MutableLiveData<Throwable> getThrowable() {
        return throwable;
    }

    public VideoListInfoViewModel() {
        if (videos.getValue() == null) {
            loadVideoList(StringValues.DEFAULT_WORD);
        }
    }

    public void loadVideoList(String keyWord) {
        Log.i(TAG, "Calling repository load method from VideoListInfoViewModel");
        cacheVideoList(keyWord);
        if (dbLoadDisposable != null && !dbLoadDisposable.isDisposed()) {
            dbLoadDisposable.dispose();
        }
        loadVideoListFromDatabase(keyWord);
    }

    private void loadVideoListFromDatabase(String keyWord) {
        dbLoadDisposable = repository.loadVideoListFromDatabase(keyWord)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(t -> throwable.setValue(t))
                .subscribe(list -> videos.setValue(list));

    }

    private void cacheVideoList(String keyWord) {
        repository.loadVideoListFromApi(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        DisposableService.add(d);
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        throwable.setValue(e);
                    }
                });
    }
}
