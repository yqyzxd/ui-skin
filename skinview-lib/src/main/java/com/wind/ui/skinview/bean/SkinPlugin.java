package com.wind.ui.skinview.bean;

import android.content.res.Resources;

/**
 * Created By wind
 * on 2020-02-26
 */
public class SkinPlugin {

    private String mPackageName;
    private Resources mResources;

    public SkinPlugin(String packageName, Resources resources) {
        this.mPackageName = packageName;
        this.mResources = resources;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public Resources getResources() {
        return mResources;
    }
}
