package ua.artemii.internshipmovieproject.fragments;

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

import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.FragmentDetailVideoInfoBinding;
import ua.artemii.internshipmovieproject.viewmodel.DetailVideoInfoViewModel;

public class DetailVideoFragment extends Fragment {
    private static final String TAG =
            DetailVideoFragment.class.getCanonicalName();

    private FragmentDetailVideoInfoBinding detailVideoInfoBinding;
    private DetailVideoInfoViewModel videosVM;

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

        return detailVideoInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            DetailVideoFragmentArgs args =
                    DetailVideoFragmentArgs.fromBundle(getArguments());
            videosVM.loadDetailVideoInfo(args.getImdbID(), "full");
        }
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
}
