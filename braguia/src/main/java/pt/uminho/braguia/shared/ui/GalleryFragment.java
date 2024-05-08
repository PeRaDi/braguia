package pt.uminho.braguia.shared.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.ui.PinDetailsFragmentDirections;
import pt.uminho.braguia.pins.ui.PinDetailsViewModel;
import pt.uminho.braguia.pins.ui.PinsViewModel;
import pt.uminho.braguia.trail.ui.TrailDetailsFragmentDirections;
import pt.uminho.braguia.trail.ui.TrailDetailsViewModel;

@AndroidEntryPoint
public class GalleryFragment extends Fragment {

    TrailDetailsViewModel detailsViewModel;
    PinDetailsViewModel pinViewModel;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(TrailDetailsViewModel detailsViewModel) {
        GalleryFragment fragment = new GalleryFragment();
        fragment.detailsViewModel = detailsViewModel;
        return fragment;
    }

    public static GalleryFragment newInstance(PinDetailsViewModel pinViewModel) {
        GalleryFragment fragment = new GalleryFragment();
        fragment.pinViewModel = pinViewModel;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trail_gallery, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.addItemDecoration(GridSpacingItemDecoration.ofDP(context, 2, 5, true));

            PinMediaAdapter adapter;

            if (detailsViewModel != null) {
                adapter = new PinMediaAdapter((v, media) -> {
                    NavDirections directions = TrailDetailsFragmentDirections.actionTrailDetailsFragmentToMediaFragment(media);
                    NavHostFragment.findNavController(GalleryFragment.this).navigate(directions);
                });
                detailsViewModel.getMedias().observe(getViewLifecycleOwner(), trailMedias -> adapter.setData(trailMedias));
            } else {
                adapter = new PinMediaAdapter((v, media) -> {
                    NavDirections directions = PinDetailsFragmentDirections.actionPinDetailsFragmentToMediaFragment(media);
                    NavHostFragment.findNavController(GalleryFragment.this).navigate(directions);
                });
                pinViewModel.getMedias().observe(getViewLifecycleOwner(), pinMedias -> adapter.setData(pinMedias));
            }

            recyclerView.setAdapter(adapter);
        }
    }

}