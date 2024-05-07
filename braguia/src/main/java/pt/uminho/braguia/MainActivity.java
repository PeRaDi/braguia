package pt.uminho.braguia;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.permissions.PermissionRequestCodes;
import pt.uminho.braguia.permissions.Permissions;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    AuthenticationService authenticationService;

    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean connected = isNetworkConnected(context);
                if (connected) {
                    Toast.makeText(context, getString(R.string.internet_connection), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        checkCallingPermission();

        if(!isGoogleMapsInstalled())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app uses Google Maps for navigation. Please install it.");
            builder.setPositiveButton("Install on market", getGoogleMapsListener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, bundle) -> {
            if (bundle == null || bundle.getBoolean("ShowBottomNavBar", true)) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });

        authenticationService.authInfo().observe(this, authInfo -> {
            MenuItem pinItem = bottomNavigationView.getMenu().findItem(R.id.pinsActivity);
            pinItem.setVisible(authInfo.authenticated && authInfo.user.isPremium());
        });

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

    private void checkCallingPermission() {
        if (!Permissions.hasPermission(this, Manifest.permission.CALL_PHONE))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PermissionRequestCodes.CALL_PHONE_PERMISSION_REQUEST_CODE.getValue());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionRequestCodes.CALL_PHONE_PERMISSION_REQUEST_CODE.getValue()) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, getString(R.string.call_permission_recomended), Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        } catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
            startActivity(intent);
//            finish();
        };
    }

}