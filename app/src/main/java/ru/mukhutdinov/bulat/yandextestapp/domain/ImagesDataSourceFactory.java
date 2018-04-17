package ru.mukhutdinov.bulat.yandextestapp.domain;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;
import ru.mukhutdinov.bulat.yandextestapp.data.Photo;

public class ImagesDataSourceFactory extends DataSource.Factory<Integer, Photo> {

    private final CompositeDisposable compositeDisposable;

    @Getter
    private MutableLiveData<ImagesDataSource> liveDataSource = new MutableLiveData<>();

    public ImagesDataSourceFactory(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public DataSource<Integer, Photo> create() {
        ImagesDataSource dataSource = new ImagesDataSource(compositeDisposable);
        liveDataSource.postValue(dataSource);
        return dataSource;
    }
}