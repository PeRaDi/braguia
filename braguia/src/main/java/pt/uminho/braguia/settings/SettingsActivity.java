package pt.uminho.braguia.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import pt.uminho.braguia.R;
import pt.uminho.braguia.contact.ContactSelectionActivity;
import pt.uminho.braguia.contact.EmergencyCallActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
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

            Preference logoutPreference = findPreference("logout");
            logoutPreference.setOnPreferenceClickListener(preference1 -> {
                // TODO : LOG OUT
                return true;
            });
        }
    }
}