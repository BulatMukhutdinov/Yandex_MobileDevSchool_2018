package ru.mukhutdinov.bulat.yandextestapp.presentation.module.item;

import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BasePresenter;

public class ItemPresenterImpl extends BasePresenter<ItemView, MainRouter> implements ItemPresenter {

    public ItemPresenterImpl(ItemView view, MainRouter router) {
        super(view, router);
    }
}