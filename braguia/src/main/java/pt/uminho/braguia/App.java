package pt.uminho.braguia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import pt.uminho.braguia.api.APIClient;
import pt.uminho.braguia.services.AuthService;
import pt.uminho.braguia.services.EncryptedSharedPreferencesService;
import pt.uminho.braguia.services.ServiceContainer;
import pt.uminho.braguia.views.LoginActivity;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            APIClient.buildClient();
            buildServices();

            if (!ServiceContainer.getInstance(this).getService(AuthService.class).isAuthenticated()) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void buildServices() {
        ServiceContainer.getInstance(this).add(EncryptedSharedPreferencesService.class);
        ServiceContainer.getInstance(this).add(AuthService.class);
    }
}