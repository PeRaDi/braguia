package pt.uminho.braguia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import pt.uminho.braguia.contact.Contact;
import pt.uminho.braguia.contact.ContactDao;
import pt.uminho.braguia.pins.data.PinLocalDatasource;
import pt.uminho.braguia.pins.data.db.PinEntity;
import pt.uminho.braguia.trail.data.TrailLocalDatasource;
import pt.uminho.braguia.trail.data.db.TrailEntity;
import pt.uminho.braguia.user.User;
import pt.uminho.braguia.user.UserDAO;

@Database(entities = {
        User.class,
        Contact.class,
        TrailEntity.class,
        PinEntity.class
}, version = 5)
@TypeConverters({Converters.class})
public abstract class BraGuiaDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();

    public abstract ContactDao contactDao();

    public abstract TrailLocalDatasource trailLocalDatasource();

    public abstract PinLocalDatasource pinLocalDatasource();
}