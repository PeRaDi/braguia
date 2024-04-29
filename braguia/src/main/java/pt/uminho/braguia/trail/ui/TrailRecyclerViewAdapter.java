package pt.uminho.braguia.trail.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.uminho.braguia.databinding.FragmentTrailBinding;
import pt.uminho.braguia.trail.domain.Trail;

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
        return new ViewHolder(FragmentTrailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId().toString());
        holder.mContentView.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Trail mItem;

        public ViewHolder(FragmentTrailBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}