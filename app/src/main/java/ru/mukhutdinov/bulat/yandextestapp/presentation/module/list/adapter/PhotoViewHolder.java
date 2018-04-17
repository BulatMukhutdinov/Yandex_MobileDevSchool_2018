package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.OnPhotoClickListener;

class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final OnPhotoClickListener listener;

    ImageView image;

    private Photo photo;

    private PhotoViewHolder(View itemView, OnPhotoClickListener listener) {
        super(itemView);
        image = itemView.findViewById(R.id.photo);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    void bindTo(Photo item) {
        photo = item;

        Picasso.with(itemView.getContext())
                .load(item.getWebFormatURL())
                .placeholder(R.color.background)
                .into(image);
    }

    static PhotoViewHolder create(ViewGroup parent, OnPhotoClickListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view, listener);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(photo, image);
    }
}