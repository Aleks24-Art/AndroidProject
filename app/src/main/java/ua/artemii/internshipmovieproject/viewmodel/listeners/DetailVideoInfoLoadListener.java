package ua.artemii.internshipmovieproject.viewmodel.listeners;

import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;

public interface DetailVideoInfoLoadListener {
    void detailVideoDataLoad(DetailVideoInfoModel detailVideoInfo);
    void detailVideoDataFailed(Throwable t);
}
