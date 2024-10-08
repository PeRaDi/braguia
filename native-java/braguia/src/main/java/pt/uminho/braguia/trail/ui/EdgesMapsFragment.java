package pt.uminho.braguia.trail.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinHelper;

@AndroidEntryPoint
public class EdgesMapsFragment extends Fragment {

    TrailDetailsViewModel detailsViewModel;

    public EdgesMapsFragment() {
    }

    public EdgesMapsFragment(TrailDetailsViewModel detailsViewModel) {
        this.detailsViewModel = detailsViewModel;
    }

    public static EdgesMapsFragment newInstance(TrailDetailsViewModel detailsViewModel) {
        return new EdgesMapsFragment(detailsViewModel);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         *
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            detailsViewModel.getPins().observe(getViewLifecycleOwner(), pins -> {
                if (pins.isEmpty()) {
                    return;
                }
                List<MarkerOptions> markerOptions = pins
                        .stream()
                        .map(pin -> {
                            LatLng latLng = new LatLng(pin.getLatitude(), pin.getLongitude());
                            return new MarkerOptions()
                                    .position(latLng)
                                    .title(pin.getName());
                        })
                        .collect(Collectors.toList());

                markerOptions.forEach(googleMap::addMarker);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerOptions.get(0).getPosition(), 10);
                googleMap.animateCamera(cameraUpdate);

                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setCompassEnabled(true);

                googleMap.setOnInfoWindowClickListener(marker -> PinHelper.startGMaps(requireActivity(), pins));
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edges_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (detailsViewModel == null) {
            detailsViewModel = new ViewModelProvider(this).get(TrailDetailsViewModel.class);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.trail_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}