package ua.artemii.internshipmovieproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.artemii.internshipmovieproject.MainActivity;
import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.adapter.VideoListAdapter;
import ua.artemii.internshipmovieproject.databinding.FragmentVideoListBinding;

public class VideoListFragment extends Fragment {

    FragmentVideoListBinding videoListBinding;
    RecyclerView rvVideo;
    VideoListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        videoListBinding
                = FragmentVideoListBinding.inflate(inflater, container, false);

        return videoListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVideoRecyclerView();
    }


    private void initVideoRecyclerView() {
        RecyclerView recyclerView = videoListBinding.rvVideo;

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(((MainActivity)getActivity())
                        .getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter =
                new VideoListAdapter(10);

        recyclerView.setAdapter(adapter);
    }




}
