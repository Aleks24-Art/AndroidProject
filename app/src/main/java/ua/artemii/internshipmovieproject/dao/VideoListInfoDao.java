package ua.artemii.internshipmovieproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.values.DatabaseHelper;
import ua.artemii.internshipmovieproject.repository.VideoRepository;

/**
 * Dao for work with video list items
 * @see VideoListInfoModel
 */
@Dao
public interface VideoListInfoDao {
    /**
     * Get list of videos from database using keyWord
     * @param keyWord video list item param
     * @return if no such keyWord in db return empty list,
     * else return list of VideoListInfoModel
     */
    @Query(DatabaseHelper.GET_BY_KEY_WORD_QUERY)
    Flowable<List<VideoListInfoModel>> getAllByKeyWord(String keyWord);

    /**
     * Insert video to db
     * If video is already exists — replace it
     * Using single insert cause
     * @see VideoRepository
     * @param video list item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoListInfoModel video);

    /**
     * Delete all info from db
     * @return Completable to subscribe and get onComplete
     */
    @Query(DatabaseHelper.DELETE_ALL_QUERY)
    Completable deleteAll();
}
