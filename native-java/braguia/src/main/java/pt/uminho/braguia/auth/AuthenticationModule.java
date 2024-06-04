package pt.uminho.braguia.auth;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class AuthenticationModule {

    @Singleton
    @Provides
    public AuthenticationService provideAuthService(Retrofit retrofit, SharedPreferences sharedPreferences) {
        return new RetrofitAuthenticationService(retrofit, sharedPreferences);
    }

}
