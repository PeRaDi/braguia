package pt.uminho.braguia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import pt.uminho.braguia.api.APIClient;
import pt.uminho.braguia.models.AppInfo;
import pt.uminho.braguia.views.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends AppCompatActivity {
    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            APIClient.buildClient();

            testAPICall();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    void testAPICall() {
        Call<List<AppInfo>> call = APIClient.getClientInterface().getAppInfo();
        call.enqueue(new Callback<List<AppInfo>>() {
            @Override
            public void onResponse(Call<List<AppInfo>> call, Response<List<AppInfo>> response) {
                appInfo = response.body().get(0);
                Intent intent = new Intent(App.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<AppInfo>> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
            }
        });
    }
}