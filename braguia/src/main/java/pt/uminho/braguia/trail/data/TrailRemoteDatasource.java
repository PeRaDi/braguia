package pt.uminho.braguia.trail.data;

import java.util.List;

import pt.uminho.braguia.trail.domain.Trail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TrailRemoteDatasource {

    @GET("/trails")
    Call<List<Trail>> getTrails();

    // Only premium users
    @GET("/trail/{id}")
    Call<Trail> getTrail(@Path("id") String id);

}
