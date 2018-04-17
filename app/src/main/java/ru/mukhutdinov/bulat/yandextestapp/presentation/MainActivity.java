package ru.mukhutdinov.bulat.yandextestapp.presentation;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import ru.mukhutdinov.bulat.yandextestapp.R;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.item.ItemFragment;
import ru.mukhutdinov.bulat.yandextestapp.presentation.module.list.ListFragment;

public class MainActivity extends AppCompatActivity implements MainRouter {
    private static final String LIST_TAG = "list_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new ListFragment(), LIST_TAG)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPhoto(ImageView photo, String photoUrl) {
        getSupportFragmentManager().beginTransaction()
                .addSharedElement(photo, ViewCompat.getTransitionName(photo))
                .replace(R.id.container, ItemFragment.newInstance(photoUrl, ViewCompat.getTransitionName(photo)))
                .addToBackStack(null)
                .commit();
    }
}