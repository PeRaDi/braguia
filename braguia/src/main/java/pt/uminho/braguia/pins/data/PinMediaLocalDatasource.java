package pt.uminho.braguia.pins.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pt.uminho.braguia.pins.data.db.PinMediaEntity;

@Dao
public interface PinMediaLocalDatasource {

    @Insert
    void insert(PinMediaEntity... media);

    @Query("SELECT * FROM media")
    LiveData<List<PinMediaEntity>> getMedia();

    @Query("SELECT * FROM media WHERE media_pin = :pinId")
    LiveData<List<PinMediaEntity>> getMediaForPin(Long pinId);

    @Query("DELETE FROM media WHERE media_pin = :pinId")
    void deleteMediaForPin(Long pinId);

    @Query("DELETE FROM media")
    void deleteAll();
}
