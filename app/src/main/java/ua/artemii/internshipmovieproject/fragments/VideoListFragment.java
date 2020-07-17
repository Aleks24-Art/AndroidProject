package ua.artemii.internshipmovieproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.artemii.internshipmovieproject.MainActivity;
import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.adapter.VideoListAdapter;
import ua.artemii.internshipmovieproject.databinding.FragmentVideoListBinding;
import ua.artemii.internshipmovieproject.model.Search;
import ua.artemii.internshipmovieproject.services.NetworkService;

public class VideoListFragment extends Fragment {

    public static final String TAG = VideoListFragment.class.getCanonicalName();
    private FragmentVideoListBinding videoListBinding;
    private VideoListAdapter adapter;
    //private List<ShortDescVideo> shortDescVideoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting downloading data");
        getItemListWithRequest();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (videoListBinding != null) {
            return videoListBinding.getRoot();
        }
        videoListBinding
                = FragmentVideoListBinding.inflate(inflater, container, false);

        initVideoRecyclerView();
        return videoListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initVideoRecyclerView() {
        if (adapter == null) {
            RecyclerView recyclerView =
                    videoListBinding.rvVideo;

            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(((MainActivity) getActivity())
                            .getApplicationContext());

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);
            adapter =
                    new VideoListAdapter(new ArrayList<>());
        }
        Log.d(TAG, "Setting adapter = " + adapter);
        videoListBinding.rvVideo.setAdapter(adapter);
    }

    private void getItemListWithRequest() {
        NetworkService.getInstance()
                .getVideoItemService()
                .getVideoList()
                .enqueue(new Callback<Search>() {
                    @Override
                    public void onResponse(Call<Search> call, Response<Search> response) {
                        Log.d(TAG, "Setting list = " + response.body().getShortDescVideoList().toString());
                        adapter.setShortDescVideoList(response.body().getShortDescVideoList());
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Adapter = " + adapter);
                    }

                    @Override
                    public void onFailure(Call<Search> call, Throwable t) {

                    }
                });
    }


}
