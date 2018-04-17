package ru.mukhutdinov.bulat.yandextestapp.presentation.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;
import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.domain.ImagesDataSource;
import ru.mukhutdinov.bulat.yandextestapp.domain.ImagesDataSourceFactory;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.NetworkState;

import static ru.mukhutdinov.bulat.yandextestapp.presentation.util.Constants.PAGE_SIZE;

public class PhotoViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable;

    @Getter
    private LiveData<PagedList<Photo>> entities;

    private ImagesDataSourceFactory dataSourceFactory;

    public PhotoViewModel() {
        compositeDisposable = new CompositeDisposable();

        dataSourceFactory = new ImagesDataSourceFactory(compositeDisposable);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPrefetchDistance(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build();

        entities = new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }


    public void retry() {
        //noinspection ConstantConditions
        dataSourceFactory.getLiveDataSource().getValue().retry();
    }

    public void refresh() {
        //noinspection ConstantConditions
        dataSourceFactory.getLiveDataSource().getValue().invalidate();
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(dataSourceFactory.getLiveDataSource(), ImagesDataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(dataSourceFactory.getLiveDataSource(), ImagesDataSource::getInitialLoad);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}