package io.github.jokoframework.repository;

import io.github.jokoframework.mboehaolib.BuildConfig;
import io.github.jokoframework.rx.RxErrorHandlingCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import io.github.jokoframework.mboehaolib.constants.Constants;


public class RepoBuilder {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging).build();
        }
    }

    private RepoBuilder() {
        // Utility Class
    }

    public static <T> T getInstance(Class<T> clazz) {
        return getInstance(httpClient.build(), clazz);
    }

    public static <T> T getInstance(OkHttpClient client, Class<T> clazz) {
        RxErrorHandlingCallAdapterFactory rxAdapterWithErrorHandling = (RxErrorHandlingCallAdapterFactory) RxErrorHandlingCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapterWithErrorHandling)
                .client(client)
                .build();
        return retrofit.create(clazz);
    }

    public static <T> T getSyncApi(Class<T> clazz) {
        return getSyncApi(httpClient.build(), clazz);
    }

    public static <T> T getSyncApi(OkHttpClient client, Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(clazz);
    }
}
