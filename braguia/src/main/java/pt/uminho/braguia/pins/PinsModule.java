package pt.uminho.braguia.pins;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class PinsModule {

    @Singleton
    @Provides
    public PinsService providePinsService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        return new RetrofitPinsService(retrofit, sharedPreferences);
    }

}
