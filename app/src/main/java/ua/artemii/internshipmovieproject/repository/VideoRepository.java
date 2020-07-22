package ua.artemii.internshipmovieproject.repository;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;
import ua.artemii.internshipmovieproject.services.VideoLoadService;
import ua.artemii.internshipmovieproject.viewmodel.listeners.DetailVideoInfoLoadListener;
import ua.artemii.internshipmovieproject.viewmodel.listeners.VideoListLoadListener;

public class VideoRepository {

    private static final String TAG = VideoRepository.class.getCanonicalName();
    private static VideoRepository instance;

    private VideoRepository() {}

    public static VideoRepository getInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }

    public void loadDetailVideoInfo(String id, String plot, DetailVideoInfoLoadListener listener) {
        Log.i(TAG, "Starting downloading data for detail video fragment");
        VideoLoadService.getInstance()
                .getVideoItemService()
                .getDetailVideoInfo(id, plot)
                .enqueue(new Callback<DetailVideoInfoModel>() {
                    @Override
                    public void onResponse(@NotNull Call<DetailVideoInfoModel> call, @NotNull Response<DetailVideoInfoModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listener.detailVideoDataLoad(response.body());
                        } else {
                            listener.detailVideoDataFailed(new Throwable("Request failed"));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<DetailVideoInfoModel> call, @NotNull Throwable t) {
                        Log.e(TAG, "Request failed " + t);
                        listener.detailVideoDataFailed(t);
                    }
                });
    }

    public void loadVideoListInfo(String keyWord, VideoListLoadListener listener) {
        Log.i(TAG, "Starting downloading data for video list fragment");
        VideoLoadService.getInstance()
                .getVideoItemService()
                .getVideoListInfo(keyWord)
                .enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(@NotNull Call<Search> call, @NotNull Response<Search> response) {
                        if (response.isSuccessful() && response.body()  != null && response.body().getVideoListInfoModelList().size() > 0) {
                            listener.videoListDataLoad(response.body().getVideoListInfoModelList());
                        } else {
                            listener.videoListDataFailed(new Throwable("Request failed"));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Search> call, @NotNull Throwable t) {
                        listener.videoListDataFailed(t);
                    }
                });
    }
}
