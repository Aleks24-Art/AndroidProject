package ua.artemii.internshipmovieproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.artemii.internshipmovieproject.MainActivity;
import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.*;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private FragmentVideoListBinding videoListBinding;
    private ItemVideoBinding itemBinding;
    private int itemNum;

    public VideoListAdapter(int itemNum) {
        this.itemNum = itemNum;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        itemBinding =
                ItemVideoBinding.inflate(inflater, parent, false);
        videoListBinding =
                FragmentVideoListBinding.inflate(inflater, parent, false);


        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.itemNum;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public VideoViewHolder(@NonNull ItemVideoBinding itemBinding) {
            super(itemBinding.getRoot());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Navigate from videoListFragment to detailVideoInfoFragment
            Navigation.findNavController(v).navigate(R.id.action_videoListFragment_to_detailVideoInfoFragment);
        }
    }

}
