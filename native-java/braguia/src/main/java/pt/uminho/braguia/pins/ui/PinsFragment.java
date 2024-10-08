package pt.uminho.braguia.pins.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.pins.domain.RelPin;
import pt.uminho.braguia.preference.SharedPreferencesModule;
import pt.uminho.braguia.trail.ui.TrailRecyclerViewAdapter;
import pt.uminho.braguia.trail.ui.TrailsViewModel;

@AndroidEntryPoint
public class PinsFragment extends Fragment {

    private PinsViewModel pinsViewModel;

    @Inject
    AuthenticationService authenticationService;

    public PinsFragment() {
    }

    @SuppressWarnings("unused")
    public static PinsFragment newInstance() {
        return new PinsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pinsViewModel = new ViewModelProvider(this).get(PinsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pins, container, false);

        pinsViewModel.getPins().observe(getViewLifecycleOwner(), pins -> {
            pinsViewModel.getPinsMedia().observe(getViewLifecycleOwner(), pinsMedia -> {
                pinsViewModel.getRelPins().observe(getViewLifecycleOwner(), relPins -> {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    SharedPreferences sharedPreferences = SharedPreferencesModule.provideSharedPreferences(context.getApplicationContext());
                    Set<String> visitedPins = sharedPreferences.getStringSet("visited", new HashSet<>());

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new PinRecyclerViewAdapter(pins, pinsMedia, relPins, visitedPins, this, authenticationService));
                });
            });
        });

        return view;
    }
}
