package pt.uminho.braguia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pt.uminho.braguia.contact.Contact;
import pt.uminho.braguia.database.daos.ContactDao;

@Database(entities = {Contact.class}, version = 4)
public abstract class BraGuiaDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
}