package ru.mukhutdinov.bulat.yandextestapp;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import ru.mukhutdinov.bulat.yandextestapp.data.network.ApiImpl;

public class App extends Application {
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ApiImpl.init(getString(R.string.server_url));
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}