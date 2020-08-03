package ua.artemii.internshipmovieproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ua.artemii.internshipmovieproject.dao.VideoListInfoDao;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.values.DatabaseHelper;

@Database(entities = {VideoListInfoModel.class}, version = 1, exportSchema = false)
public abstract class VideoDatabase extends RoomDatabase {

    private static VideoDatabase instance;

    public abstract VideoListInfoDao videoListDao();

    public static VideoDatabase getInstance() {
        return instance;
    }

    public synchronized static void setContext(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context, VideoDatabase.class,
                    DatabaseHelper.DATABASE_NAME)
                    .enableMultiInstanceInvalidation()
                    .build();
        }
    }
}
