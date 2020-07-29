package ua.artemii.internshipmovieproject.repository;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.services.VideoLoadService;

public class VideoRepository {

    private static final String TAG = VideoRepository.class.getCanonicalName();
    private static VideoRepository instance;
    private VideoLoadService service;

    private VideoRepository() {
        service = VideoLoadService.getInstance();
    }

    public static VideoRepository getInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }

    public Observable<DetailVideoInfoModel> loadDetailVideoInfo(String id, String plot) {
        Log.i(TAG, "Starting downloading data for detail video fragment");
        return service.getService()
                .getDetailVideoInfo(id, plot)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<VideoListInfoModel>> loadVideoListInfo(String keyWord) {
        Log.i(TAG, "Starting downloading data for video list fragment");
        return service.getService()
                .getVideoListInfo(keyWord)
                .map(Search::getVideoListInfoModelList)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
