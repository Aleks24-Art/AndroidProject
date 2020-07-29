package ua.artemii.internshipmovieproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;
import ua.artemii.internshipmovieproject.services.DisposableService;

public class DetailVideoInfoViewModel extends ViewModel {
    private static final String TAG = DetailVideoInfoViewModel.class.getCanonicalName();
    private MutableLiveData<DetailVideoInfoModel> videos = new MutableLiveData<>();
    private MutableLiveData<Throwable> throwable = new MutableLiveData<>();

    public LiveData<DetailVideoInfoModel> getVideos() {
        return videos;
    }

    public LiveData<Throwable> getThrowable() {
        return throwable;
    }

    public void loadDetailVideoInfo(String id, String plot) {
        if (videos.getValue() == null) {
            Log.i(TAG, "Calling repository load method from DetailVideoInfoViewModel");
            VideoRepository.getInstance()
                    .loadDetailVideoInfo(id, plot)
                    .subscribe(new Observer<DetailVideoInfoModel>() {
                @Override
                public void onSubscribe(Disposable d) {
                    DisposableService.add(d);
                }

                @Override
                public void onNext(DetailVideoInfoModel videoInfo) {
                    videos.setValue(videoInfo);
                }

                @Override
                public void onError(Throwable t) {
                    throwable.setValue(t);
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }
}
