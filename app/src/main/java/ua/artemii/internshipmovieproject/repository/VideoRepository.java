package ua.artemii.internshipmovieproject.repository;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.database.VideoDatabase;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.services.VideoLoadService;
import ua.artemii.internshipmovieproject.values.StringValues;

public class VideoRepository {

    private static final String TAG = VideoRepository.class.getCanonicalName();
    private static VideoRepository instance;
    private VideoLoadService service;
    private VideoDatabase db;

    private VideoRepository() {
        service = VideoLoadService.getInstance();
        db = VideoDatabase.getInstance();
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

    public Completable loadVideoListFromApi(String keyWord) {
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
                                    // Setting keyWord to find video list using it
                                    videoListInfoModel.setKeyWord(keyWord);
                                    Log.e(TAG, "Setting data to DB from API");
                                    // Cache data to db
                                    db.videoListDao().insert(videoListInfoModel);
                                    return videoListInfoModel;
                                }))
                .toList()
                .ignoreElement();
    }

    public Flowable<List<VideoListInfoModel>> loadVideoListFromDatabase(String keyWord) {
        return db.videoListDao().getAllByKeyWord(keyWord);
    }

    public Completable deleteAll() {
        return db.videoListDao().deleteAll();
    }
}

