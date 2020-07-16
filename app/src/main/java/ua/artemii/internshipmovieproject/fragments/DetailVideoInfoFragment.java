package ua.artemii.internshipmovieproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ua.artemii.internshipmovieproject.MainActivity;
import ua.artemii.internshipmovieproject.databinding.FragmentDetailVideoInfoBinding;

public class DetailVideoInfoFragment extends Fragment {

    FragmentDetailVideoInfoBinding detailInfoBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        detailInfoBinding =
                FragmentDetailVideoInfoBinding.inflate(inflater, container, false);

        return detailInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailInfoBinding.btnToThirdFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).goToDescriptionVideoFragment();
            }
        });
    }
}
