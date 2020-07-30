package ua.artemii.internshipmovieproject.repository;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.services.VideoLoadService;
import ua.artemii.internshipmovieproject.values.StringValues;

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
        return service.getService().getDetailVideoInfo(id, plot);
    }

    public Single<List<VideoListInfoModel>> loadVideoListInfo(String keyWord) {
        return service.getService()
                .getVideoListInfo(keyWord)
                .map(Search::getVideoListInfoModelList)
                .flatMap(Observable::fromIterable)
                .flatMap(videoListInfoModel ->
                        service.getService()
                                .getDetailVideoInfo(videoListInfoModel.getImdbID(), StringValues.PLOT_TYPE)
                                .subscribeOn(Schedulers.io())
                                .map(detailVideoInfoModel -> {
                                    Log.d(TAG, "Thread — " + Thread.currentThread().getName());
                                    videoListInfoModel.setActors(detailVideoInfoModel.getActors());
                                    return videoListInfoModel;
                                }))
                .toList();
    }
}
