package ua.artemii.internshipmovieproject.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoLoadService {

    private static VideoLoadService instance;
    private static final String BASE_URL = "http://www.omdbapi.com/";

    private Retrofit retrofit;

    private VideoLoadService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static VideoLoadService getInstance() {
        if (instance == null) {
            instance = new VideoLoadService();
        }
        return instance;
    }

    public VideoItemService getVideoItemService() {
        return retrofit.create(VideoItemService.class);
    }


}
