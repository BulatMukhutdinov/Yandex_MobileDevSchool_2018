package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list;

import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.presentation.base.Presenter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.OnPhotoClickListener;

public interface ListPresenter extends Presenter {

    void onClick(ImageView photo, String photoUrl);
}