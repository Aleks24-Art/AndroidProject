package ua.artemii.internshipmovieproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public static final String TAG =
            DetailVideoFragment.class.getCanonicalName();

    private FragmentDetailVideoInfoBinding detailVideoInfoBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            updateDetailVideoInfo(args.getImdbID());
        }
    }

    private void updateDetailVideoInfo(String imdbID) {
        new ViewModelProvider(this)
                .get(DetailVideoInfoViewModel.class)
                .getDetailVideoInfo(imdbID, "full")
                .observe(getViewLifecycleOwner(), detailVideoInfo -> {
                    loadBigPoster(detailVideoInfo.getPosterUrl());
                    detailVideoInfoBinding.bigTitle.setText(detailVideoInfo.getTitle());
                    detailVideoInfoBinding.releasedInfo.setText((detailVideoInfo.getReleased()));
                    detailVideoInfoBinding.actorsInfo.setText(detailVideoInfo.getActors());
                    detailVideoInfoBinding.countryInfo.setText(detailVideoInfo.getCountry());
                    detailVideoInfoBinding.genreInfo.setText(detailVideoInfo.getGenre());
                    detailVideoInfoBinding.runtimeInfo.setText(detailVideoInfo.getRuntime());
                    detailVideoInfoBinding.typeInfo.setText(detailVideoInfo.getType());
                    detailVideoInfoBinding.btnToVideoDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DetailVideoFragmentDirections.ActionDetailVideoInfoFragmentToDescriptionVideoFragment action =
                                    DetailVideoFragmentDirections.actionDetailVideoInfoFragmentToDescriptionVideoFragment(detailVideoInfo.getPlot());
                            Navigation.findNavController(v).navigate(action);
                        }
                    });
                });
    }

    private void loadBigPoster(String posterUrl) {
        Log.d(TAG, "LoadBigPoster!!!");
        Glide.with(getContext())
                .asBitmap()
                .load(posterUrl)
                .error(R.drawable.img_poster_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(detailVideoInfoBinding.icVideoPosterBig);
    }
}
