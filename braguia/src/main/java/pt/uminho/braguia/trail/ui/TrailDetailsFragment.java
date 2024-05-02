package pt.uminho.braguia.trail.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.trail.ui.TrailDetailsFragmentArgs;

@AndroidEntryPoint
public class TrailDetailsFragment extends Fragment {

    private TrailDetailsViewModel mViewModel;

    public static TrailDetailsFragment newInstance() {
        return new TrailDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trail_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrailDetailsViewModel.class);

        TrailDetailsFragmentArgs args = TrailDetailsFragmentArgs.fromBundle(getArguments());
        ImageView imageView = view.findViewById(R.id.trail_image);
        TextView titleView = view.findViewById(R.id.trail_name);
        TextView durationView = view.findViewById(R.id.trail_duration);
        TextView descriptionView = view.findViewById(R.id.trail_description);

        mViewModel.getTrail(args.getTrailId()).observe(getViewLifecycleOwner(), trail -> {
            if(trail == null) {
                return;
            }
            Picasso.get().load(trail.getImageUrl()).into(imageView);
            titleView.setText(trail.getName());
            descriptionView.setText(trail.getDescription());
            durationView.setText(trail.formatDuration());
        });
    }
}