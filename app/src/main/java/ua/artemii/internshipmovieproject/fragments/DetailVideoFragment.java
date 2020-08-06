package ua.artemii.internshipmovieproject.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import ua.artemii.internshipmovieproject.services.VideoPlayer;
import ua.artemii.internshipmovieproject.values.StringValues;
import ua.artemii.internshipmovieproject.viewmodel.DetailVideoInfoViewModel;

/**
 * To load from API detail info about video
 * If connection is not available — show appropriate view,
 * else start download info
 */
public class DetailVideoFragment extends Fragment {

    private static final String TAG = DetailVideoFragment.class.getCanonicalName();
    private FragmentDetailVideoInfoBinding detailVideoInfoBinding;
    private DetailVideoInfoViewModel videosVM;
    private PlayerView playerView;
    private VideoPlayer playerService;
    private int orientation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = getResources().getConfiguration().orientation;
        playerService = VideoPlayer.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (detailVideoInfoBinding != null) {
            return detailVideoInfoBinding.getRoot();
        }
        detailVideoInfoBinding =
                FragmentDetailVideoInfoBinding.inflate(inflater, container, false);

        playerView =
                orientation == Configuration.ORIENTATION_LANDSCAPE
                ? detailVideoInfoBinding.playerViewLandscape : detailVideoInfoBinding.playerViewPortrait;

        return detailVideoInfoBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videosVM = new ViewModelProvider(this).get(DetailVideoInfoViewModel.class);

        if (!isNetworkConnected()) {
            detailVideoInfoBinding.svVideoInfo.setVisibility(View.INVISIBLE);
            detailVideoInfoBinding.noConnection.setVisibility(View.VISIBLE);
            return;
        }

        // Subscribe on live data
        updateDetailVideoInfo();
        updateDownloadState();


        if (getArguments() != null) {
            DetailVideoFragmentArgs args = DetailVideoFragmentArgs.fromBundle(getArguments());
            videosVM.loadDetailVideoInfo(args.getImdbID(), StringValues.PLOT_TYPE);
        }

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
    public void onPause() {
        super.onPause();
        VideoPlayer.getInstance().getPlayer().stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (VideoPlayer.getInstance().isStarted()) {
            playerService.updateCurrentPlayerPosition();
        }
        playerView.setPlayer(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detailVideoInfoBinding.noConnection.setVisibility(View.GONE);
        detailVideoInfoBinding.svVideoInfo.setVisibility(View.VISIBLE);
    }

    private void updateDetailVideoInfo() {
        videosVM.getVideos().observe(getViewLifecycleOwner(), detailVideoInfo -> {
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

    private void updateDownloadState() {
        videosVM.getThrowable().observe(getViewLifecycleOwner(), throwable -> {
            if (getContext() != null && videosVM.isThrowableReadyToShown()) {
                Toast.makeText(getContext(), "Download error", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Download error: ", throwable);
                videosVM.setThrowableReadyToShown(false);
            }
        });
    }

    private void loadBigPoster(String posterUrl) {
        Log.d(TAG, "Load big poster with url: " + posterUrl);
        if (getContext() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .load(posterUrl)
                    .error(R.drawable.img_poster_default)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(detailVideoInfoBinding.icVideoPosterBig);
        }
    }

    private void initPlayer() {
        //Adding player listener
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
            if (playerService.isStarted()) {
                playerService.updateCurrentPlayerPosition();
            }
            // Set current pos before pause/play, to continue from it
            if (playerService.getPlayer().isPlaying()) {
                // Set stop action on btn, if we have already started video
                detailVideoInfoBinding.playVideo.setText(StringValues.PLAY);
                playerService.getPlayer().stop();
                //playerService.setStarted(false);
            } else {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    playVideoLandscape();
                    playerService.setStarted(true);
                } else {
                    // Continue or start playing video
                    detailVideoInfoBinding.playVideo.setText(StringValues.PAUSE);
                    playVideoPortrait();
                    playerService.setStarted(true);
                }
            }

        });
        //Continue playing video depend on orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && playerService.isStarted()) {
            playVideoLandscape();
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT && playerService.isStarted()) {
            playVideoPortrait();
        }
    }

    private void playVideoPortrait() {
        // Set some settings to have a correct view
        detailVideoInfoBinding.icVideoPosterBig.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        //Starting video from last checked pos
        playerService.preparePlayer();
    }

    private void playVideoLandscape() {
        // Set some settings to have a correct view
        detailVideoInfoBinding.svVideoInfo.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        //Starting video from last checked pos
        playerService.preparePlayer();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        NetworkInfo activeNetwork = null;
        if (getActivity() != null) {
            cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
