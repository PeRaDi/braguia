package pt.uminho.braguia.auth;

import java.util.Map;

import pt.uminho.braguia.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAuthenticationAPI {

    @POST("/login")
    Call<Map<String, String>> login(@Body LoginPayload body);

    @GET("/user")
    Call<User> getUser();

}
