package pt.uminho.braguia.pins.old;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;

@AndroidEntryPoint
public class PinsActivity extends AppCompatActivity {

    @Inject
    PinsService pinsService;

    ListView listView;
    ArrayList<PinData> pins;
    PinsList pinsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pins);
        populatePinsData();
        listView = findViewById(R.id.pinsListView);
        pinsList = new PinsList(getApplicationContext(), pins);
        listView.setAdapter(pinsList);
        pinsList.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinDetails(v);
            }
        });
    }

    private void populatePinsData() {
        pins = new ArrayList<>();

        pinsService.pins((result) -> {
            if (result.isError()) {
                Toast.makeText(this, result.getError(), Toast.LENGTH_LONG).show();
            } else {
                Log.i("pins service result - ", result.toString());
                JsonArray jsonArray = result.getValue();
                for (JsonElement element : jsonArray) {
                    Log.i("object - ", element.toString());
                    JsonObject jsonObject = element.getAsJsonObject();
                    PinData pin = new PinData();
                    pin.setItemData(jsonObject);
                    pins.add(pin);
                }
            }
            pinsList.updateList(pins);
        });
    }

    public void pinDetails(View view) {
        int position = listView.getPositionForView(view);
        PinData clickedPin = pins.get(position);
        Intent intent = new Intent(this, PinDetailsActivity.class);
        intent.putExtra("pinId", clickedPin.getId());
        startActivity(intent);
    }


    public void goBack(View view){
        onBackPressed();
    }
}