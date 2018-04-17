package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list;

import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BasePresenter;

public class ListPresenterImpl extends BasePresenter<ListView, MainRouter> implements ListPresenter {

    public ListPresenterImpl(ListView view, MainRouter router) {
        super(view, router);
    }

    @Override
    public void onClick(Photo photo, ImageView image) {
        router.showPhoto(photo, image);
    }
}