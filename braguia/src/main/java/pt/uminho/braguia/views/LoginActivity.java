package pt.uminho.braguia.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pt.uminho.braguia.App;
import pt.uminho.braguia.R;
import pt.uminho.braguia.services.AuthService;
import pt.uminho.braguia.services.ServiceContainer;

public class LoginActivity extends AppCompatActivity {

    EditText txt_username;
    EditText txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
    }

    public void login(View v) {
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();

        if(username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Campos necessários em falta.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthService authService = ServiceContainer.getInstance(this).getService(AuthService.class);


    }
}