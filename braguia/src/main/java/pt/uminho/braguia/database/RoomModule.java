package pt.uminho.braguia.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomModule {

    @Provides
    public BraGuiaDatabase provideBraGuiaDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, BraGuiaDatabase.class, "braguia.db").setJournalMode(RoomDatabase.JournalMode.TRUNCATE).fallbackToDestructiveMigration().build();
    }
}
