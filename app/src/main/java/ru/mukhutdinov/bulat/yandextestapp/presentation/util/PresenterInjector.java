package ru.mukhutdinov.bulat.yandextestapp.presentation.util;


import android.support.annotation.NonNull;

import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.item.ItemPresenter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.item.ItemPresenterImpl;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.item.ItemView;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListPresenter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListPresenterImpl;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListView;

public class PresenterInjector {

    private PresenterInjector() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @NonNull
    public static ListPresenter provideListPresenter(MainRouter router, ListView listView) {
        return new ListPresenterImpl(router, listView);
    }

    @NonNull
    public static ItemPresenter provideItemPresenter(MainRouter router, ItemView itemView) {
        return new ItemPresenterImpl(router, itemView);
    }
}