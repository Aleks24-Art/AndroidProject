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

    private static final String TAG = DescriptionVideoFragment.class.getCanonicalName();
    private FragmentDescriptionVideoBinding descriptionVideoBinding;

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
        if (getArguments() != null) {
            DescriptionVideoFragmentArgs args =
                    DescriptionVideoFragmentArgs.fromBundle(getArguments());
            descriptionVideoBinding.description.setText(
                    args.getFullPlot() == null
                            ? "Plot is not found" : args.getFullPlot());
        }
    }
}
