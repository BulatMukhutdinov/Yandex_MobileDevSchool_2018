package ru.mukhutdinov.bulat.yandextestapp.data.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mukhutdinov.bulat.yandextestapp.data.PhotoResponse;

public interface Api {

    @GET("/api/")
    Single<PhotoResponse> getImages(@Query("page") int page, @Query("per_page") int pageSize);
}
