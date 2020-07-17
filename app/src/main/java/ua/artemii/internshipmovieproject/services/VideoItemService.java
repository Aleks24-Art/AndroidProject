package ua.artemii.internshipmovieproject.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.artemii.internshipmovieproject.model.FullDescVideo;
import ua.artemii.internshipmovieproject.model.Search;

public interface VideoItemService {
    static final String API_KEY = "?apikey=56ecf249&";
    @GET(API_KEY + "s=Woman")
    Call<Search> getVideoList();

    @GET(API_KEY)
    Call<FullDescVideo> getFullDescVideo(@Query("i") String imdbID);
}
