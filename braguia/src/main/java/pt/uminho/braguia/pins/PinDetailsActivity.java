package pt.uminho.braguia.pins;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;

@AndroidEntryPoint
public class PinDetailsActivity extends AppCompatActivity {
    @Inject
    PinsService pinsService;
    PinAdditionalData pin = null;
    private int pinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pin);
        Intent intent = getIntent();
        this.pinId = intent.getIntExtra("pinId",0);
        getPinData(this.pinId);
    }

    private void getPinData(int pinNumber) {
        pinsService.pin(pinNumber,(result) -> {
            if (result.isError()) {
                Toast.makeText(this, result.getError(), Toast.LENGTH_LONG).show();
            } else {
                Log.i("pins service result - ", result.toString());
                JsonObject jsonObject = result.getValue();
                PinAdditionalData pin = new PinAdditionalData();
                pin.setItemData(jsonObject);
                this.pin = pin;
                refreshData();
            }
        });
    }

    private void refreshData() {
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(pin.getName());
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(pin.getDescription());
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        TextView lat = (TextView) findViewById(R.id.lat);
        lat.setText(String.valueOf(pin.getPinLat()));
        TextView lng = (TextView) findViewById(R.id.lng);
        lng.setText(String.valueOf(pin.getPinLng()));
        TextView alt = (TextView) findViewById(R.id.alt);
        alt.setText(String.valueOf(pin.getPinAlt()));
        if (pin.getVideo() != ("")){
            videoView.setVideoURI(Uri.parse(pin.getVideo()));
            videoView.start();
        }else {
            videoView.setVisibility(View.GONE);
        }
        ImageView image = (ImageView) findViewById(R.id.imageView);

        if (pin.getImage().equals("")){
            image.setImageResource(R.drawable.noimage);
        }else {
            Picasso.get()
                    .load(pin.getImage())
                    .into(image);
        }
    }

}
