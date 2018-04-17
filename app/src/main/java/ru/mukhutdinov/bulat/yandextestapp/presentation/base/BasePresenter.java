package ru.mukhutdinov.bulat.yandextestapp.presentation.base;

public abstract class BasePresenter<V extends View, R extends Router> implements Presenter {
    protected V view;
    protected R router;

    public BasePresenter(V view, R router) {
        this.view = view;
        this.router = router;
    }

    @Override
    public void setView(View v) {
        //noinspection unchecked
        this.view = (V) v;
    }

    @Override
    public void onStop() {
        router = null;
        view = null;
    }
}