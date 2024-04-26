package pt.uminho.braguia.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.permissions.PermissionRequestCodes;
import pt.uminho.braguia.permissions.Permissions;

@AndroidEntryPoint
public class ContactSelectionActivity extends AppCompatActivity {

    @Inject
    ContactRepository contactRepository;

    private RecyclerView recyclerView;
    private ContactSelectionRecyclerViewAdapter contactAdapter;
    private List<Contact> selectedContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_selection);
        checkContactsPermission();
    }

    private void checkContactsPermission() {
        if (!Permissions.hasPermission(this, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PermissionRequestCodes.READ_CONTACTS_PERMISSION_REQUEST_CODE.getValue());
        } else {
            loadContacts();
        }
    }

    private void loadContacts() {
        contactRepository.getAllContacts().observe(this, contacts -> {
            selectedContactList = contacts != null ? contacts : new ArrayList<>();
            Log.i("ContactSelectionActivity", "Loaded " + selectedContactList.size() + " contacts.");

            recyclerView = findViewById(R.id.recyclerView);
            contactAdapter = new ContactSelectionRecyclerViewAdapter(retrieveContacts(), selectedContactList);
            recyclerView.setAdapter(contactAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionRequestCodes.READ_CONTACTS_PERMISSION_REQUEST_CODE.getValue()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                // TODO - Handle permission denied. DO NOT proceed with retrieving contacts.
                // finish activity so?
                Toast.makeText(this, "É necessária a permissão para continuar.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("Range")
    private List<Contact> retrieveContacts() {
        List<Contact> contacts = new ArrayList<>();

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contacts.add(new Contact(id, name, number));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return contacts;
    }

    public void saveContacts(View view) {
        contactRepository.deleteAllContacts();
        contactRepository.insertContacts(selectedContactList);
        finish();
    }
}