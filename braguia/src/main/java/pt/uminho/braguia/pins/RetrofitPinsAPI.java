package pt.uminho.braguia.pins;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import pt.uminho.braguia.auth.LoginPayload;
import pt.uminho.braguia.user.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitPinsAPI {
    @GET("/pins")
    Call<JsonArray> getPins(@Header("Cookie") String cookie);

    @GET("/pin/{number}")
    Call<JsonObject> getPin(@Header("Cookie") String cookie, @Path("number") int pinNumber);

}
