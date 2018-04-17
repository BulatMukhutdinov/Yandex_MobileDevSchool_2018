package ru.mukhutdinov.bulat.yandextestapp.data.network;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiImpl {
    private static final long REQUEST_TIMEOUT = 30;

    private static ApiImpl instance;

    private final String serverUrl;

    private ApiImpl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public static void init(String serverUrl) {
        instance = new ApiImpl(serverUrl);
    }

    public static Api create() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("key", "8687928-15a4bf5ee323934543425386b")
                    .build();

            Request request = original.newBuilder()
                    .url(url).build();
            return chain.proceed(request);
        })
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .followRedirects(false)
                .followSslRedirects(false)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(instance.serverUrl)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();
        return retrofit.create(Api.class);
    }
}