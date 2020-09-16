package ua.artemii.internshipmovieproject.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ua.artemii.internshipmovieproject.values.StringValues;

/**
 * Video player class which help to create and manipulate player
 */
public class VideoPlayer {

    private static final String TAG = VideoPlayer.class.getCanonicalName();
    private static VideoPlayer instance;
    private static final String VIDEO_URI =
            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
    private HlsMediaSource hlsMediaSource;
    private SimpleExoPlayer player;
    private long currentPlayerPosition;
    private boolean started;

    /**
     * Getting single instance
     * @return video player class instance
     */
    public static VideoPlayer getInstance() {
        if (instance == null) {
            instance = new VideoPlayer();
        }
        return instance;
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    /**
     * Initialize player
     * @param context to init player
     */
    public void initPlayer(Context context) {
        player = new SimpleExoPlayer.Builder(context).build();

        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(context, StringValues.APP_NAME));

        hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(VIDEO_URI));

    }

    /**
     * Prepare player to play from current position
     */
    public void preparePlayer() {
        player.seekTo(currentPlayerPosition);
        player.prepare(hlsMediaSource, false, false);
        player.setPlayWhenReady(true);
    }

    /**
     * Release player
     */
    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            hlsMediaSource = null;
        }
    }

    /**
     * Set current player position
     * to field
     */
    public void updateCurrentPlayerPosition() {
        Log.d(TAG, "CurrentPlayerPosition: " +  player.getCurrentPosition());
        currentPlayerPosition = player.getCurrentPosition();
    }

    /**
     * Zeroing player position to begin from start next time
     */
    public void zeroingPlayerPosition() {
        currentPlayerPosition = 0;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
