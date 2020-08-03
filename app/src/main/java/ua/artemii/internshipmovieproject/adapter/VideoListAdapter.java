package ua.artemii.internshipmovieproject.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.ItemVideoBinding;
import ua.artemii.internshipmovieproject.fragments.VideoListFragment;
import ua.artemii.internshipmovieproject.fragments.VideoListFragmentDirections;
import ua.artemii.internshipmovieproject.model.VideoListInfoModel;
import ua.artemii.internshipmovieproject.services.SimpleExoPlayerService;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private static final String TAG = VideoListFragment.class.getCanonicalName();
    private List<VideoListInfoModel> videoListInfoModelList = Collections.emptyList();

    public void setVideoListInfoModelList(List<VideoListInfoModel> videoListInfoModelList) {
        this.videoListInfoModelList = videoListInfoModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemVideoBinding itemBinding =
                ItemVideoBinding.inflate(inflater, parent, false);

        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Log.d(TAG, "URL = " + videoListInfoModelList.get(position).getPoster());
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(videoListInfoModelList.get(position).getPoster())
                .error(R.drawable.img_poster_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.itemBinding.icVideoPoster);

        holder.itemBinding.title.setText(videoListInfoModelList.get(position).getTitle());
        holder.itemBinding.actorsList.setText(videoListInfoModelList.get(position).getActors());
        /*holder.itemBinding.year.setText(videoListInfoModelList.get(position).getYear().endsWith(StringValues.STILL_GOING)
                ? videoListInfoModelList.get(position).getYear() + StringValues.NOW
                : videoListInfoModelList.get(position).getYear());*/
    }

    @Override
    public int getItemCount() {
        return videoListInfoModelList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemVideoBinding itemBinding;

        VideoViewHolder(@NonNull ItemVideoBinding itemBinding) {
            super(itemBinding.getRoot());
            itemView.setOnClickListener(this);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onClick(View v) {
            //Navigate from videoListFragment to detailVideoInfoFragment
            SimpleExoPlayerService.getInstance().zeroingPlayerPosition();
            SimpleExoPlayerService.getInstance().setStarted(false);

            VideoListFragmentDirections.ActionVideoListFragmentToDetailVideoInfoFragment action =
                            VideoListFragmentDirections.actionVideoListFragmentToDetailVideoInfoFragment(
                                            videoListInfoModelList.get(getAdapterPosition()).getImdbID());
            Navigation.findNavController(v).navigate(action);
        }
    }

}
