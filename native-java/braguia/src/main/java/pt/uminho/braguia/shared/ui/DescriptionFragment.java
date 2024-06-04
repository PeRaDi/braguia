package pt.uminho.braguia.shared.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.ui.PinDetailsViewModel;
import pt.uminho.braguia.pins.ui.PinsViewModel;
import pt.uminho.braguia.trail.ui.TrailDetailsViewModel;

@AndroidEntryPoint
public class DescriptionFragment extends Fragment {
    TrailDetailsViewModel trailDetailsViewModel;
    PinDetailsViewModel pinViewModel;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(TrailDetailsViewModel detailsViewModel) {
        DescriptionFragment fragment = new DescriptionFragment();
        fragment.trailDetailsViewModel = detailsViewModel;
        return fragment;
    }

    public static DescriptionFragment newInstance(PinDetailsViewModel pinViewModel) {
        DescriptionFragment fragment = new DescriptionFragment();
        fragment.pinViewModel = pinViewModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trail_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(trailDetailsViewModel != null) {
            setupTrailDescription(view);
        } else {
            setupPinDescription(view);
        }
    }

    private void setupTrailDescription(View view) {
        TextView descriptionView = view.findViewById(R.id.trail_description);

        trailDetailsViewModel.getTrail().observe(getViewLifecycleOwner(), trail -> {
            if (trail == null) {
                return;
            }
            descriptionView.setText(trail.getDescription());
        });
    }

    private void setupPinDescription(View view) {
        TextView descriptionView = view.findViewById(R.id.trail_description);

        pinViewModel.getPin().observe(getViewLifecycleOwner(), pin -> {
            if (pin == null) {
                Log.i("DescriptionFragment", "Pin object is null");
                return;
            }
            Log.i("DescriptionFragment", "Pin object is not null");
            if (pin.getDescription() == null || pin.getDescription().isEmpty()) {
                Log.i("DescriptionFragment", "Pin description is null or empty");
            } else {
                Log.i("DescriptionFragment", "Pin description: " + pin.getDescription());
            }
            descriptionView.setText(pin.getDescription());
        });
    }
}