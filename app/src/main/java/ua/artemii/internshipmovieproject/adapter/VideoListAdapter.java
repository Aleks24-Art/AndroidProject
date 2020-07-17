package ua.artemii.internshipmovieproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.FragmentVideoListBinding;
import ua.artemii.internshipmovieproject.databinding.ItemVideoBinding;
import ua.artemii.internshipmovieproject.fragments.DetailVideoInfoFragmentDirections;
import ua.artemii.internshipmovieproject.fragments.VideoListFragment;
import ua.artemii.internshipmovieproject.fragments.VideoListFragmentDirections;
import ua.artemii.internshipmovieproject.model.ShortDescVideo;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private ItemVideoBinding itemBinding;
    private List<ShortDescVideo> shortDescVideoList;
    public static final String TAG = VideoListFragment.class.getCanonicalName();
    Context context;

    public VideoListAdapter(List<ShortDescVideo> shortDescVideoList) {
        this.shortDescVideoList = shortDescVideoList;
        notifyDataSetChanged();
        Log.d(TAG, "URL = " + this.shortDescVideoList);
    }

    public void setShortDescVideoList(List<ShortDescVideo> shortDescVideoList) {
        this.shortDescVideoList = shortDescVideoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        itemBinding =
                ItemVideoBinding.inflate(inflater, parent, false);


        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Log.d(TAG, "URL = " + shortDescVideoList.get(position).getPoster());
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(shortDescVideoList.get(position).getPoster())
                .error(R.drawable.ic_error_black_50dp)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.itemBinding.icVideoPoster);


        holder.itemBinding.title.setText(shortDescVideoList.get(position).getTitle());
        holder.itemBinding.year.setText(shortDescVideoList.get(position).getYear().endsWith("–") ?
                shortDescVideoList.get(position).getYear() + "nowadays" : shortDescVideoList.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return shortDescVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemVideoBinding itemBinding;

        public VideoViewHolder(@NonNull ItemVideoBinding itemBinding) {
            super(itemBinding.getRoot());
            itemView.setOnClickListener(this);
            this.itemBinding = itemBinding;
        }

        @Override
        public void onClick(View v) {
            //Navigate from videoListFragment to detailVideoInfoFragment
            VideoListFragmentDirections.ActionVideoListFragmentToDetailVideoInfoFragment action =
                    VideoListFragmentDirections.actionVideoListFragmentToDetailVideoInfoFragment(shortDescVideoList.get(getAdapterPosition()).getImdbID());
            Navigation.findNavController(v).navigate(action);
        }
    }

}
