package pt.uminho.braguia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
            goToLogin();
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, authenticationService.currentUser().toString(), Toast.LENGTH_LONG);
        }
    }

    public void logout(View view) {
        authenticationService.logout();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}