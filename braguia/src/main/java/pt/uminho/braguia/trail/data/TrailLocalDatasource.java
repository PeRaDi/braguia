package pt.uminho.braguia.trail.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pt.uminho.braguia.trail.data.db.TrailEntity;

@Dao
public interface TrailLocalDatasource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrailEntity... trails);

    @Query("SELECT DISTINCT * from trail")
    LiveData<List<TrailEntity>> getTrails();

    @Query("SELECT * from trail WHERE id = :id")
    LiveData<TrailEntity> getTrailById(Long id);

    @Query("DELETE FROM trail")
    void deleteAll();
}
