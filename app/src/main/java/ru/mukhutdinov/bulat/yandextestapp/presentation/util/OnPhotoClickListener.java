package ru.mukhutdinov.bulat.yandextestapp.presentation.util;

import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.data.Photo;

public interface OnPhotoClickListener {

    void onClick(Photo photo, ImageView image);
}