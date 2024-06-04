package pt.uminho.braguia.trail.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.uminho.braguia.R;

/**
 * A fragment representing a list of Items.
 */
public class TrailPinsFragment extends Fragment {

    private TrailDetailsViewModel detailsViewModel;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrailPinsFragment() {
    }

    public static TrailPinsFragment newInstance(TrailDetailsViewModel detailsViewModel) {
        TrailPinsFragment fragment = new TrailPinsFragment();
        fragment.detailsViewModel = detailsViewModel;
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trail_pins_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            TrailPinRecyclerViewAdapter adapter = new TrailPinRecyclerViewAdapter();
            recyclerView.setAdapter(adapter);

            detailsViewModel.getPins().observe(getViewLifecycleOwner(), pins -> adapter.setData(pins));
        }
        return view;
    }
}