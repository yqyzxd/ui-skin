package com.wind.ui.skinview.app;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.wind.ui.skinview.Skins;

/**
 * Created By wind
 * on 2020-02-26
 */
public class MainActivity extends YourBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_skin)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      skin();
                    }
                });
    }

    private void skin() {
        String skinPath = Environment.getExternalStorageDirectory() + "/tencent/qqfile_recv/skin-debug.apk";
        Skins.getInstance().skin(skinPath,this,R.color.colorPrimary);
    }


}
