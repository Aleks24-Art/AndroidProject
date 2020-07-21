package ua.artemii.internshipmovieproject.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;

public interface VideoItemService {
    String API_KEY = "?apikey=56ecf249&";

    @GET(API_KEY)
    Call<Search> getVideoListInfo(@Query("s") String keyWord);

    @GET(API_KEY)
    Call<DetailVideoInfoModel> getDetailVideoInfo(@Query("i") String imdbID, @Query("plot") String plot);
}
