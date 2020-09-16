package ua.artemii.internshipmovieproject.values;

/**
 * Class with room database request constant
 */
public final class DatabaseHelper {
    public static final String DATABASE_NAME = "video_database";
    public static final String VIDEO_LIST_TABLE = "video_list";
    public static final String GET_BY_KEY_WORD_QUERY = "SELECT * FROM " + VIDEO_LIST_TABLE + " WHERE key_word = :keyWord";
    public static final String DELETE_ALL_QUERY = "DELETE FROM " + VIDEO_LIST_TABLE;
}
