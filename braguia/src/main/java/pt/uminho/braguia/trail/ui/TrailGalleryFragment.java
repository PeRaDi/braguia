package pt.uminho.braguia.trail.ui;

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
import pt.uminho.braguia.shared.ui.GridSpacingItemDecoration;
import pt.uminho.braguia.shared.ui.PinMediaAdapter;

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
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.addItemDecoration(GridSpacingItemDecoration.ofDP(context, 2, 5, true));

            PinMediaAdapter adapter = new PinMediaAdapter((v, media) -> {
                NavDirections directions = TrailDetailsFragmentDirections.actionTrailDetailsFragmentToMediaFragment(media);
                NavHostFragment.findNavController(TrailGalleryFragment.this).navigate(directions);
            });
            recyclerView.setAdapter(adapter);

            detailsViewModel.getMedias().observe(getViewLifecycleOwner(), pinMedias -> adapter.setData(pinMedias));
        }
    }

}

