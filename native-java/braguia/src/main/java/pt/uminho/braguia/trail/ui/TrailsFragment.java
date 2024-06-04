package pt.uminho.braguia.trail.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.auth.AuthenticationService;

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
public class TrailsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private TrailsViewModel trailsViewModel;

    @Inject
    AuthenticationService authenticationService;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrailsFragment() {
    }

    @SuppressWarnings("unused")
    public static TrailsFragment newInstance(int columnCount) {
        TrailsFragment fragment = new TrailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        } else {
            mColumnCount = getResources().getInteger(R.integer.grid_column_count);
        }

        trailsViewModel = new ViewModelProvider(this).get(TrailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trail_list, container, false);

        trailsViewModel.getTrails().observe(getViewLifecycleOwner(), trails -> {
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                recyclerView.setAdapter(new TrailRecyclerViewAdapter(trails, this, authenticationService));
            }
        });

        return view;
    }
}