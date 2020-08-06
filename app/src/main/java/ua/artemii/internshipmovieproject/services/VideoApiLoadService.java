package ua.artemii.internshipmovieproject.services;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoApiLoadService {

    private static VideoApiLoadService instance;
    private static final String BASE_URL = "http://www.omdbapi.com/";
    private VideoItemService service;

    private VideoApiLoadService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(VideoItemService.class);
    }

    /**
     * Getting single instance
     * @return load service instance
     */
    public static VideoApiLoadService getInstance() {
        if (instance == null) {
            instance = new VideoApiLoadService();
        }
        return instance;
    }

    public VideoItemService getService() {
        return service;
    }
}
