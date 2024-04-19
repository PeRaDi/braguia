package pt.uminho.braguia.api;

import java.util.List;

import pt.uminho.braguia.models.AppInfo;
import pt.uminho.braguia.models.Trail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("/app")
    Call<List<AppInfo>> getAppInfo();

    @GET("/trails")
    Call<List<Trail>> getTrails();

    @POST("/login")
    Call<String> login(String username, String password);
}
