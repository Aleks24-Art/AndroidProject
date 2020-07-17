package ua.artemii.internshipmovieproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.commons.text.WordUtils;

import java.lang.reflect.Field;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.artemii.internshipmovieproject.R;
import ua.artemii.internshipmovieproject.databinding.FragmentDetailVideoInfoBinding;
import ua.artemii.internshipmovieproject.model.FullDescVideo;
import ua.artemii.internshipmovieproject.services.NetworkService;

public class DetailVideoInfoFragment extends Fragment {
    public static final String TAG =
            DetailVideoInfoFragment.class.getCanonicalName();
    FragmentDetailVideoInfoBinding detailInfoBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (detailInfoBinding != null) {
            return detailInfoBinding.getRoot();
        }
        detailInfoBinding =
                FragmentDetailVideoInfoBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            DetailVideoInfoFragmentArgs args = DetailVideoInfoFragmentArgs.fromBundle(getArguments());
            getItemListWithRequest(args.getImdbID());
            Log.i(TAG, "onViewCreated: " + args.getImdbID());
        }
        return detailInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailInfoBinding.btnToVideoDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_detailVideoInfoFragment_to_descriptionVideoFragment);
            }
        });
    }

    private void getItemListWithRequest(String imdbID) {
        NetworkService.getInstance()
                .getVideoItemService()
                .getFullDescVideo(imdbID)
                .enqueue(new Callback<FullDescVideo>() {
                    @Override
                    public void onResponse(Call<FullDescVideo> call, Response<FullDescVideo> response) {
                        //Log.d(TAG, "Setting list = " + response.body().getShortDescVideoList().toString());
                        //Getting full info about movie
                        FullDescVideo desc = response.body();
                        //Setting custom title in view
                        detailInfoBinding.bigTitle.setText(desc.getTitle());
                        //Load big poster with Glide
                        loadBigPoster(desc.getPoster());
                        try {
                            //Getting an array of pojo fields
                            Field[] fields = desc.getClass().getDeclaredFields();
                            for (Field f : fields) {
                                f.setAccessible(true);
                                //Getting field value and name
                                Object fieldValue = f.get(desc);
                                String fieldName = WordUtils.capitalize(f.getName());
                                // Don't pass null, poster url and id
                                if (fieldValue != null && !fieldValue.toString().equals("N/A") && !fieldName.equals("Poster") && !fieldName.equals("ImdbID")) {
                                    //Create text view for every field programmatically
                                    TextView text = new TextView(getContext());
                                    text.setTextColor(Color.BLACK);
                                    text.setTextSize(20);
                                    LinearLayout.LayoutParams params =
                                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(0, 5, 0, 5);
                                    text.setLayoutParams(params);
                                    text.setText(fieldName + ": " + f.get(desc).toString());
                                    detailInfoBinding.svInnerLayout.addView(text);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

/*
                        for(PropertyDescriptor propertyDescriptor :
                                Introspector.getBeanInfo(yourClass).getPropertyDescriptors()){

                            // propertyEditor.getReadMethod() exposes the getter
                            // btw, this may be null if you have a write-only property
                            System.out.println(propertyDescriptor.getReadMethod());
                        }*/
                       /*
                        detailInfoBinding.released.append((desc.getReleased()));
                        detailInfoBinding.actors.append(desc.getActors());
                        detailInfoBinding.boxOffice.append(desc.getBoxOffice());
                        detailInfoBinding.country.append(desc.getCountry());
                        detailInfoBinding.genre.append(desc.getGenre());
                        detailInfoBinding.director.append(desc.getDirector());
                        detailInfoBinding.plot.append(desc.getPlot());
                        detailInfoBinding.production.append(desc.getProduction());
                        detailInfoBinding.runtime.append(desc.getRuntime());
                        detailInfoBinding.type.append(desc.getType());
                        detailInfoBinding.writer.append(desc.getWriter());
*/

                        //adapter.notifyDataSetChanged();
                        //Log.d(TAG, "Adapter = " + adapter);
                    }

                    @Override
                    public void onFailure(Call<FullDescVideo> call, Throwable t) {

                    }
                });
    }

    private void loadBigPoster(String posterUrl) {
        Glide.with(getContext())
                .asBitmap()
                .load(posterUrl)
                .error(R.drawable.ic_error_black_50dp)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(detailInfoBinding.icVideoPosterBig);
    }
}
