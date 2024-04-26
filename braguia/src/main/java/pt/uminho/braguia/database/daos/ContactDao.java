package pt.uminho.braguia.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pt.uminho.braguia.contact.Contact;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Insert
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);
}
