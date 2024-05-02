package pt.uminho.braguia.trail.ui.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.trail.data.TrailRepository;
import pt.uminho.braguia.trail.domain.Edge;

@AndroidEntryPoint
public class TrailDetailsActivity extends AppCompatActivity {

    // @Inject
    // PinRepository pinRepository;

    @Inject
    TrailRepository trailRepository;

    RecyclerView pinsRecyclerView;

    private TextView trailName;
    private TextView trailDescription;
    private TextView trailDuration;
    private ImageView trailBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_details);

        trailName = findViewById(R.id.lbl_trail_title);
        trailDescription = findViewById(R.id.lbl_trail_description);
        trailDuration = findViewById(R.id.lbl_trail_duration);
        trailBanner = findViewById(R.id.img_trail_banner);

        pinsRecyclerView = findViewById(R.id.pins_recycler_view);

        int trailId = getIntent().getIntExtra("trailId", -1);
        trailRepository.getTrailById(trailId).observe(this, trail -> {
            if (trail != null) {
                trailName.setText(trail.getName());
                trailDescription.setText(trail.getDescription());
                Picasso.get().load(trail.getImageUrl()).into(trailBanner);

                int hours = (int) (trail.getDuration() / 60);
                int minutes = (int) (trail.getDuration() % 60);
                trailDuration.setText("Duration: " + hours + "h " + minutes + "m");

                // List<Edge> edgeList = trail.getEdges();
                // pinsRecyclerView.setAdapter(new PinRecyclerViewAdapter(retrievePins(edgeList)));
            }
        });
    }

    public List<Pin> retrievePins(List<Edge> edgeList) {
        List<Pin> pins = new ArrayList<>();

        for (Edge edge : edgeList) {
            pins.add(edge.getStartPin());
        }

        return pins;
    }
}