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

@Dao
public interface VideoListInfoDao {
    @Query(DatabaseHelper.GET_BY_KEY_WORD_QUERY)
    Flowable<List<VideoListInfoModel>> getAllByKeyWord(String keyWord);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoListInfoModel user);

    @Query(DatabaseHelper.DELETE_ALL_QUERY)
    Completable deleteAll();
}
