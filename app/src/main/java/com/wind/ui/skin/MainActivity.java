package com.wind.ui.skin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.wind.ui.skin.attr.SkinFactory;
import com.wind.ui.skin.attr.Skins;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_main);

       boolean granted= ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;

        if (!granted){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1112);
        }
        findViewById(R.id.btn_skin)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleSkinByAttr();
                    }
                });
    }



    private void toggleSkinByAttr(){
        try {
            SkinFactory skinFactory= Skins.getSkinFactory(this);

            if (skinFactory!=null) {
                String skinPath = Environment.getExternalStorageDirectory() + "/tencent/qqfile_recv/skin-debug.apk";
                Skins.getInstance().load(skinPath);
                skinFactory.toggleSkin();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
