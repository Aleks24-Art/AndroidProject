package ua.artemii.internshipmovieproject.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.FragmentDetailVideoInfoBinding;
import ua.artemii.internshipmovieproject.services.SimpleExoPlayerService;
import ua.artemii.internshipmovieproject.viewmodel.DetailVideoInfoViewModel;

public class DetailVideoFragment extends Fragment {

    private static final String TAG =
            DetailVideoFragment.class.getCanonicalName();

    private static final String PLOT_TYPE = "full";
    private FragmentDetailVideoInfoBinding detailVideoInfoBinding;
    private DetailVideoInfoViewModel videosVM;
    private PlayerView playerView;
    private SimpleExoPlayerService playerService;
    private int orientation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        videosVM =
                new ViewModelProvider(this).get(DetailVideoInfoViewModel.class);

        // Create a player instance.
        playerService = SimpleExoPlayerService.getInstance();

        updateDetailVideoInfo();
        updateDownloadState();

        if (getArguments() != null) {
            DetailVideoFragmentArgs args =
                    DetailVideoFragmentArgs.fromBundle(getArguments());
            videosVM.loadDetailVideoInfo(args.getImdbID(), PLOT_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (detailVideoInfoBinding != null) {
            return detailVideoInfoBinding.getRoot();
        }
        detailVideoInfoBinding =
                FragmentDetailVideoInfoBinding
                        .inflate(inflater, container, false);

        // Orientation initialize
        orientation = getResources().getConfiguration().orientation;

        // Player view initialize
        playerView =
                orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? detailVideoInfoBinding.playerViewLandscape : detailVideoInfoBinding.playerViewPortrait;

        return detailVideoInfoBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getContext() != null) {
            initPlayer();
        } else {
            Log.e(TAG, "Context is null");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        playerService.updateCurrentPlayerPosition();
        playerView.setPlayer(null);
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
                playerService.getPlayer().setPlayWhenReady(false);
                DetailVideoFragmentDirections.ActionDetailVideoInfoFragmentToDescriptionVideoFragment action =
                        DetailVideoFragmentDirections.actionDetailVideoInfoFragmentToDescriptionVideoFragment(detailVideoInfo.getPlot());
                Navigation.findNavController(v).navigate(action);
            });
        });
    }

    //ok
    private void updateDownloadState() {
        videosVM.getThrowable().observe(this, throwable -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Download error", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Download error: ", throwable);
            }
        });
    }
    //ok
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
    //ok
    @SuppressLint("SetTextI18n")
    private void initPlayer() {

        playerService.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    detailVideoInfoBinding.icVideoPosterBig.setVisibility(View.VISIBLE);
                    playerView.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Set player on player view
        playerView.setPlayer(playerService.getPlayer());

        //Make playerView invisible to see a picture
        playerView.setVisibility(View.INVISIBLE);

        // Setting action on play button
        detailVideoInfoBinding.playVideo.setOnClickListener(v -> {
            // Set current pos before pause/play, to continue from it
            if (playerService.getPlayer().isPlaying()) {
                playerService.updateCurrentPlayerPosition();
                // Set stop action on btn, if we have already started video
                detailVideoInfoBinding.playVideo.setText("Play");
                playerService.getPlayer().stop();
            } else {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE && playerService.getCurrentPlayerPosition() == 0) {
                    playVideoLandscape();
                } else  {
                    // Continue or start playing video
                    detailVideoInfoBinding.playVideo.setText("Pause");
                    playVideoPortrait();
                }
            }
        });
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && playerService.getCurrentPlayerPosition() != 0) {
            playVideoLandscape();
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT && playerService.getCurrentPlayerPosition() != 0) {
            playVideoPortrait();
        }
    }

    //ok
    private void playVideoPortrait() {
        // Set some settings to have a correct view
        detailVideoInfoBinding.icVideoPosterBig.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        //Starting video from last checked pos
        playerService.preparePlayer();
    }
    //ok
    private void playVideoLandscape() {
        // Set some settings to have a correct view
        detailVideoInfoBinding.svVideoInfo.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        //Starting video from last checked pos
        playerService.preparePlayer();
    }
}
