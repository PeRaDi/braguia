package pt.uminho.braguia.trail.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;

@AndroidEntryPoint
public class TrailDescriptionFragment extends Fragment {
    TrailDetailsViewModel detailsViewModel;

    public TrailDescriptionFragment() {
        // Required empty public constructor
    }

    public static TrailDescriptionFragment newInstance(TrailDetailsViewModel detailsViewModel) {
        TrailDescriptionFragment fragment = new TrailDescriptionFragment();
        fragment.detailsViewModel = detailsViewModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trail_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (detailsViewModel == null) {
            detailsViewModel = new ViewModelProvider(this).get(TrailDetailsViewModel.class);
        }

        TextView descriptionView = view.findViewById(R.id.trail_description);

        detailsViewModel.getTrail().observe(getViewLifecycleOwner(), trail -> {
            if (trail == null) {
                return;
            }
            descriptionView.setText(trail.getDescription());
        });
    }
}