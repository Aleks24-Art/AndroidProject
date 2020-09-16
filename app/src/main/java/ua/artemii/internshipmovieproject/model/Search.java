package ua.artemii.internshipmovieproject.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Default POJO wrapper class to download video list
 */
public class Search {
    @SerializedName("Search")
    private List<VideoListInfoModel> videoListInfoModelList;

    public List<VideoListInfoModel> getVideoListInfoModelList() {
        return videoListInfoModelList;
    }

    public void setVideoListInfoModelList(List<VideoListInfoModel> videoListInfoModelList) {
        this.videoListInfoModelList = videoListInfoModelList;
    }
}