package pt.uminho.braguia.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> users);

}
