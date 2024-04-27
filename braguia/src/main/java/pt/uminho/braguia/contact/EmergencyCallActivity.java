package pt.uminho.braguia.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;


@AndroidEntryPoint
public class EmergencyCallActivity extends AppCompatActivity {

    @Inject
    ContactRepository contactRepository;

    List<Contact> selectedContactList;

    private ImageView img_contact1, img_contact2, img_contact3, img_contact4;
    private TextView lbl_contact1, lbl_contact2, lbl_contact3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);

        img_contact1 = findViewById(R.id.img_contact1);
        img_contact2 = findViewById(R.id.img_contact2);
        img_contact3 = findViewById(R.id.img_contact3);
        img_contact4 = findViewById(R.id.img_contact4);

        lbl_contact1 = findViewById(R.id.lbl_contact1);
        lbl_contact2 = findViewById(R.id.lbl_contact2);
        lbl_contact3 = findViewById(R.id.lbl_contact3);

        loadContacts();
    }

    private void loadContacts() {
        contactRepository.getAllContacts().observe(this, contacts -> {
            selectedContactList = contacts != null ? contacts : new ArrayList<>();
            Log.i("ContactSelectionActivity", "Loaded " + selectedContactList.size() + " contacts.");

            if (selectedContactList.size() == 0)
                return;

            setContactNames();

            img_contact1.setOnClickListener(v -> contactClickListner(1));
            img_contact2.setOnClickListener(v -> contactClickListner(2));
            img_contact3.setOnClickListener(v -> contactClickListner(3));
            img_contact4.setOnClickListener(v -> callEmergency(null));
        });
    }

    private void setContactNames() {
        for(int i = 1; i <= 3; i++) {
            if (selectedContactList.size() >= i) {
                switch (i) {
                    case 1:
                        lbl_contact1.setText(selectedContactList.get(0).contactName == null ? "Não selecionado" : selectedContactList.get(0).contactName);
                        break;
                    case 2:
                        lbl_contact2.setText(selectedContactList.get(1).contactName == null ? "Não selecionado" : selectedContactList.get(1).contactName);
                        break;
                    case 3:
                        lbl_contact3.setText(selectedContactList.get(2).contactName == null ? "Não selecionado" : selectedContactList.get(2).contactName);
                        break;
                }
            }
        }
    }

    private void contactClickListner(int buttonNumber) {
        if (selectedContactList.size() >= buttonNumber) {
            callEmergency(selectedContactList.get(buttonNumber - 1));
        }
    }

    private void callEmergency(Contact contact) {
        String phoneNumber;
        if (contact == null) {
            phoneNumber = "112";
        } else {
            phoneNumber = contact.contactNumber;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}