package pt.uminho.braguia.pins.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pt.uminho.braguia.pins.data.db.RelPinEntity;

@Dao
public interface RelPinLocalDatasource {

    @Insert
    void insert(RelPinEntity... relPins);

    @Query("SELECT * FROM rel_pin")
    LiveData<List<RelPinEntity>> getRelPins();

    @Query("SELECT * FROM rel_pin WHERE pin = :pinId")
    LiveData<List<RelPinEntity>> getRelPinsForPin(Long pinId);

    @Query("DELETE FROM rel_pin WHERE pin = :pinId")
    void deleteRelPinsForPin(Long pinId);

    @Query("DELETE FROM rel_pin")
    void deleteAll();
}
