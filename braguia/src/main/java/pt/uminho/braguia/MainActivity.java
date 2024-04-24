package pt.uminho.braguia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import javax.inject.Inject;

import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.auth.LoginActivity;

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

    public void logout(View view) {
        Log.i("MainActivity", "Logging out");
        authenticationService.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}