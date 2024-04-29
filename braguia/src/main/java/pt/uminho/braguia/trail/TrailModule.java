package pt.uminho.braguia.trail;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import pt.uminho.braguia.database.BraGuiaDatabase;
import pt.uminho.braguia.network.CacheManager;
import pt.uminho.braguia.trail.data.TrailLocalDatasource;
import pt.uminho.braguia.trail.data.TrailRemoteDatasource;
import pt.uminho.braguia.trail.data.TrailRepository;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class TrailModule {

    @Singleton
    @Provides
    public static TrailLocalDatasource provideLocalDatasource(BraGuiaDatabase database) {
        return database.trailLocalDatasource();
    }

    @Singleton
    @Provides
    public static TrailRemoteDatasource provideRemoteDatasource(Retrofit retrofit) {
        return retrofit.create(TrailRemoteDatasource.class);
    }

    @Singleton
    @Provides
    public static TrailRepository repository(TrailLocalDatasource localDatasource,
                                             TrailRemoteDatasource remoteDatasource,
                                             CacheManager cacheManager) {
        return new TrailRepository(localDatasource, remoteDatasource, cacheManager.control(TrailRepository.class));
    }

}
