package pt.uminho.braguia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import pt.uminho.braguia.user.User;
import pt.uminho.braguia.user.UserDAO;

@Database(
        entities = {User.class},
        version = 1
)
@TypeConverters({Converters.class})
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract UserDAO userDAO();

}
