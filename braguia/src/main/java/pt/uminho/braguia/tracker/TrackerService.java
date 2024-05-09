package pt.uminho.braguia.tracker;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import pt.uminho.braguia.R;
import pt.uminho.braguia.permissions.Permissions;
import pt.uminho.braguia.pins.data.PinRepository;

public class TrackerService extends Service {
    private static final long LOCATION_INTERVAL = TimeUnit.SECONDS.toMillis(5); // Atualizar a cada X segundos
    private static final long FASTEST_INTERVAL = TimeUnit.SECONDS.toMillis(1); // Atualização mais rápida (opcional)
    private static final String CHANNEL_ID = "location";

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private NotificationManager notificationManager;

    @Inject
    PinRepository pinRepository;

    public TrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                showNotification(location);
            }
        };

        createNotificationChannel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationUpdates();
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (Permissions.hasLocationPermissions(this)) {
            LocationRequest locationRequest = createLocationRequest();
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            startForeground(1, createNotification(null));
        } else {
            Log.d("TrackerService", "No permissions");
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest createLocationRequest() {
        return new LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL)
                .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelName = "Location Updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription("Notification channel for location updates");
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(Location location) {
        String text = "Localização";
        if (location != null) {
            text = String.format("Latitude: %.6f, Longitude: %.6f", location.getLatitude(), location.getLongitude());
        }
        return new NotificationCompat.Builder(this, "location")
                .setContentTitle("BraGuia - Roteiro")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .build();
    }

    private void showNotification(Location location) {
        if(notificationManager != null && shouldNotify(location)) {
            notificationManager.cancel(1); // Cancel any previous notification
            notificationManager.notify(1, createNotification(location));
        }
    }

    private boolean shouldNotify(Location location) {
        if(location == null) {
            return false;
        }
        // TODO: Verificar se está num ponto de interesse e notificar o utilizador
        return true;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), TrackerService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), TrackerService.class);
        context.stopService(intent);
    }

}