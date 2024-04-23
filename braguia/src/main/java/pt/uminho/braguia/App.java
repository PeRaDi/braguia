package pt.uminho.braguia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.auth.LoginActivity;

@AndroidEntryPoint
public class App extends AppCompatActivity {

    @Inject
    AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!authenticationService.isAuthenticated()) {
            Log.i("App", "Not Authenticated");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}