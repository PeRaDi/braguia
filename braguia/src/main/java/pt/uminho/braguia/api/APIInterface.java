package pt.uminho.braguia.api;

import java.util.List;

import pt.uminho.braguia.models.AppInfo;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/app")
    Call<List<AppInfo>> getAppInfo();
}
