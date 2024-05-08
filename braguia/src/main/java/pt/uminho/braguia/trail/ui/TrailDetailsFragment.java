package pt.uminho.braguia.trail.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.shared.ui.DescriptionFragment;
import pt.uminho.braguia.shared.ui.GalleryFragment;

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
        ImageView imageView = view.findViewById(R.id.trail_image);
        TextView titleView = view.findViewById(R.id.trail_name);
        TextView durationView = view.findViewById(R.id.trail_duration);
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

        mViewModel.getTrail(args.getTrailId()).observe(getViewLifecycleOwner(), trail -> {
            if (trail == null) {
                return;
            }
            Picasso.get().load(trail.getImageUrl()).into(imageView);
            titleView.setText(trail.getName());
            durationView.setText(trail.formatDuration());
        });
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