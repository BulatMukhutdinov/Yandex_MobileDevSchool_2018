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
import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainActivity;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.base.BaseFragment;
import ru.mukhutdinov.bulat.yandextestapp.presentation.util.PresenterInjector;

import static ru.mukhutdinov.bulat.yandextestapp.presentation.util.Constants.PHOTO;
import static ru.mukhutdinov.bulat.yandextestapp.presentation.util.Constants.TRANSITION_NAME;


public class ItemFragment extends BaseFragment<ItemPresenter> implements ItemView {

    public static BaseFragment newInstance(Photo photo, String transitionName) {
        BaseFragment fragment = new ItemFragment();
        Bundle extras = new Bundle();
        extras.putSerializable(PHOTO, photo);
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
        presenter = PresenterInjector.provideItemPresenter(this, (MainRouter) getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ImageView image = view.findViewById(R.id.photo);

        if (getActivity() != null && ((MainActivity) getActivity()).getSupportActionBar() != null
                && getArguments() != null) {
            Photo photo = (Photo) getArguments().getSerializable(PHOTO);
            String transitionName = getArguments().getString(TRANSITION_NAME);

            //noinspection ConstantConditions
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //noinspection ConstantConditions
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(photo.getTags());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setTransitionName(transitionName);
            }

            //noinspection ConstantConditions
            Picasso.with(getContext())
                    .load(photo.getWebFormatURL())
                    .noFade()
                    .into(image, new Callback() {
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