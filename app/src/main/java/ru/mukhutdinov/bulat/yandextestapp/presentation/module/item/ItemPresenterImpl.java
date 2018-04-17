package ru.mukhutdinov.bulat.yandextestapp.presentation.module.item;

import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BasePresenter;

public class ItemPresenterImpl extends BasePresenter<ItemView, MainRouter> implements ItemPresenter {

    public ItemPresenterImpl(MainRouter router, ItemView view) {
        this.router = router;
        this.view = view;
    }

    @Override
    public void onStop() {
        router = null;
        view = null;
    }
}
