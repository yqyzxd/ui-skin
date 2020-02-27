package com.wind.ui.skinview.app;

import android.app.Application;

import com.wind.ui.skinview.Skins;

/**
 * Created By wind
 * on 2020-02-26
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Skins.init(this);
    }
}
