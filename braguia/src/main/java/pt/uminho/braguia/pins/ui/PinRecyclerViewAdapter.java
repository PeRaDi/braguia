package pt.uminho.braguia.pins.ui;

import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import pt.uminho.braguia.contact.Contact;
import pt.uminho.braguia.databinding.FragmentContactSelectionBinding;
import pt.uminho.braguia.databinding.FragmentPinsItemBinding;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.pins.domain.RelPin;

public class PinRecyclerViewAdapter extends RecyclerView.Adapter<PinRecyclerViewAdapter.ViewHolder> {

    private final List<Pin> mValues;
    private final List<PinMedia> pinsMedia;
    private final List<RelPin> relPins;
    private final List<Pin> visitedPins;

    public PinRecyclerViewAdapter(List<Pin> pins,
                                  List<PinMedia> pinsMedia,
                                  List<RelPin> relPins,
                                  List<Pin> visitedPins) {
        this.mValues = pins;
        this.pinsMedia = pinsMedia;
        this.relPins = relPins;
        this.visitedPins = visitedPins;

        // associate media with pins and rel pins too
        for (Pin pin : mValues) {
            for (PinMedia media : pinsMedia) {
                if (media.getPinId().equals(pin.getId())) {
                    pin.addMedia(media);
                }
            }
            for (RelPin relPin : relPins) {
                if (relPin.getPinId().equals(pin.getId())) {
                    pin.addRelPin(relPin);
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentPinsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), visitedPins);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView pinName;
        public final ImageView pinImage;
        public final TextView pinDescription;
        public final TextView pinLat;
        public final TextView pinLong;
        public final TextView pinAlt;
        public final CheckBox visited;

        public final ImageButton expandButton;

        public boolean expanded = false;

        public Pin mItem;

        private ViewGroup parent;

        public ViewHolder(FragmentPinsItemBinding binding, ViewGroup parent) {
            super(binding.getRoot());
            pinImage = binding.pinImage;
            pinName = binding.pinName;
            pinDescription = binding.pinDescription;
            pinLat = binding.pinLat;
            pinLong = binding.pinLong;
            pinAlt = binding.pinAlt;
            visited = binding.checkBoxVisited;
            expandButton = binding.expandButton;
            this.parent = parent;
        }

        public void bind(Pin pin, List<Pin> visitedPins) {
            mItem = pin;
            if(pin.getPinMedia().isEmpty()) {
                pinImage.setImageResource(android.R.drawable.ic_menu_gallery);
                pinImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                String uriString = pin.getPinMedia().get(0).getFileUrl();
                Picasso.get().load(uriString).into(pinImage);
            }
            pinName.setText(pin.getName());
            pinDescription.setText(pin.getDescription());
            pinLat.setText(String.valueOf(pin.getLatitude()));
            pinLong.setText(String.valueOf(pin.getLongitude()));
            pinAlt.setText(String.valueOf(pin.getAltitude()));

            pinDescription.setVisibility(View.GONE);

            expandButton.setOnClickListener(view -> {
                TransitionManager.beginDelayedTransition(parent, new AutoTransition());
                if (expanded) {
                    pinDescription.setVisibility(View.GONE);
                    expandButton.setImageResource(android.R.drawable.arrow_down_float);
                } else {
                    pinDescription.setVisibility(View.VISIBLE);
                    expandButton.setImageResource(android.R.drawable.arrow_up_float);
                }
                expanded = !expanded;
            });

            // TODO: check if visited. create table for visited pinIds
            // visited.setChecked(visitedPins.stream().anyMatch(visitedPin -> visitedPin.getId().equals(pin.getId())));
        }
    }
}