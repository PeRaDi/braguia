package pt.uminho.braguia.pins;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import pt.uminho.braguia.auth.AuthenticationService;
import pt.uminho.braguia.auth.RetrofitAuthenticationService;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class PinsModule {

    @Singleton
    @Provides
    public PinsService provideAuthService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        return new RetrofitPinsService(retrofit, sharedPreferences);
    }

}
