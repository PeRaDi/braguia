package pt.uminho.braguia.pins.data;

import java.util.List;

import pt.uminho.braguia.pins.domain.Pin;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PinRemoteDatasource {

    @GET("/pins")
    Call<List<Pin>> getPins();
}
