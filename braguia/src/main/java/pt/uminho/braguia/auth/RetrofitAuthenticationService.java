package pt.uminho.braguia.auth;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import pt.uminho.braguia.user.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitAuthenticationService implements AuthenticationService {
    private final Retrofit retrofit;
    private final SharedPreferences sharedPreferences;

    @Inject
    public RetrofitAuthenticationService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        this.retrofit = retrofit;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean isAuthenticated() {
        return getCookie() != null;
    }

    @Override
    public void login(String username, String password) {
        RetrofiAuthenticationAPI api = retrofit.create(RetrofiAuthenticationAPI.class);
        Call<Map<String, String>> login = api.login(new LoginPayload(username, password));
        login.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    String cookie = response.headers().values("Set-Cookie")
                            .stream()
                            .collect(Collectors.joining());
                    sharedPreferences.edit().putString("Cookie", cookie);
                    getUserInfo(api);
                } else {
                    Log.e("auth", "onResponse not successful: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("auth", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getUserInfo(RetrofiAuthenticationAPI api) {
        api.getUser(getCookie()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("user-info", response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("user-info-error", t.getMessage());
            }
        });
    }

    @Nullable
    private String getCookie() {
        return sharedPreferences.getString("Cookie", null);
    }
}
