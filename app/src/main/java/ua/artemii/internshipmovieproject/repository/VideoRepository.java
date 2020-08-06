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
import ua.artemii.internshipmovieproject.services.VideoApiLoadService;
import ua.artemii.internshipmovieproject.values.StringValues;

/**
 * Class apply Repository pattern
 * Use Room to store data to the local database or, if data does not exist in db, download from API
 */
public class VideoRepository {

    private static final String TAG = VideoRepository.class.getCanonicalName();
    private static VideoRepository instance;
    private VideoApiLoadService service;
    private VideoDatabase db;

    private VideoRepository() {
        service = VideoApiLoadService.getInstance();
        db = VideoDatabase.getInstance();
    }

    /**
     * Getting single instance
     * @return repository instance
     */
    public static VideoRepository getInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }

    /**
     * Load detail video info using retrofit 2
     * @param id video imdbId
     * @param plot type of video plot
     * @return observable detail video info
     */
    public Observable<DetailVideoInfoModel> loadDetailVideoInfo(String id, String plot) {
        return service.getService().getDetailVideoInfo(id, plot);
    }

    /**
     * Load Search item from API than get video list and iterate it
     * after that get imdbId from every item
     * and make async request by to get list of actors
     * @param keyWord for load search item from API
     * @return completable
     */
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

    /**
     * Load data from db
     * @param keyWord to get video list
     * @return flowable video list
     */
    public Flowable<List<VideoListInfoModel>> loadVideoListFromDatabase(String keyWord) {
        return db.videoListDao().getAllByKeyWord(keyWord);
    }

    /**
     * Delete all data from db
     * @return completable
     */
    public Completable deleteAll() {
        return db.videoListDao().deleteAll();
    }
}

