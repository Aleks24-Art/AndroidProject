package ua.artemii.internshipmovieproject.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import ua.artemii.internshipmovieproject.values.DatabaseHelper;

@Entity(tableName = DatabaseHelper.VIDEO_LIST_TABLE)
public class VideoListInfoModel {

    @NonNull
    @PrimaryKey
    @SerializedName("imdbID")
    @ColumnInfo(name = "id")
    private String imdbID;
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("Poster")
    private String poster;
    private String actors;
    @ColumnInfo(name = "key_word")
    private String keyWord;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @NotNull
    @Override
    public String toString() {
        return "VideoListInfoModel{" +
                "imdbID='" + imdbID + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", poster='" + poster + '\'' +
                ", actors='" + actors + '\'' +
                ", keyWord='" + keyWord + '\'' +
                '}';
    }
}
