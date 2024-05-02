package pt.uminho.braguia.trail.ui.details;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pt.uminho.braguia.databinding.FragmentPinBinding;
import pt.uminho.braguia.pins.domain.Pin;

public class PinRecyclerViewAdapter extends RecyclerView.Adapter<PinRecyclerViewAdapter.ViewHolder> {

    private final List<Pin> mValues;

    public PinRecyclerViewAdapter(List<Pin> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentPinBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPinTitle.setText(mValues.get(position).getName());
        holder.mPinDescription.setText(mValues.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mPinTitle;
        public final TextView mPinDescription;
        public Pin mItem;

        public ViewHolder(FragmentPinBinding binding) {
            super(binding.getRoot());
            mPinTitle = binding.lblPinName;
            mPinDescription = binding.lblPinDescription;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPinTitle.getText() + "'";
        }
    }
}