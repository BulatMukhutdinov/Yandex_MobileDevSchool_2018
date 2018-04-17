package ru.mukhutdinov.bulat.yandextestapp.presentation.module.item;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainActivity;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BaseFragment;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.PresenterInjector;

import static ru.mukhutdinov.bulat.yandextestapp.presentation.util.Constants.PHOTO_URL;
import static ru.mukhutdinov.bulat.yandextestapp.presentation.util.Constants.TRANSITION_NAME;


public class ItemFragment extends BaseFragment<ItemPresenter> implements ItemView {

    public static BaseFragment newInstance(String photo, String transitionName) {
        BaseFragment fragment = new ItemFragment();
        Bundle extras = new Bundle();
        extras.putString(PHOTO_URL, photo);
        extras.putString(TRANSITION_NAME, transitionName);
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    protected void injectPresenter() {
        presenter = PresenterInjector.provideItemPresenter((MainRouter) getActivity(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        ImageView photo = view.findViewById(R.id.photo);

        if (getActivity() != null) {
            //noinspection ConstantConditions
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        if (getArguments() != null) {

            String transitionName = getArguments().getString(TRANSITION_NAME);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photo.setTransitionName(transitionName);
            }

            Picasso.with(getContext())
                    .load(getArguments().getString(PHOTO_URL))
                    .noFade()
                    .into(photo, new Callback() {
                        @Override
                        public void onSuccess() {
                            startPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            startPostponedEnterTransition();
                        }
                    });
        }

        return view;
    }
}