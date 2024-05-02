package pt.uminho.braguia.pins.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pt.uminho.braguia.pins.data.db.PinEntity;

@Dao
public interface PinLocalDatasource {

    @Insert
    void insert(PinEntity... pins);

    @Query("SELECT * FROM pin")
    LiveData<List<PinEntity>> getPins();

    @Query("DELETE FROM pin")
    void deleteAll();
}
