package ua.artemii.internshipmovieproject.services;

import androidx.annotation.Nullable;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.artemii.internshipmovieproject.model.DetailVideoInfoModel;
import ua.artemii.internshipmovieproject.model.Search;

/**
 * Service to for download data from API using retrofit 2
 */
public interface VideoItemService {

    /**
     * Personal API key to make request
     */
    String API_KEY = "?apikey=56ecf249&";

    /**
     * Load Search item with video list inside
     *
     * @param keyWord pararm for request
     * @return search observable
     */
    @GET(API_KEY)
    Observable<Search> getVideoListInfo(@Nullable @Query("s") String keyWord);

    /**
     * Load detail video info item
     *
     * @param imdbID pararm for request
     * @param plot   pararm for request
     * @return detail video info observable
     */
    @GET(API_KEY)
    Observable<DetailVideoInfoModel> getDetailVideoInfo(
            @Query("i") String imdbID,
            @Query("plot") String plot);
}
