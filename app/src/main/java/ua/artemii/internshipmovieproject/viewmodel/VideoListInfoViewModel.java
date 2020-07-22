package ua.artemii.internshipmovieproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;
import ua.artemii.internshipmovieproject.viewmodel.listeners.VideoListLoadListener;

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
            VideoRepository.getInstance().loadVideoListInfo(keyWord, new VideoListLoadListener() {
                @Override
                public void videoListDataLoad(List<VideoListInfoModel> videoList) {
                    videos.setValue(videoList);
                }

                @Override
                public void videoListDataFailed(Throwable t) {
                    throwable.setValue(t);
                }
            });
        }
    }
}
