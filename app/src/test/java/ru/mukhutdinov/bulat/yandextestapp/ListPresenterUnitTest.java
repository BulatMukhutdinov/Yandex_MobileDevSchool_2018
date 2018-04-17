package ru.mukhutdinov.bulat.yandextestapp;

import android.widget.ImageView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import ru.mukhutdinov.bulat.yandextestapp.data.Photo;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListPresenterImpl;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class ListPresenterUnitTest {

    private ListPresenterImpl presenter;

    @Mock
    private MainRouter router;

    @Mock
    private ListView view;

    @Before
    public void setUp() {
        presenter = new ListPresenterImpl(view, router);
    }

    @Test
    public void onStop_isResourcesCleared() throws Exception {
        presenter.onStop();

        MainRouter router = Whitebox.getInternalState(presenter, "router");
        ListView view = Whitebox.getInternalState(presenter, "view");

        Assert.assertNull(router);
        Assert.assertNull(view);
    }

    @Test
    public void onClick_isRouterCalled() throws Exception {
        ImageView image = mock(ImageView.class);
        Photo photo = new Photo(1, "url", "tags");

        presenter.onClick(photo, image);

        verify(router).showPhoto(photo, image);
    }
}