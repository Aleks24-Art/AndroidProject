package ua.artemii.internshipmovieproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.repository.VideoRepository;

public class DetailVideoInfoViewModel extends ViewModel {
    public static final String TAG = DetailVideoInfoViewModel.class.getCanonicalName();
    private MutableLiveData<DetailVideoInfoModel> videos = new MutableLiveData<>();

    public LiveData<DetailVideoInfoModel> getDetailVideoInfo(String id, String plot) {
        if (videos.getValue() == null) {
            VideoRepository.getInstance().loadDetailVideoInfo(id, plot, detailVideoInfo -> videos.postValue(detailVideoInfo));
        }
        return videos;
    }
}
