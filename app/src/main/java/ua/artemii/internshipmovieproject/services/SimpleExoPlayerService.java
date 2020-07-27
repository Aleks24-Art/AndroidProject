package ua.artemii.internshipmovieproject.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class SimpleExoPlayerService {

    private static final String TAG = SimpleExoPlayerService.class.getCanonicalName();
    private static SimpleExoPlayerService instance;
    private static Context context;
    private static SimpleExoPlayer player;
    private static long currentPlayerPosition;
    private static HlsMediaSource hlsMediaSource;
    private static final String VIDEO_URI =
            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";

    private SimpleExoPlayerService() {
        Log.i(TAG, "SimpleExoPlayerService constructor is called");
        player = new SimpleExoPlayer.Builder(context).build();
        Log.i(TAG, "SimpleExoPlayer " + player);

        // Create a data source factory.
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(context, "app-name"));

        // Create a HLS media source pointing to a playlist uri.
        hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(VIDEO_URI));
    }

    public static SimpleExoPlayerService getInstance() {
        if (instance == null) {
            instance = new SimpleExoPlayerService();
        }

        return instance;
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void preparePlayer() {
        player.seekTo(currentPlayerPosition);
        player.prepare(hlsMediaSource, false, false);
        player.setPlayWhenReady(true);
    }

    public static void releasePlayer() {
        if (player != null) {
            currentPlayerPosition = player.getCurrentPosition();
            player.release();
            //player = null;
            //hlsMediaSource = null;
        }
    }

    public void updateCurrentPlayerPosition() {
        currentPlayerPosition = player.getCurrentPosition();
    }

    public long getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    public static void setContext(Context c) {
        context = c;
    }

    public static void zeroingPlayerPosition() {
        currentPlayerPosition = 0;
    }
}
