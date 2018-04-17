package ru.mukhutdinov.bulat.yandextestapp.presentation.base;

import android.support.v4.app.Fragment;

public abstract class BaseFragment<P extends Presenter> extends Fragment implements View {
    protected P presenter;

    @Override
    public void onStart() {
        super.onStart();
        injectPresenter();
    }

    protected abstract void injectPresenter();
}