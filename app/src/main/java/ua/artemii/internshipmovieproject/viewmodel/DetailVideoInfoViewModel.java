package ua.artemii.internshipmovieproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;

public class DetailVideoInfoViewModel extends ViewModel {
    private static final String TAG = DetailVideoInfoViewModel.class.getCanonicalName();
    private MutableLiveData<DetailVideoInfoModel> videos = new MutableLiveData<>();
    private MutableLiveData<Throwable> throwable = new MutableLiveData<>();
    private Disposable detailLoadDisposable;
    private boolean throwableReadyToShown;

    public LiveData<DetailVideoInfoModel> getVideos() {
        return videos;
    }

    public LiveData<Throwable> getThrowable() {
        return throwable;
    }

    public void loadDetailVideoInfo(String id, String plot) {
        if (videos.getValue() == null) {
            if (detailLoadDisposable != null && !detailLoadDisposable.isDisposed()) {
                detailLoadDisposable.dispose();
            }
            Log.i(TAG, "Calling repository load method from DetailVideoInfoViewModel");
            VideoRepository.getInstance()
                    .loadDetailVideoInfo(id, plot)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DetailVideoInfoModel>() {
                        @Override
                        public void onSubscribe(Disposable d){
                            detailLoadDisposable = d;
                        }

                        @Override
                        public void onNext(DetailVideoInfoModel videoInfo) {
                            videos.setValue(videoInfo);
                        }

                        @Override
                        public void onError(Throwable t) {
                            if (!throwableReadyToShown) {
                                throwable.postValue(t);
                                setThrowableReadyToShown(true);
                            }
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    public boolean isThrowableReadyToShown() {
        return throwableReadyToShown;
    }

    public void setThrowableReadyToShown(boolean throwableReadyToShown) {
        this.throwableReadyToShown = throwableReadyToShown;
    }
}
