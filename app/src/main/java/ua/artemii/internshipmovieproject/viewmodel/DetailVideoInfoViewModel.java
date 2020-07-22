package ua.artemii.internshipmovieproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;
import ua.artemii.internshipmovieproject.viewmodel.listeners.DetailVideoInfoLoadListener;

public class DetailVideoInfoViewModel extends ViewModel {
    private static final String TAG = DetailVideoInfoViewModel.class.getCanonicalName();
    private MutableLiveData<DetailVideoInfoModel> videos = new MutableLiveData<>();
    private MutableLiveData<Throwable> throwable = new MutableLiveData<>();

    public LiveData<Throwable> getThrowable() {
        return throwable;
    }

    public LiveData<DetailVideoInfoModel> getVideos() {
        return videos;
    }

    public void loadDetailVideoInfo(String id, String plot) {
        if (videos.getValue() == null) {
            Log.i(TAG, "Calling repository load method from DetailVideoInfoViewModel");
            VideoRepository.getInstance().loadDetailVideoInfo(id, plot, new DetailVideoInfoLoadListener() {
                @Override
                public void detailVideoDataLoad(DetailVideoInfoModel detailVideoInfo) {
                    videos.setValue(detailVideoInfo);
                }

                @Override
                public void detailVideoDataFailed(Throwable t) {
                    throwable.setValue(t);
                }
            });
        }
    }
}
