package pt.uminho.braguia.pins;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.App;
import pt.uminho.braguia.R;
import pt.uminho.braguia.auth.AuthenticationService;

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
    }

    private void populatePinsData() {
        pins = new ArrayList<>();
        /*
        PinData pin2 = new PinData();
        pin2.setName("Bom Jesus");
        pin2.setDescription("Bom Jesus de Braga");
        pin2.setImage("http://579f8a2e08b3ae26545741d09e8f230a.serveo.net/media/guimaraes-castelo.jpg");
        pins.add(pin2);

        pin2 = new PinData();
        pin2.setName("Bom Jesus 2");
        pin2.setDescription("Bom Jesus de Braga 2");
        pin2.setImage("http://579f8a2e08b3ae26545741d09e8f230a.serveo.net/media/universidade.jpg");
        pins.add(pin2);
        */

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
    public void goBack(View view){
        onBackPressed();
    }
}