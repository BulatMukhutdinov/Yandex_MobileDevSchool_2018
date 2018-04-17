package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.NetworkState;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.OnPhotoClickListener;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.RetryCallback;

public class PhotoAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private final OnPhotoClickListener listener;

    private NetworkState networkState;

    private RetryCallback retryCallback;

    public PhotoAdapter(RetryCallback retryCallback, OnPhotoClickListener listener) {
        super(PhotoDiffCallback);
        this.retryCallback = retryCallback;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_photo:
                return PhotoViewHolder.create(parent, listener);
            case R.layout.item_network_state:
                return NetworkStateViewHolder.create(parent, retryCallback);
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_photo:
                ((PhotoViewHolder) holder).bindTo(getItem(position));
                // noinspection ConstantConditions
                ViewCompat.setTransitionName(((PhotoViewHolder) holder).image,
                        String.valueOf(getItem(position).getId()));
                break;
            case R.layout.item_network_state:
                ((NetworkStateViewHolder) holder).bindTo(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_network_state;
        } else {
            return R.layout.item_photo;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        if (getCurrentList() != null) {
            if (getCurrentList().size() != 0) {
                NetworkState previousState = this.networkState;
                boolean hadExtraRow = hasExtraRow();
                this.networkState = newNetworkState;
                boolean hasExtraRow = hasExtraRow();
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount());
                    } else {
                        notifyItemInserted(super.getItemCount());
                    }
                } else if (hasExtraRow && previousState != newNetworkState) {
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        }
    }

    private static DiffUtil.ItemCallback<Photo> PhotoDiffCallback = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(Photo oldItem, Photo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Photo oldItem, Photo newItem) {
            return oldItem.getWebFormatURL().equals(newItem.getWebFormatURL());
        }
    };
}