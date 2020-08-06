package ua.artemii.internshipmovieproject.database;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ua.artemii.internshipmovieproject.dao.VideoListInfoDao;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.values.DatabaseHelper;

/**
 * Default singleton room class
 * Entities:
 * @see VideoListInfoModel
 */
@Database(entities = {VideoListInfoModel.class}, version = 1, exportSchema = false)
public abstract class VideoDatabase extends RoomDatabase {

    private static VideoDatabase instance;

    /**
     * Getting single instance of room db
     * @return db instance
     */
    public static VideoDatabase getInstance() {
        return instance;
    }

    /**
     * Set context at MainApp and crate instance of db
     * @param context - application context
     */
    public synchronized static void setContext(@Nullable Context context) {
        if (instance == null && context != null) {
            instance = Room.databaseBuilder(
                    context, VideoDatabase.class,
                    DatabaseHelper.DATABASE_NAME)
                    .enableMultiInstanceInvalidation()
                    .build();
        }
    }

    /**
     * Video list dao getter to take it's method
     * @return video list dao
     */
    public abstract VideoListInfoDao videoListDao();
}
