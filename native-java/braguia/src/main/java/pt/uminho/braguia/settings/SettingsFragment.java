package pt.uminho.braguia.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.contact.ContactSelectionActivity;
import pt.uminho.braguia.contact.EmergencyCallActivity;
import pt.uminho.braguia.permissions.PermissionRequestCodes;
import pt.uminho.braguia.permissions.Permissions;
import pt.uminho.braguia.pins.domain.PinHelper;
import pt.uminho.braguia.tracker.TrackerService;
import pt.uminho.braguia.user.UserActivity;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel viewModel;
    private Preference authPreference;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference selectEmergencyNumbersPreference = findPreference("select_emergency_numbers");
        selectEmergencyNumbersPreference.setOnPreferenceClickListener(preference1 -> {
            Intent intent = new Intent(getContext(), ContactSelectionActivity.class);
            startActivity(intent);
            return true;
        });

        Preference makeEmergencyCallPreference = findPreference("make_emergency_call");
        makeEmergencyCallPreference.setOnPreferenceClickListener(preference1 -> {
            Intent intent = new Intent(getContext(), EmergencyCallActivity.class);
            startActivity(intent);
            return true;
        });

        Preference viewProfilePreference = findPreference("profile");
        viewProfilePreference.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getContext(), UserActivity.class);
            startActivity(intent);
            return true;
        });

        SwitchPreferenceCompat locationServicePref = findPreference("location_service");

        locationServicePref.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (boolean) newValue;
            if (enabled) {
                if (Permissions.hasLocationPermissions(getContext())) {
                    startTrackService();
                } else {
                    Permissions.requestLocationPermissions(getActivity());
                }
            } else {
                TrackerService.stop(getContext());
            }
            return true;
        });


        authPreference = findPreference("auth");
        authPreference.setOnPreferenceClickListener(preference -> {
            if (viewModel.isAuthenticated()) {
                viewModel.logout();
            } else {
                NavDirections directions = SettingsFragmentDirections.actionSettingsFragmentToLoginFragment();
                NavHostFragment.findNavController(SettingsFragment.this).navigate(directions);
            }
            return true;
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case LOGGED_IN:
                    authPreference.setTitle(R.string.logout);
                    authPreference.setSummary(R.string.logout_description);
                    break;
                case LOGGED_OUT:
                    authPreference.setTitle(R.string.login);
                    authPreference.setSummary(R.string.login_description);
                    break;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionRequestCodes.LOCATION.getValue()) {
            if (Arrays.stream(grantResults).anyMatch(gr -> gr != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getContext(), getString(R.string.location_permission_recomended), Toast.LENGTH_SHORT).show();
            } else {
                startTrackService();
            }
        }
    }

    private void startTrackService() {
        TrackerService.start(requireContext());
    }
}
