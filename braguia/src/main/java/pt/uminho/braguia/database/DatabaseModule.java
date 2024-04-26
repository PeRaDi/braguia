package pt.uminho.braguia.database;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public static BraGuiaDatabase provideDatabase(@ApplicationContext Context context) {
        return Room
                .databaseBuilder(context, BraGuiaDatabase.class, "braguia")
                .fallbackToDestructiveMigration()
                .build();
    }

}
