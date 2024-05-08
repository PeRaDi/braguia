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
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.contact.Contact;
import pt.uminho.braguia.databinding.FragmentContactSelectionBinding;
import pt.uminho.braguia.databinding.FragmentPinsItemBinding;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.pins.domain.RelPin;
import pt.uminho.braguia.trail.ui.TrailsFragment;
import pt.uminho.braguia.trail.ui.TrailsFragmentDirections;

public class PinRecyclerViewAdapter extends RecyclerView.Adapter<PinRecyclerViewAdapter.ViewHolder> {

    private final List<Pin> mValues;
    private final List<PinMedia> pinsMedia;
    private final List<RelPin> relPins;
    private final List<Pin> visitedPins;
    private final PinsFragment pinsFragment;
    private final AuthenticationService authenticationService;

    public PinRecyclerViewAdapter(List<Pin> pins,
                                  List<PinMedia> pinsMedia,
                                  List<RelPin> relPins,
                                  List<Pin> visitedPins,
                                  PinsFragment pinsFragment,
                                  AuthenticationService authenticationService) {
        this.mValues = pins;
        this.pinsMedia = pinsMedia;
        this.relPins = relPins;
        this.visitedPins = visitedPins;
        this.pinsFragment = pinsFragment;
        this.authenticationService = authenticationService;

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
        Pin pin = mValues.get(position);

        String imageURI = "";
        for (PinMedia media : pin.getPinMedia()) {
            if (media.getType().equals("I")) {
                imageURI = media.getFileUrl();
                break;
            }
        }

        if(imageURI.isEmpty()) {
            holder.pinImage.setImageResource(android.R.drawable.ic_menu_gallery);
            holder.pinImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            Picasso.get().load(Uri.parse(imageURI)).into(holder.pinImage);
        }
        holder.pinName.setText(pin.getName());
        holder.pinDescription.setText(pin.getDescription());
        holder.pinLat.setText(String.valueOf(pin.getLatitude()));
        holder.pinLong.setText(String.valueOf(pin.getLongitude()));
        holder.pinAlt.setText(String.valueOf(pin.getAltitude()));

        holder.pinDescription.setVisibility(View.GONE);

        holder.expandButton.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(holder.parent, new AutoTransition());
            if (holder.expanded) {
                holder.pinDescription.setVisibility(View.GONE);
                holder.expandButton.setImageResource(android.R.drawable.arrow_down_float);
            } else {
                holder.pinDescription.setVisibility(View.VISIBLE);
                holder.expandButton.setImageResource(android.R.drawable.arrow_up_float);
            }
            holder.expanded = !holder.expanded;
        });

        holder.pinImage.setOnClickListener(view -> {
            if (authenticationService.isAuthenticated() && authenticationService.currentUser().isPremium()) {
                NavDirections directions = PinsFragmentDirections.actionPinsActivityToPinDetailsFragment(pin.getId());
                NavHostFragment.findNavController(pinsFragment).navigate(directions);
            }
        });

        // TODO: check if visited. create table for visited pinIds
        if(visitedPins != null)
            holder.visited.setChecked(visitedPins.stream().anyMatch(visitedPin -> visitedPin.getId().equals(pin.getId())));
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
        }
    }
}