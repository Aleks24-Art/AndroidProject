package ua.artemii.internshipmovieproject.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("Search")
    private List<VideoListInfoModel> videoListInfoModelList;

    public Search(List<VideoListInfoModel> videoListInfoModelList) {
        this.videoListInfoModelList = videoListInfoModelList;
    }

    public List<VideoListInfoModel> getVideoListInfoModelList() {
        return videoListInfoModelList;
    }
}