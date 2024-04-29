package pt.uminho.braguia.trail.data;

import java.util.List;

import pt.uminho.braguia.trail.domain.Trail;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TrailRemoteDatasource {

    @GET("/trails")
    Call<List<Trail>> getTrails();

}
