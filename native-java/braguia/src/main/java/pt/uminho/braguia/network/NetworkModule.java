package pt.uminho.braguia.network;

import static pt.uminho.braguia.preference.SharedPreferencesModule.COOKIE_KEY;

import android.content.SharedPreferences;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import pt.uminho.braguia.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public final class NetworkModule {

    @Singleton
    @Provides
    public static OkHttpClient provideHttpClient(
            SharedPreferences sharedPreferences
    ) {
        // Create a trust manager that does not validate certificate chains
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
        final TrustManager[] trustAllCerts = new TrustManager[]{x509TrustManager};

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient
                .Builder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    if (originalRequest.url().uri().getPath().contains("/login")) {
                        return chain.proceed(originalRequest);
                    }
                    Request request = originalRequest
                            .newBuilder()
                            .header("Cookie", sharedPreferences.getString(COOKIE_KEY, ""))
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    @Singleton
    @Provides
    public static Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    public static CacheManager provideCacheManager() {
        return new CacheManager();
    }

}
