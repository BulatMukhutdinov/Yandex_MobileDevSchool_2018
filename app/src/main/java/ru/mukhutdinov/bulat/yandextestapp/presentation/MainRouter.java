package ru.mukhutdinov.bulat.yandextestapp.presentation;

import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.presentation.base.Router;

public interface MainRouter extends Router {

    void showPhoto(ImageView photo, String photoUrl);
}