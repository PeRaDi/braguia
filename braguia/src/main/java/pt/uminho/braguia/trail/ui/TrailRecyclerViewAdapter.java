package pt.uminho.braguia.trail.ui;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pt.uminho.braguia.R;
import pt.uminho.braguia.databinding.FragmentTrailBinding;
import pt.uminho.braguia.trail.domain.Trail;
import pt.uminho.braguia.trail.ui.details.TrailDetailsActivity;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Trail}.
 */
public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.ViewHolder> {

    private final List<Trail> mValues;

    public TrailRecyclerViewAdapter(List<Trail> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentTrailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Trail trail = mValues.get(position);
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
            Context context = view.getContext();
            Intent intent = new Intent(context, TrailDetailsActivity.class);
            int trailId = trail.getId().intValue();
            intent.putExtra("trailId", trailId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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