package pt.uminho.braguia.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pt.uminho.braguia.contact.Contact;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts")
    LiveData<List<Contact>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Contact> contact);

    @Query("DELETE FROM contacts")
    void deleteAll();
}
