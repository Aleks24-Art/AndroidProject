package ua.artemii.internshipmovieproject.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("Search")
    private List<ShortDescVideo> shortDescVideoList;

    public Search(List<ShortDescVideo> shortDescVideoList) {
        this.shortDescVideoList = shortDescVideoList;
    }

    public List<ShortDescVideo> getShortDescVideoList() {
        return shortDescVideoList;
    }

    public void setShortDescVideoList(List<ShortDescVideo> shortDescVideoList) {
        this.shortDescVideoList = shortDescVideoList;
    }
}