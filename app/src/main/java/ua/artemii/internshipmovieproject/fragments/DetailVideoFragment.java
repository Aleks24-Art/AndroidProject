package ua.artemii.internshipmovieproject.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.FragmentDetailVideoInfoBinding;
import ua.artemii.internshipmovieproject.viewmodel.DetailVideoInfoViewModel;

public class DetailVideoFragment extends Fragment {
    private static final String TAG =
            DetailVideoFragment.class.getCanonicalName();
    private static final String PLOT_TYPE = "full";

    private FragmentDetailVideoInfoBinding detailVideoInfoBinding;
    private DetailVideoInfoViewModel videosVM;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private HlsMediaSource hlsMediaSource;
    private static final String VIDEO_URI = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
    public static long currentPlayerPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videosVM =
                new ViewModelProvider(this).get(DetailVideoInfoViewModel.class);

        updateDetailVideoInfo();
        updateDownloadState();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (detailVideoInfoBinding != null) {
            return detailVideoInfoBinding.getRoot();
        }
        detailVideoInfoBinding =
                FragmentDetailVideoInfoBinding.inflate(inflater, container, false);

        // Player view initialize
        int orientation = getResources().getConfiguration().orientation;
        playerView =
                orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? detailVideoInfoBinding.playerViewLandscape : detailVideoInfoBinding.playerViewPortrait;

        return detailVideoInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            DetailVideoFragmentArgs args =
                    DetailVideoFragmentArgs.fromBundle(getArguments());
            videosVM.loadDetailVideoInfo(args.getImdbID(), PLOT_TYPE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getContext() != null) {
            initPlayer();
            startPlayer();
        } else {
            Log.e(TAG, "Context is null");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player.isPlaying()) {
            currentPlayerPosition = player.getCurrentPosition();
        }
        playerView.setPlayer(null);
        player.release();
        player = null;
        hlsMediaSource = null;
    }

    private void updateDetailVideoInfo() {
        videosVM.getVideos().observe(this, detailVideoInfo -> {
            loadBigPoster(detailVideoInfo.getPosterUrl());
            detailVideoInfoBinding.bigTitle.setText(detailVideoInfo.getTitle());
            detailVideoInfoBinding.releasedInfo.setText((detailVideoInfo.getReleased()));
            detailVideoInfoBinding.actorsInfo.setText(detailVideoInfo.getActors());
            detailVideoInfoBinding.countryInfo.setText(detailVideoInfo.getCountry());
            detailVideoInfoBinding.genreInfo.setText(detailVideoInfo.getGenre());
            detailVideoInfoBinding.runtimeInfo.setText(detailVideoInfo.getRuntime());
            detailVideoInfoBinding.typeInfo.setText(detailVideoInfo.getType());

            detailVideoInfoBinding.btnToVideoDescription.setOnClickListener(v -> {
                DetailVideoFragmentDirections.ActionDetailVideoInfoFragmentToDescriptionVideoFragment action =
                        DetailVideoFragmentDirections.actionDetailVideoInfoFragmentToDescriptionVideoFragment(detailVideoInfo.getPlot());
                Navigation.findNavController(v).navigate(action);
            });
        });
    }

    private void updateDownloadState() {
        videosVM.getThrowable().observe(this, throwable -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Download error", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Download error: ", throwable);
            }
        });
    }

    private void loadBigPoster(String posterUrl) {
        Log.d(TAG, "LoadBigPoster!!!");
        if (getContext() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .load(posterUrl)
                    .error(R.drawable.img_poster_default)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(detailVideoInfoBinding.icVideoPosterBig);
        }
    }

    private void playVideoPortrait() {
        // Set some settings to have a correct view
        detailVideoInfoBinding.icVideoPosterBig.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        //Starting video from last checked pos
        player.seekTo(currentPlayerPosition);
        player.prepare(hlsMediaSource, false, false);
        player.setPlayWhenReady(true);
    }

    private void playVideoLandscape() {
        // Set some settings to have a correct view
        detailVideoInfoBinding.svVideoInfo.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        //Starting video from last checked pos
        player.seekTo(currentPlayerPosition);
        player.prepare(hlsMediaSource, false, false);
        player.setPlayWhenReady(true);
    }

    private void stopVideo() {
        // Set current pos before stop, to continue from it
        currentPlayerPosition = player.getCurrentPosition();
        player.stop();
    }

    private void initPlayer() {
        // Create a player instance.
        player = new SimpleExoPlayer.Builder(getContext()).build();

        //Set player on player view
        playerView.setPlayer(player);

        //Make playerView invisible to see a picture
        playerView.setVisibility(View.INVISIBLE);

        // Create a data source factory.
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(getContext(), "app-name"));

        // Create a HLS media source pointing to a playlist uri.
        hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(VIDEO_URI));

        // Setting action on play button
        detailVideoInfoBinding.playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Playing is : " + player.isPlaying());
                if (player.isPlaying()) {
                    // Set stop action on btn, if we have already started video
                    stopVideo();
                } else {
                    // Continue or start playing video
                    playVideoPortrait();
                }
            }
        });
    }

    private void startPlayer() {
        // Get current orientation and play video depending on it
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && currentPlayerPosition != 0) {
            playVideoLandscape();
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT && currentPlayerPosition != 0) {
            playVideoPortrait();
        }
    }

}
