package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list;

import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BasePresenter;

public class ListPresenterImpl extends BasePresenter<ListView, MainRouter> implements ListPresenter {


    public ListPresenterImpl(MainRouter router, ListView view) {
        this.router = router;
        this.view = view;
    }

    @Override
    public void onStop() {
        router = null;
        view = null;
    }

    @Override
    public void onClick(ImageView photo, String photoUrl) {
        router.showPhoto(photo, photoUrl);
    }
}