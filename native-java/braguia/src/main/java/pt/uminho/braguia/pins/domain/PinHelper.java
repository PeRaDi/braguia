package pt.uminho.braguia.pins.domain;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PinHelper {

    public static void startGMaps(Activity activity, List<Pin> pins) {
        Log.d("PinHelper", Optional.ofNullable(pins).map(List::toString).orElse("NULL Pins"));
        toGoogleMapsURI(pins).ifPresent((uri) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PackageManager packageManager = activity.getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                activity.startActivity(intent);
            }
        });
    }

    public static Optional<Uri> toGoogleMapsURI(List<Pin> pins) {
        if (pins == null || pins.isEmpty() || pins.size() == 1) {
            return Optional.empty();
        }

        int pinsCount = pins.size();
        String origin = "&origin=" + pinToUrlParams(pins.get(0));
        String destination = "&destination=" + pinToUrlParams(pins.get(pinsCount - 1));
        String waypoints = pinsCount == 2 ? "" : "&waypoints=" + IntStream.range(1, pinsCount)
                .mapToObj(index -> pinToUrlParams(pins.get(index)))
                .collect(Collectors.joining("|"));

        String url = String.format("https://www.google.com/maps/dir/?api=1%s%s%s", origin, destination, waypoints);
        return Optional.ofNullable(Uri.parse(url));
    }

    private static String pinToUrlParams(Pin pin) {
        return pin.getLatitude() + "," + pin.getLongitude();
    }

}
