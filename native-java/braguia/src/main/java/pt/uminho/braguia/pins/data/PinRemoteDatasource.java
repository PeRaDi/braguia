package pt.uminho.braguia.pins.data;

import java.util.List;

import pt.uminho.braguia.pins.domain.Pin;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PinRemoteDatasource {

    @GET("/pins")
    Call<List<Pin>> getPins();

    @GET("/pin/{id}")
    Call<Pin> getPin(@Path("id") String id);
}
