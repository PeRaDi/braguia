package pt.uminho.braguia.trail.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;

@AndroidEntryPoint
public class TrailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trails);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TrailsFragment.newInstance(1))
                    .commitNow();
        }
    }
}