package pt.uminho.braguia.shared.ui;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.squareup.picasso.Picasso;

import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.domain.PinMedia;


public class MediaFragment extends Fragment {

    Player player;

    public MediaFragment() {
        // Required empty public constructor
    }

    public static MediaFragment newInstance() {
        MediaFragment fragment = new MediaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = view.findViewById(R.id.image_view);
        PlayerView playerView = view.findViewById(R.id.media_view);

        pt.uminho.braguia.shared.ui.MediaFragmentArgs args = pt.uminho.braguia.shared.ui.MediaFragmentArgs.fromBundle(getArguments());
        PinMedia media = args.getMedia();

        if (media.isImage()) {
            playerView.setVisibility(View.GONE);
            Picasso.get().load(media.getFileUrl()).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
            player = new ExoPlayer.Builder(getContext()).build();
            player.setMediaItem(new MediaItem.Builder().setUri(media.getFileUrl()).build());
            player.prepare();
            playerView.setPlayer(player);
        }

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveMediaToDevice(media.getFileUrl()));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void saveMediaToDevice(String mediaUrl) {
        Uri uri = Uri.parse(mediaUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Downloading Media");
        request.setDescription("Downloading media file...");

        String fileName = uri.getLastPathSegment();

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM, fileName);

        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                }
            }
        };

        getContext().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}