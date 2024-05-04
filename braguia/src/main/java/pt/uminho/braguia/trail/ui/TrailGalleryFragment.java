package pt.uminho.braguia.trail.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Optional;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.domain.PinMedia;

@AndroidEntryPoint
public class TrailGalleryFragment extends Fragment {

    TrailDetailsViewModel detailsViewModel;

    public TrailGalleryFragment() {
        // Required empty public constructor
    }

    public static TrailGalleryFragment newInstance(TrailDetailsViewModel detailsViewModel) {
        TrailGalleryFragment fragment = new TrailGalleryFragment();
        fragment.detailsViewModel = detailsViewModel;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trail_gallery, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsViewModel = new ViewModelProvider(this).get(TrailDetailsViewModel.class);

        GridView gridView = view.findViewById(R.id.grid_view);

        detailsViewModel.getMedias().observe(getViewLifecycleOwner(), pinMedias -> {
            gridView.setAdapter(new GalleryAdapter(this.getContext(), pinMedias));
        });
    }

    public class GalleryAdapter extends BaseAdapter {

        private Context context;
        private List<PinMedia> pinMedias;

        public GalleryAdapter(Context context, List<PinMedia> pinMedias) {
            this.context = context;
            this.pinMedias = pinMedias;
        }

        private PinMedia getMedia(int index) {
            return pinMedias.get(index);
        }

        @Override
        public int getCount() {
            return pinMedias.size();
        }

        @Override
        public Object getItem(int index) {
            return getMedia(index);
        }

        @Override
        public long getItemId(int index) {
            return getMedia(index).getId();
        }

        @Override
        public View getView(int index, View view, ViewGroup viewGroup) {
            PinMedia media = getMedia(index);

            if (media.isImage()) {
                ImageView imageView;
                if (view == null) {
                    imageView = new ImageView(context);
                    imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setPadding(8, 8, 8, 8);
                } else {
                    imageView = (ImageView) view;
                }
                Picasso.get().load(media.getFileUrl()).into(imageView);
            } else if (media.isVideo()) {

            } else if (media.isRecord()) {

            }

            TextView textView = new TextView(context);
            textView.setText(R.string.media_not_supported);
            return textView;
        }
    }
}