package ru.mukhutdinov.bulat.yandextestapp.presentation.module.list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.databinding.FragmentPhotosBinding;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.NetworkState;
import ru.mukhutdinov.bulat.yandextestapp.domain.state.Status;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainActivity;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BaseFragment;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.adapter.PhotoAdapter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.OnPhotoClickListener;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.PresenterInjector;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.RetryCallback;
import ru.mukhutdinov.bulat.yandextestapp.presentation.viewmodel.PhotoViewModel;

public class ListFragment extends BaseFragment<ListPresenter> implements ListView, RetryCallback,
        OnPhotoClickListener {

    private FragmentPhotosBinding binding;

    private PhotoViewModel photoViewModel;

    private PhotoAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            //noinspection ConstantConditions
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            photoViewModel = ViewModelProviders.of(getActivity()).get(PhotoViewModel.class);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            adapter = new PhotoAdapter(this, this);
            binding.photos.setLayoutManager(gridLayoutManager);
            binding.photos.setAdapter(adapter);
            photoViewModel.getEntities().observe(this, adapter::submitList);
            photoViewModel.getNetworkState().observe(this, adapter::setNetworkState);

            initSwipeToRefresh();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photos, container, false);

        binding.retry.retry.setOnClickListener(v -> retry());

        return binding.getRoot();
    }

    private void initSwipeToRefresh() {
        photoViewModel.getRefreshState().observe(this, networkState -> {
            if (networkState != null) {
                if (adapter.getCurrentList() != null) {
                    if (adapter.getCurrentList().size() > 0) {
                        binding.refresh.setRefreshing(
                                networkState.getStatus() == NetworkState.LOADING.getStatus());
                    } else {
                        setInitialLoadingState(networkState);
                    }
                } else {
                    setInitialLoadingState(networkState);
                }
            }
        });
        binding.refresh.setOnRefreshListener(() -> photoViewModel.refresh());
    }

    private void setInitialLoadingState(NetworkState networkState) {
        binding.retry.error.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMessage() != null) {
            binding.retry.error.setText(networkState.getMessage());
        }

        binding.retry.retry.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        binding.retry.loading.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);

        binding.refresh.setEnabled(networkState.getStatus() == Status.SUCCESS);
    }

    @Override
    public void retry() {
        photoViewModel.retry();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void injectPresenter() {
        presenter = PresenterInjector.provideListPresenter((MainRouter) getActivity(), this);
    }

    @Override
    public void onClick(ImageView photo, String photoUrl) {
        presenter.onClick(photo, photoUrl);
    }
}