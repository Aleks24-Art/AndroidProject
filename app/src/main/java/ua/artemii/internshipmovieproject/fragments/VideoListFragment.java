package ua.artemii.internshipmovieproject.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.artemii.internshipmovieproject.adapter.VideoListAdapter;
import ua.artemii.internshipmovieproject.databinding.FragmentVideoListBinding;
import ua.artemii.internshipmovieproject.viewmodel.VideoListInfoViewModel;

public class VideoListFragment extends Fragment {

    private static final String TAG = VideoListFragment.class.getCanonicalName();
    private FragmentVideoListBinding videoListBinding;
    private VideoListAdapter adapter;
    private VideoListInfoViewModel videosVM;
    /**
     * Toast for back button
     */
    private Toast backToast;
    /**
     * Timer for count pause between clicking
     */
    private long backPressedTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videosVM = new ViewModelProvider(this).get(VideoListInfoViewModel.class);

        updateVideoList();
        updateDownloadState();

        videosVM.loadVideoList("House");

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

    private void initVideoRecyclerView() {
        if (adapter == null) {
            RecyclerView recyclerView =
                    videoListBinding.rvVideo;

            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(getContext());

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);

            adapter =
                    new VideoListAdapter(new ArrayList<>());
        }

        videoListBinding.rvVideo.setAdapter(adapter);
    }

    private void addCustomBackNavigation() {
        // This callback will only be called when VideoListFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    Log.d(TAG, "Зашли в backPressedTime + 2000 > System.currentTimeMillis()");
                    backToast.cancel();
                    this.remove();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                    return;
                } else {
                    Log.d(TAG, "Зашли в else");
                    backToast = Toast.makeText(getContext(), "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        };
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(this, callback);
    }

    private void updateVideoList() {
        videosVM.getVideos().observe(this, videoList -> {
            adapter.setVideoListInfoModelList(videoList);
            adapter.notifyDataSetChanged();
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

}
