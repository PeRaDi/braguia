package pt.uminho.braguia.trail.ui;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.databinding.FragmentTrailBinding;
import pt.uminho.braguia.trail.domain.Trail;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Trail}.
 */
public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.ViewHolder> {

    private final List<Trail> items;
    private final TrailsFragment trailsFragment;
    private final AuthenticationService authenticationService;

    public TrailRecyclerViewAdapter(List<Trail> items, TrailsFragment trailsFragment, AuthenticationService authenticationService) {
        this.items = items;
        this.trailsFragment = trailsFragment;
        this.authenticationService = authenticationService;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentTrailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Trail trail = items.get(position);
        holder.mItem = trail;
        holder.idView.setText(trail.getId().toString());
        holder.nameView.setText(trail.getName());
        holder.descriptionView.setText(trail.getDescription());
        Picasso.get().load(trail.getImageUrl()).into(holder.imageView);

        holder.binding.trailDuration.setText(trail.formatDuration());
        holder.binding.trailDifficulty.setText(trail.getDifficulty());
        holder.detailsLayout.setVisibility(View.GONE);

        holder.expandButton.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(holder.parent, new AutoTransition());
            if (holder.expanded) {
                holder.detailsLayout.setVisibility(View.GONE);
                holder.expandButton.setImageResource(android.R.drawable.arrow_down_float);
            } else {
                holder.detailsLayout.setVisibility(View.VISIBLE);
                holder.expandButton.setImageResource(android.R.drawable.arrow_up_float);
            }
            holder.expanded = !holder.expanded;
        });

        holder.imageView.setOnClickListener(view -> {
            if (authenticationService.isAuthenticated() && authenticationService.currentUser().isPremium()) {
                NavDirections directions = TrailsFragmentDirections.actionTrailsFragmentToTrailDetailsFragment(trail.getId());
                NavHostFragment.findNavController(trailsFragment).navigate(directions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Trail mItem;
        public final TextView idView;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView descriptionView;
        public final LinearLayout detailsLayout;
        public final ImageButton expandButton;
        public boolean expanded;
        private ViewGroup parent;
        private FragmentTrailBinding binding;

        public ViewHolder(FragmentTrailBinding binding, ViewGroup parent) {
            super(binding.getRoot());
            this.binding = binding;
            this.parent = parent;
            idView = binding.trailId;
            imageView = binding.trailImage;
            nameView = binding.trailName;
            descriptionView = binding.trailDescription;
            detailsLayout = binding.trailItemDetails;
            expandButton = binding.trailExpandButton;
            expanded = false;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + idView.getText() + "'";
        }
    }
}