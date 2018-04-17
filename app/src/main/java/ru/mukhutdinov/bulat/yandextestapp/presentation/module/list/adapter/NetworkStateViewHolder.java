package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.NetworkState;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.Status;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.RetryCallback;


class NetworkStateViewHolder extends RecyclerView.ViewHolder {

    private TextView error;

    private Button retry;

    private ProgressBar loading;

    private NetworkStateViewHolder(View itemView, RetryCallback retryCallback) {
        super(itemView);
        error = itemView.findViewById(R.id.error);
        loading = itemView.findViewById(R.id.loading);
        retry = itemView.findViewById(R.id.retry);
        retry.setOnClickListener(v -> retryCallback.retry());
    }

    void bindTo(NetworkState networkState) {
        error.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMessage() != null) {
            error.setText(networkState.getMessage());
        }

        retry.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        loading.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);
    }

    static NetworkStateViewHolder create(ViewGroup parent, RetryCallback retryCallback) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_network_state, parent, false);
        return new NetworkStateViewHolder(view, retryCallback);
    }
}