package pt.uminho.braguia.contact;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import pt.uminho.braguia.database.BraGuiaDatabase;
import pt.uminho.braguia.repositories.ContactRepository;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ContactModule {

    @Singleton
    @Provides
    public static ContactRepository provideContactRepository(BraGuiaDatabase braGuiaDatabase) {
        return new ContactRepository(braGuiaDatabase);
    }
}
