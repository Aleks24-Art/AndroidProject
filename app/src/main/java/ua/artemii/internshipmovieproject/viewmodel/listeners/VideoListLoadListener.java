package ua.artemii.internshipmovieproject.viewmodel.listeners;

import java.util.List;

import ua.artemii.internshipmovieproject.model.VideoListInfoModel;

public interface VideoListLoadListener {
    void videoListDataLoad(List<VideoListInfoModel> videoList);
    void videoListDataFailed(Throwable t);
}
