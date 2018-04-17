package ru.mukhutdinov.bulat.yandextestapp.domain;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import retrofit2.HttpException;
import ru.mukhutdinov.bulat.yandextestapp.App;
import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.data.network.Api;
import ru.mukhutdinov.bulat.yandextestapp.data.network.ApiImpl;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.NetworkState;

import static ru.mukhutdinov.bulat.yandextestapp.presentation.util.Constants.PAGE_SIZE;

public class ImagesDataSource extends PageKeyedDataSource<Integer, Photo> {
    private final Api api;

    private final CompositeDisposable compositeDisposable;

    @Getter
    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    @Getter
    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private Completable retryCompletable;

    ImagesDataSource(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
        api = ApiImpl.create();
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Photo> callback) {
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        compositeDisposable.add(api.getImages(1, PAGE_SIZE)
                .subscribe(photoResponse -> {
                    setRetry(null);

                    networkState.postValue(NetworkState.LOADED);
                    initialLoad.postValue(NetworkState.LOADED);

                    callback.onResult(photoResponse.getPhotos(), null, 2);
                }, throwable -> {
                    setRetry(() -> loadInitial(params, callback));
                    NetworkState error = getProcessedError(throwable);
                    networkState.postValue(error);
                    initialLoad.postValue(error);
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {
        // ignored, since we only ever append to our initial load
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {
        networkState.postValue(NetworkState.LOADING);
        compositeDisposable.add(api.getImages(params.key, PAGE_SIZE)
                .subscribe(photoResponse -> {
                    setRetry(null);

                    networkState.postValue(NetworkState.LOADED);

                    callback.onResult(photoResponse.getPhotos(), params.key + 1);
                }, throwable -> {
                    setRetry(() -> loadAfter(params, callback));

                    networkState.postValue(getProcessedError(throwable));
                }));
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> Logger.e(throwable.getMessage())));
        }
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

    private NetworkState getProcessedError(Throwable throwable) throws IOException {
        NetworkState error;

        if (throwable instanceof SocketTimeoutException
                || throwable instanceof UnknownHostException) {
            error = NetworkState.error(App.instance.getString(R.string.network_error));
        } else if (throwable instanceof HttpException
                && ((HttpException) throwable).response().errorBody() != null) {
            //noinspection ConstantConditions
            error = NetworkState.error(((HttpException) throwable).response().errorBody().string());
        } else {
            error = NetworkState.error(throwable.getMessage());
        }
        return error;
    }
}