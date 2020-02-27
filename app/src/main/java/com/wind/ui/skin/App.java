package com.wind.ui.skin;

import android.app.Application;

import com.wind.ui.skin.attr.Skins;

/**
 * Created By wind
 * on 2020-02-22
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Skins.getInstance().init(this);


    }
}
