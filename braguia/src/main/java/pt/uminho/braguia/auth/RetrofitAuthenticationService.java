package pt.uminho.braguia.auth;

import static pt.uminho.braguia.preference.SharedPreferencesModule.COOKIE_KEY;
import static pt.uminho.braguia.preference.SharedPreferencesModule.USER_KEY;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import pt.uminho.braguia.user.User;
import pt.uminho.braguia.util.Result;
import pt.uminho.braguia.util.ResultConsumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitAuthenticationService implements AuthenticationService {
    private final Retrofit retrofit;
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();


    @Inject
    public RetrofitAuthenticationService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        this.retrofit = retrofit;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser() != null;
    }

    @Override
    public User currentUser() {
        String userJson = sharedPreferences.getString(USER_KEY, null);
        if (userJson == null) {
            return null;
        }
        return gson.fromJson(userJson, User.class);
    }

    @Override
    public void login(String username, String password, ResultConsumer<User> result) {
        RetrofiAuthenticationAPI api = retrofit.create(RetrofiAuthenticationAPI.class);
        Call<Map<String, String>> login = api.login(new LoginPayload(username, password));
        login.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    String cookie = response.headers()
                            .values("Set-Cookie")
                            .stream()
                            .collect(Collectors.joining("; "));
                    sharedPreferences.edit().putString(COOKIE_KEY, cookie).commit();
                    getUserInfo(api, result);
                } else {
                    result.accept(onError(response.errorBody().toString(), null));
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                result.accept(onError(t.getMessage(), t));
            }
        });
    }

    @Override
    public void logout() {
        clearCookie();
    }

    private void getUserInfo(RetrofiAuthenticationAPI api, ResultConsumer<User> result) {
        api.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user.getType() != null) {
                        sharedPreferences.edit().putString(USER_KEY, gson.toJson(user)).commit();
                        result.accept(Result.ok(user));
                    } else {
                        result.accept(onError(response.body().toString(), null));
                    }
                } else {
                    result.accept(onError(response.errorBody().toString(), null));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                result.accept(onError(t.getMessage(), t));
            }
        });
    }

    private Result onError(String error, Throwable t) {
        clearCookie();
        return Result.error(error, t);
    }

    private void clearCookie() {
        sharedPreferences.edit().remove(COOKIE_KEY).commit();
        sharedPreferences.edit().remove(USER_KEY).commit();
    }

}
