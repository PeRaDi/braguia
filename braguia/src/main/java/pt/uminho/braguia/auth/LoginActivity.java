package pt.uminho.braguia.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.MainActivity;
import pt.uminho.braguia.R;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthenticationService authenticationService;

    Button btLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
    }

    public void login(View v) {
        EditText txtUsername = findViewById(R.id.txt_username);
        EditText txtPassword = findViewById(R.id.txt_password);
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Campos necessários em falta.", Toast.LENGTH_SHORT).show();
            return;
        }

        btLogin.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        authenticationService.login(username, password, (result) -> {
            progressBar.setVisibility(View.GONE);
            if (result.isError()) {
                Toast.makeText(this, result.getError(), Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }
}