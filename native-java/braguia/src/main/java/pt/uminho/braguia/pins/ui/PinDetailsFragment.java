package pt.uminho.braguia.pins.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.preference.SharedPreferencesModule;
import pt.uminho.braguia.shared.ui.DescriptionFragment;
import pt.uminho.braguia.shared.ui.GalleryFragment;

@AndroidEntryPoint
public class PinDetailsFragment extends Fragment {

    private ImageView img_pin;
    private TextView lbl_pinName;
    private TextView lbl_lat;
    private TextView lbl_long;
    private TextView lbl_alt;
    private CheckBox visited;

    private PinDetailsViewModel mViewModel;

    public static PinDetailsFragment PinDetailsFragment() {
        return new PinDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pin_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lbl_pinName = view.findViewById(R.id.pin_name);
        lbl_lat = view.findViewById(R.id.pin_lat);
        lbl_long = view.findViewById(R.id.pin_long);
        lbl_alt = view.findViewById(R.id.pin_alt);
        img_pin = view.findViewById(R.id.pin_image);
        visited = view.findViewById(R.id.checkBox_visited);

        // nao consigo instanciar o view model
        mViewModel = new ViewModelProvider(this).get(PinDetailsViewModel.class);

        PinDetailsFragmentArgs args = PinDetailsFragmentArgs.fromBundle(getArguments());

        Log.d("PinDetailsFragment", "Pin ID: " + args.getPinId());

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setUserInputEnabled(false);

        Adapter adapter = new Adapter(this)
                .addFragment(getString(R.string.trail_description), DescriptionFragment.newInstance(mViewModel))
                .addFragment(getString(R.string.gallery), GalleryFragment.newInstance(mViewModel));

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(adapter.getTitle(position)));
        tabLayoutMediator.attach();

        mViewModel.getPin(args.getPinId()).observe(getViewLifecycleOwner(), pin -> {
            if (pin != null) {
                lbl_pinName.setText(pin.getName());
                lbl_lat.setText(String.valueOf(pin.getLatitude()));
                lbl_long.setText(String.valueOf(pin.getLongitude()));
                lbl_alt.setText(String.valueOf(pin.getAltitude()));

                String imageURI = "";
                for (PinMedia media : pin.getPinMedia()) {
                    if (media.getType().equals("I")) {
                        imageURI = media.getFileUrl();
                        break;
                    }
                }

                if(imageURI.isEmpty()) {
                    img_pin.setImageResource(android.R.drawable.ic_menu_gallery);
                    img_pin.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } else {
                    Picasso.get().load(Uri.parse(imageURI)).into(img_pin);
                }

                Context context = view.getContext();
                SharedPreferences sharedPreferences = SharedPreferencesModule.provideSharedPreferences(context.getApplicationContext());
                Set<String> visitedPins = sharedPreferences.getStringSet("visited", new HashSet<>());

                visited.setOnClickListener(v -> {
                    if (visitedPins.contains(pin.getId().toString())) {
                        removeVisitedPin(context, pin.getId().toString());
                    } else {
                        addVisitedPin(context, pin.getId().toString());
                    }
                });

                visited.setChecked(visitedPins.contains(pin.getId().toString()));
            }


        });
    }
    public void addVisitedPin(Context context, String pinId) {
        SharedPreferences sharedPreferences = SharedPreferencesModule.provideSharedPreferences(context.getApplicationContext());
        Set<String> visitedPins = sharedPreferences.getStringSet("visited", new HashSet<>());
        visitedPins.add(pinId);
        sharedPreferences.edit().putStringSet("visited", visitedPins).apply();
    }

    public void removeVisitedPin(Context context, String pinId) {
        SharedPreferences sharedPreferences = SharedPreferencesModule.provideSharedPreferences(context.getApplicationContext());
        Set<String> visitedPins = sharedPreferences.getStringSet("visited", new HashSet<>());
        visitedPins.remove(pinId);
        sharedPreferences.edit().putStringSet("visited", visitedPins).apply();
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
        private final List<PinDetailsFragment.Item> fragments = new ArrayList<>();

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