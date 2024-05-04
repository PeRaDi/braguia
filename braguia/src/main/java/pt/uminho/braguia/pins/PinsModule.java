package pt.uminho.braguia.pins;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import pt.uminho.braguia.database.BraGuiaDatabase;
import pt.uminho.braguia.network.CacheManager;
import pt.uminho.braguia.pins.data.PinMediaLocalDatasource;
import pt.uminho.braguia.pins.data.PinLocalDatasource;
import pt.uminho.braguia.pins.data.PinRemoteDatasource;
import pt.uminho.braguia.pins.data.PinRepository;
import pt.uminho.braguia.pins.data.RelPinLocalDatasource;
import pt.uminho.braguia.pins.old.PinsService;
import pt.uminho.braguia.pins.old.RetrofitPinsService;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class PinsModule {

    @Singleton
    @Provides
    public PinsService providePinsService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        return new RetrofitPinsService(retrofit, sharedPreferences);
    }

    @Singleton
    @Provides
    public static PinLocalDatasource provideLocalDatasource(BraGuiaDatabase database) {
        return database.pinLocalDatasource();
    }

    @Singleton
    @Provides
    public static PinRemoteDatasource provideRemoteDatasource(Retrofit retrofit) {
        return retrofit.create(PinRemoteDatasource.class);
    }

    @Singleton
    @Provides
    public static RelPinLocalDatasource provideRelPinLocalDatasource(BraGuiaDatabase database) {
        return database.relPinLocalDatasource();
    }

    @Singleton
    @Provides
    public static PinMediaLocalDatasource provideMediaLocalDatasource(BraGuiaDatabase database) {
        return database.mediaLocalDatasource();
    }

    @Singleton
    @Provides
    public static PinRepository repository(PinLocalDatasource localDatasource,
                                           PinRemoteDatasource remoteDatasource,
                                           RelPinLocalDatasource relPinLocalDatasource,
                                           PinMediaLocalDatasource mediaLocalDatasource,
                                           CacheManager cacheManager) {
        return new PinRepository(localDatasource,
                remoteDatasource,
                relPinLocalDatasource,
                mediaLocalDatasource,
                cacheManager.control(PinRepository.class));
    }

}
