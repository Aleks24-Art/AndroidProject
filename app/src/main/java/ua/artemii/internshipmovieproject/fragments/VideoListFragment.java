package ua.artemii.internshipmovieproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ua.artemii.internshipmovieproject.adapter.VideoListAdapter;
import ua.artemii.internshipmovieproject.databinding.FragmentVideoListBinding;
import ua.artemii.internshipmovieproject.services.VideoPlayer;
import ua.artemii.internshipmovieproject.values.StringValues;
import ua.artemii.internshipmovieproject.viewmodel.VideoListInfoViewModel;

/**
 * Start app fragment with video list, going after splash screen in main activity
 */
public class VideoListFragment extends Fragment {

    private static final String TAG = VideoListFragment.class.getCanonicalName();
    private FragmentVideoListBinding videoListBinding;
    private VideoListAdapter adapter;
    private VideoListInfoViewModel videosVM;
    // Toast for back button
    private Toast backToast;
    // Timer for count pause between clicking
    private long backPressedTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addCustomBackNavigation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (videoListBinding != null) {
            return videoListBinding.getRoot();
        }
        videoListBinding =
                FragmentVideoListBinding.inflate(inflater, container, false);
        initVideoRecyclerView();
        return videoListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videosVM = new ViewModelProvider(this)
                .get(VideoListInfoViewModel.class);

        // Subscribe on live data
        updateVideoList();
        updateDownloadState();

        // Init button. LiveData trigger there
        initSearchBtn();
    }

    @Override
    public void onStop() {
        super.onStop();
        VideoPlayer.getInstance().zeroingPlayerPosition();
    }

    private void initVideoRecyclerView() {
        if (adapter == null) {
            RecyclerView recyclerView =
                    videoListBinding.rvVideo;

            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(getContext());

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new VideoListAdapter();
        }
        videoListBinding.rvVideo.setAdapter(adapter);
    }

    /* Custom back pressed navigation
    If you are on VideoListFragment and press back btn
    it will show toast "Press again to exit"
    to exit you need to press it in next 2 seconds */
    private void addCustomBackNavigation() {
        // This callback will only be called when VideoListFragment is at least Started
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    this.remove();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                    return;
                } else {
                    backToast = Toast.makeText(getContext(), StringValues.PRESS_AGAIN, Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        };
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(this, callback);
    }

    private void initSearchBtn() {
        videoListBinding.btnSearch.setOnClickListener(v -> {
            String keyWord = videoListBinding.etKeyWord.getText().toString().toLowerCase();
            if (!keyWord.equals("") && getActivity() != null) {
                // LiveData trigger
                videosVM.loadVideoList(keyWord);
                hideKeyboard(v);
            }
        });
    }

    private void updateVideoList() {
        videosVM.getVideos().observe(getViewLifecycleOwner(),
                videoList -> adapter.setVideoListInfoModelList(videoList));
    }

    private void updateDownloadState() {
        videosVM.getThrowable().observe(getViewLifecycleOwner(),
                throwable -> {
                    if (getContext() != null && videosVM.isThrowableReadyToShown()) {
                        Toast.makeText(getContext(), StringValues.DOWNLOAD_ERROR, Toast.LENGTH_LONG).show();
                        Log.e(TAG, StringValues.DOWNLOAD_ERROR + ": ", throwable);
                        videosVM.setThrowableReadyToShown(false);
                    }
                });
    }

    // Hide Keyboard after searching
    private void hideKeyboard(View v) {
        if (getActivity() != null) {
            InputMethodManager imm = ((InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }
}
