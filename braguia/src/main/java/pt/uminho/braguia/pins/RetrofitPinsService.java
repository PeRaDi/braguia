package pt.uminho.braguia.pins;

import static pt.uminho.braguia.preference.SharedPreferencesModule.COOKIE_KEY;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import pt.uminho.braguia.auth.LoginPayload;
import pt.uminho.braguia.auth.RetrofitAuthenticationAPI;
import pt.uminho.braguia.user.User;
import pt.uminho.braguia.util.Result;
import pt.uminho.braguia.util.ResultConsumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RetrofitPinsService implements PinsService{
    private final Retrofit retrofit;
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();


    @Inject
    public RetrofitPinsService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        this.retrofit = retrofit;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void pins(ResultConsumer<JsonArray> result) {
        RetrofitPinsAPI api = retrofit.create(RetrofitPinsAPI.class);
        Call<JsonArray> pins = api.getPins(sharedPreferences.getString(COOKIE_KEY, ""));
        pins.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    result.accept(Result.ok(response.body()));
                } else {
                    result.accept(onError(response.errorBody().toString(), null));
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                result.accept(onError(t.getMessage(), t));
            }
        });
    }

    @Override
    public void pin(int pinNumber, ResultConsumer<JsonObject> result) {
        RetrofitPinsAPI api = retrofit.create(RetrofitPinsAPI.class);
        Call<JsonObject> pinCall = api.getPin(sharedPreferences.getString(COOKIE_KEY, ""), pinNumber);
        pinCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    result.accept(Result.ok(response.body()));
                } else {
                    result.accept(onError(response.errorBody().toString(), null));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                result.accept(onError(t.getMessage(), t));
            }
        });
    }

    private Result onError(String error, Throwable t) {
        return Result.error(error, t);
    }
}
