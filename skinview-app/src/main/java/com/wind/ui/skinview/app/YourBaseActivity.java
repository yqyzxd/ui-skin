package com.wind.ui.skinview.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.wind.ui.skinview.Skins;

public class YourBaseActivity extends AppCompatActivity {

    private Skins mSkins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSkins=Skins.getInstance();
        mSkins.injectFactory2(this,this);
        super.onCreate(savedInstanceState);


        boolean granted = ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        if (!granted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1112);
        }

    }


    /**
     *
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return view maybe null. 当这里返回的view为null时，依然会走 系统创建view的过程。不必担心view为null
     */
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        if (mSkins.isSkinEnabled()) {
            View view = mSkins.onCreateView(parent, name, context, attrs);
            if (view == null) {
                if (getDelegate() instanceof LayoutInflater.Factory2) {
                    LayoutInflater.Factory2 factory2 = (LayoutInflater.Factory2) getDelegate();
                    view = factory2.onCreateView(parent, name, context, attrs);
                }
            }
            return view;
        }

        return super.onCreateView(parent,name,context,attrs);
    }


}
