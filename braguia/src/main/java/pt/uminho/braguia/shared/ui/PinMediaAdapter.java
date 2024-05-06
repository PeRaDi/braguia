package pt.uminho.braguia.shared.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.domain.PinMedia;


public class PinMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface MediaClickListener {
        void onClick(View view, PinMedia pinMedia);
    }

    private List<PinMedia> pinMedias;
    private final MediaClickListener mediaClickListener;

    public PinMediaAdapter(@NonNull MediaClickListener mediaClickListener) {
        this.mediaClickListener = mediaClickListener;
        setData(pinMedias);
    }

    public PinMediaAdapter setData(List<PinMedia> pinMedias) {
        this.pinMedias = pinMedias == null ? new ArrayList<>() : pinMedias;
        return this;
    }

    private PinMedia getMedia(int index) {
        return pinMedias.get(index);
    }

    private View inflate(int resource, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(resource, parent, false);
    }

    @Override
    public int getItemViewType(int position) {
        return getMedia(position).type().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (PinMedia.Type.of(viewType)) {
            case IMAGE:
                return new ImageHolder(inflate(R.layout.trail_gallery_image, parent));
            case VIDEO:
                return new VideoHolder(inflate(R.layout.trail_gallery_video, parent));
            case RECORD:
                return new RecordHolder(inflate(R.layout.trail_gallery_record, parent));
            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PinMedia media = getMedia(position);
        MediaHolder mediaHolder = (MediaHolder) holder;
        View bindedView = mediaHolder.bind(media);
        bindedView.setOnClickListener((view) -> mediaClickListener.onClick(view, media));
    }

    @Override
    public int getItemCount() {
        return pinMedias.size();
    }


    private static abstract class MediaHolder<T extends View> extends RecyclerView.ViewHolder {

        protected T view;

        public MediaHolder(@NonNull View itemView) {
            super(itemView);
            this.view = findView(itemView);
        }

        Context getContext() {
            return super.itemView.getContext();
        }

        abstract T findView(View itemView);

        View bind(PinMedia media) {
            // TODO: Implementar element transition??
            ViewCompat.setTransitionName(view, media.getId().toString());
            return onBind(media);
        }

        abstract View onBind(PinMedia media);

    }

    private static class ImageHolder extends MediaHolder<ImageView> {

        private ImageHolder(View view) {
            super(view);
        }

        @Override
        ImageView findView(View itemView) {
            return itemView.findViewById(R.id.trail_gallery_image);
        }

        View onBind(PinMedia media) {
            Picasso.get().load(media.getFileUrl()).into(view);
            return itemView;
        }

    }

    private static class VideoHolder extends MediaHolder<ImageView> {

        private VideoHolder(View view) {
            super(view);
        }

        @Override
        ImageView findView(View itemView) {
            return itemView.findViewById(R.id.trail_gallery_video);
        }

        View onBind(PinMedia media) {
            return itemView;
        }

    }

    private static class RecordHolder extends MediaHolder<ImageView> {

        private RecordHolder(View view) {
            super(view);
        }

        @Override
        ImageView findView(View itemView) {
            return itemView.findViewById(R.id.trail_gallery_record);
        }

        View onBind(PinMedia media) {
            return itemView;
        }
    }
}

