package pt.uminho.braguia.trail.ui;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.braguia.databinding.FragmentPinsItemBinding;
import pt.uminho.braguia.databinding.FragmentTrailPinsBinding;
import pt.uminho.braguia.pins.domain.Pin;

public class TrailPinRecyclerViewAdapter extends RecyclerView.Adapter<TrailPinRecyclerViewAdapter.ViewHolder> {

    private List<Pin> pins;

    public TrailPinRecyclerViewAdapter() {
        setData(null);
    }

    public TrailPinRecyclerViewAdapter setData(List<Pin> pins) {
        this.pins = pins == null ? new ArrayList<>() : pins;
        return this;
    }

    public Pin get(int index) {
        return pins.get(index);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, FragmentPinsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(get(position));
    }

    @Override
    public int getItemCount() {
        return pins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentPinsItemBinding binding;
        private final ViewGroup parent;

        public ViewHolder(ViewGroup parent, FragmentPinsItemBinding binding) {
            super(binding.getRoot());
            this.parent = parent;
            this.binding = binding;
        }

        void bind(Pin pin) {
            binding.imageContainer.setVisibility(View.GONE);
            binding.checkBoxVisited.setVisibility(View.GONE);
            binding.expandButton.setVisibility(View.GONE);
            binding.pinDescription.setVisibility(View.GONE);

            binding.pinName.setText(pin.getName());
            binding.pinDescription.setText(pin.getDescription());
            binding.pinLat.setText(pin.getLatitude().toString());
            binding.pinLong.setText(pin.getLongitude().toString());
            binding.pinAlt.setText(pin.getAltitude().toString());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + binding.pinId.getText() + "'";
        }
    }
}