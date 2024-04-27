package pt.uminho.braguia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.auth.LoginActivity;
import pt.uminho.braguia.contact.EmergencyCallActivity;
import pt.uminho.braguia.permissions.PermissionRequestCodes;
import pt.uminho.braguia.permissions.Permissions;
import pt.uminho.braguia.pins.PinsActivity;

public class MainActivity extends AppCompatActivity {

    @Inject
    AuthenticationService authenticationService;

    public static TextView lbl_internetConnection;
    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lbl_internetConnection = findViewById(R.id.lbl_internet_connection);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateInternetConnectionStatus(isNetworkConnected(context));
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        checkCallingPermission();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void updateInternetConnectionStatus(boolean isConnected) {
        if (isConnected) {
            lbl_internetConnection.setVisibility(TextView.INVISIBLE);
        } else {
            lbl_internetConnection.setVisibility(TextView.VISIBLE);
        }
    }

    public void logout() {
        Log.i("MainActivity", "Logging out");
        authenticationService.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openCallActivity(View view) {
        Intent intent = new Intent(this, EmergencyCallActivity.class);
        startActivity(intent);
    }

    private void checkCallingPermission() {
        if (!Permissions.hasPermission(this, Manifest.permission.CALL_PHONE))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PermissionRequestCodes.CALL_PHONE_PERMISSION_REQUEST_CODE.getValue());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionRequestCodes.CALL_PHONE_PERMISSION_REQUEST_CODE.getValue()) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "É necessária a permissão para continuar.", Toast.LENGTH_SHORT).show();
                // logout();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void pins(View view) {
        // Handle the click event for button_pins here
        Log.i("MainActivity", "Open pins screen");
        // You can add your logic here to open the pins screen
        Intent intent = new Intent(this, PinsActivity.class);
        startActivity(intent);
    }
}