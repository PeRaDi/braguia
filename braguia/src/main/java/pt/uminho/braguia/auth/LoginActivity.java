package pt.uminho.braguia.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.auth.AuthenticationService;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View v) {
        EditText txtUsername = findViewById(R.id.txt_username);
        EditText txtPassword = findViewById(R.id.txt_password);
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if(username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Campos necessários em falta.", Toast.LENGTH_SHORT).show();
            return;
        }

        authenticationService.login(username, password);
    }
}