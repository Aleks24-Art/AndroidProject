package ua.artemii.internshipmovieproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;

public class VideoListInfoViewModel extends ViewModel {

    public static final String TAG = VideoListInfoViewModel.class.getCanonicalName();

    private MutableLiveData<List<VideoListInfoModel>> videos = new MutableLiveData<>();

    public LiveData<List<VideoListInfoModel>> getVideos(String keyWord) {
        if (videos.getValue() == null) {
            VideoRepository.getInstance().loadVideoListInfo(keyWord, videoList -> videos.postValue(videoList));
    }
        return videos;
    }
}
