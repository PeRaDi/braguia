package pt.uminho.braguia.contact;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import pt.uminho.braguia.database.BraGuiaDatabase;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ContactModule {

    @Singleton
    @Provides
    public static ContactRepository provideContactRepository(BraGuiaDatabase braGuiaDatabase) {
        return new ContactRepository(braGuiaDatabase);
    }
}
