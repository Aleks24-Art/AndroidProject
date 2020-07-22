package ua.artemii.internshipmovieproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ua.artemii.internshipmovieproject.databinding.FragmentDescriptionVideoBinding;

public class DescriptionVideoFragment extends Fragment {

    public static final String TAG = DescriptionVideoFragment.class.getCanonicalName();
    private FragmentDescriptionVideoBinding descriptionVideoBinding;
    private String fullPlot;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DescriptionVideoFragmentArgs args =
                    DescriptionVideoFragmentArgs.fromBundle(getArguments());
            fullPlot = args.getFullPlot();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        descriptionVideoBinding =
                FragmentDescriptionVideoBinding.inflate(inflater, container, false);
        return descriptionVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionVideoBinding.description.setText(fullPlot);
    }
}
