package pt.uminho.braguia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.api.APIInterface;
import pt.uminho.braguia.models.AppInfo;
import pt.uminho.braguia.services.AuthService;
import pt.uminho.braguia.services.EncryptedSharedPreferencesService;
import pt.uminho.braguia.services.ServiceContainer;
import pt.uminho.braguia.views.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@AndroidEntryPoint
public class App extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<List<AppInfo>> call = apiInterface.getAppInfo();
        call.enqueue(new Callback<List<AppInfo>>() {
            @Override
            public void onResponse(Call<List<AppInfo>> call, Response<List<AppInfo>> response) {
                if(response.isSuccessful()){
                    Log.i("API", response.body().toString());
                }else{
                    Log.e("main", "onResponse not successful: "+response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<AppInfo>> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
            }
        });


        try {

//            APIClient.buildClient();
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