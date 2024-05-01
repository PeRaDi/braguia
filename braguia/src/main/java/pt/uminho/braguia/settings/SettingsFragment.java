package pt.uminho.braguia.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.navigation.NavDirections;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.auth.LoginActivity;
import pt.uminho.braguia.contact.ContactSelectionActivity;
import pt.uminho.braguia.contact.EmergencyCallActivity;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {

    @Inject
    AuthenticationService authenticationService;

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

        PreferenceCategory contaCategory = findPreference("conta");
        Preference logoutPreference = findPreference("logout");

        logoutPreference.setOnPreferenceClickListener(preference1 -> {
            authenticationService.logout();
            Intent intent = new Intent(this.getContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        });

        if (!authenticationService.isAuthenticated()) {
            contaCategory.setVisible(false);
        }
    }
}
