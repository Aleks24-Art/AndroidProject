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

    FragmentDescriptionVideoBinding descriptionVideoBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        descriptionVideoBinding
                = FragmentDescriptionVideoBinding.inflate(inflater, container, false);
        return descriptionVideoBinding.getRoot();
    }
}
