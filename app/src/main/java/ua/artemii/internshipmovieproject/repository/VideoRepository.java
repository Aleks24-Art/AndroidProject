package ua.artemii.internshipmovieproject.repository;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;
import ua.artemii.internshipmovieproject.services.VideoLoadService;
import ua.artemii.internshipmovieproject.viewmodel.listeners.DetailVideoInfoLoadListener;
import ua.artemii.internshipmovieproject.viewmodel.listeners.VideoListLoadListener;

public class VideoRepository {

    private static final String TAG = VideoRepository.class.getCanonicalName();
    private static VideoRepository instance;
    private VideoLoadService service;

    private VideoRepository() {
        service =  VideoLoadService.getInstance();
    }

    public static VideoRepository getInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }

    public void loadDetailVideoInfo(String id, String plot, DetailVideoInfoLoadListener listener) {
        Log.i(TAG, "Starting downloading data for detail video fragment");
                service.getService()
                .getDetailVideoInfo(id, plot)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailVideoInfoModel>() {
                    DetailVideoInfoModel videoInfo;
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DetailVideoInfoModel value) {
                        videoInfo = value;
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.detailVideoDataFailed(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.detailVideoDataLoad(videoInfo);
                    }
                });
    }

    public void loadVideoListInfo(String keyWord, VideoListLoadListener listener) {

        service.getService().getVideoListInfo(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Search>() {
                    Search search = new Search();
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Search value) {
                search = value;
            }

            @Override
            public void onError(Throwable e) {
                listener.videoListDataFailed(e);
            }

            @Override
            public void onComplete() {
                listener.videoListDataLoad(search.getVideoListInfoModelList());
            }
        });
    }
}
