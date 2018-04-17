package ru.mukhutdinov.bulat.yandextestapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import ru.mukhutdinov.bulat.yandextestapp.presentation.MainRouter;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.item.ItemPresenterImpl;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.item.ItemView;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListView;

@RunWith(PowerMockRunner.class)
public class ItemPresenterUnitTest {

    private ItemPresenterImpl presenter;

    @Mock
    private MainRouter router;

    @Mock
    private ItemView view;

    @Before
    public void setUp() {
        presenter = new ItemPresenterImpl(view, router);
    }

    @Test
    public void onStop_isResourcesCleared() throws Exception {
        presenter.onStop();

        MainRouter router = Whitebox.getInternalState(presenter, "router");
        ListView view = Whitebox.getInternalState(presenter, "view");

        Assert.assertNull(router);
        Assert.assertNull(view);
    }
}