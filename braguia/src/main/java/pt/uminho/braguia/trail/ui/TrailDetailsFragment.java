package pt.uminho.braguia.trail.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.databinding.FragmentTrailDetailsBinding;
import pt.uminho.braguia.permissions.PermissionRequestCodes;
import pt.uminho.braguia.permissions.Permissions;
import pt.uminho.braguia.pins.domain.PinHelper;
import pt.uminho.braguia.shared.ui.DescriptionFragment;
import pt.uminho.braguia.shared.ui.GalleryFragment;
import pt.uminho.braguia.tracker.TrackerService;

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

        TrailDetailsFragmentArgs args = pt.uminho.braguia.trail.ui.TrailDetailsFragmentArgs.fromBundle(getArguments());
        FragmentTrailDetailsBinding binding = FragmentTrailDetailsBinding.inflate(LayoutInflater.from(getContext()));

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setUserInputEnabled(false);

        Adapter adapter = new Adapter(this)
                .addFragment(getString(R.string.trail_description), DescriptionFragment.newInstance(mViewModel))
                .addFragment(getString(R.string.pins), TrailPinsFragment.newInstance(mViewModel))
                .addFragment(getString(R.string.maps), EdgesMapsFragment.newInstance(mViewModel))
                .addFragment(getString(R.string.gallery), GalleryFragment.newInstance(mViewModel));


        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(adapter.getTitle(position)));
        tabLayoutMediator.attach();

        ImageView imageView = view.findViewById(R.id.trail_image);

        mViewModel.getTrail(args.getTrailId()).observe(getViewLifecycleOwner(), trail -> {
            if (trail == null) {
                return;
            }
            Picasso.get().load(trail.getImageUrl()).into(imageView);
            binding.trailName.setText(trail.getName());
            binding.trailDuration.setText(trail.formatDuration());
        });

        Button startStopButton = view.findViewById(R.id.start_stop_trail);
        startStopButton.setOnClickListener(v -> mViewModel.switchRouteStatus());

        mViewModel.getStatus().observe(getViewLifecycleOwner(), trailStatus -> {
            switch (trailStatus) {
                case INITIAL:
                    break;
                case STARTED:
                    Context context = requireContext();
                    if (Permissions.hasLocationPermissions(context)) {
                        startTrackService();
                    } else {
                        Permissions.requestLocationPermissions(getActivity());
                    }
                    startStopButton.setText(R.string.end_trail);
                    break;
                case STOPED:
                    TrackerService.stop(requireContext());
                    startStopButton.setText(R.string.start_trail);
                    break;
            }
        });

        mViewModel.getPins().observe(getViewLifecycleOwner(), pins -> {
           // HACK para atualizar a lista de pins para iniciar o GMaps quando o user iniciar roteiro
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionRequestCodes.LOCATION.getValue()) {
            if (Arrays.stream(grantResults).anyMatch(gr -> gr != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getContext(), getString(R.string.location_permission_recomended), Toast.LENGTH_SHORT).show();
            } else {
                startTrackService();
            }
        }
    }

    private void startTrackService() {
        PinHelper.startGMaps(requireActivity(), mViewModel.getColdPins());
        TrackerService.start(requireContext());
    }

    public class Item {
        String title;
        Fragment fragment;

        public Item(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }
    }

    public class Adapter extends FragmentStateAdapter {
        private final List<Item> fragments = new ArrayList<>();

        public Adapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        Adapter addFragment(String title, Fragment fragment) {
            fragments.add(new Item(title, fragment));
            return this;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position).fragment;
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        public String getTitle(int position) {
            return fragments.get(position).title;
        }
    }
}