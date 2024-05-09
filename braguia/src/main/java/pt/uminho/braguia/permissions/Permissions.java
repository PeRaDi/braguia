package pt.uminho.braguia.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {
    public static boolean hasPermission(Context context, String permission) {
        boolean a = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        return a;
    }

    public static boolean hasLocationPermissions(Context context) {
        if (!hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return false;
        }
        if (!hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            return false;
        }
        if (!hasPermission(context, Manifest.permission.FOREGROUND_SERVICE)) {
            return false;
        }
        return hasPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
    }

    public static void requestLocationPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE,
        }, PermissionRequestCodes.LOCATION.getValue());
    }

    public static boolean hasForegroundPermissions(Context context) {
        return hasPermission(context, Manifest.permission.FOREGROUND_SERVICE);
    }

    public static void requestForegroundPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.FOREGROUND_SERVICE,
        }, PermissionRequestCodes.FOREGROUND_SERVICE.getValue());
    }

}
