package com.wind.ui.skin.attr;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created By wind
 * on 2020-02-22
 *
 * 替换皮肤的管理类
 *
 * 1. 如何获取 插件皮肤包的资源
 * 利用AssetsManager构造一个新的Resources
 *
 *
 */
public class Skins {

    private static Skins sInstance = null;
    //皮肤包的资源类
    private Resources mSkinResource;
    //皮肤包的包名
    private String mSkinApkPkgName;
    /**
     * app对应的applicationContext
     */
    private Context mContext;

    private Skins() {
    }

    public static Skins getInstance() {
        if (sInstance == null) {
            synchronized (Skins.class) {
                if (sInstance == null) {
                    sInstance = new Skins();
                }
            }
        }
        return sInstance;
    }

    /**
     * 尽可能早的执行，可以选择在Application的onCreate中执行
     *
     * @param application
     */
    public void init(Application application) {
        mContext = application.getApplicationContext();
        injectSkinFactory(application);
    }


    /**
     * 监听Activity生命周期，在onActivityCreated中注入 SkinFactory以拦截view的创建过程
     * @param application
     */
    private static void injectSkinFactory(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            /**
             * 该方法在自己的activity的onCreate之前执行
             * @param activity
             * @param savedInstanceState
             */
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                System.out.println("onActivityCreated");
                //todo 反射将LayoutInflater中的mFactorySet设置为false
                try {
                    //为什么需要将mFactorySet设置为false？ 因为AppCompatActivity会设置它的Factory2
                    Field factorySetField = LayoutInflater.class.getDeclaredField("mFactorySet");
                    factorySetField.setAccessible(true);
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    factorySetField.set(inflater, false);
                    SkinFactory skinFactory;
                    if (activity instanceof AppCompatActivity) {
                        AppCompatActivity compatActivity = (AppCompatActivity) activity;
                        skinFactory = new SkinFactory(compatActivity.getDelegate());
                    } else {
                        skinFactory = new SkinFactory();
                    }
                    //设置factory2 hook 系统创建view的过程
                    inflater.setFactory2(skinFactory);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    /**
     * 原理：
     * 通过backgroundId 获取 宿主中该资源的名称 和 类型
     * 从皮肤包中通过该资源名称和资源类型去获取 皮肤包中相应的资源
     *
     * @param backgroundId 宿主app中的 background 指向的资源id
     * @return
     */
    public Drawable getDrawable(int backgroundId) {
        if (mSkinResource == null) {
            return mContext.getResources().getDrawable(backgroundId);
        }

        String entryName = mContext.getResources().getResourceEntryName(backgroundId);
        String typeName = mContext.getResources().getResourceTypeName(backgroundId);

        int skinBackgroundId = mSkinResource.getIdentifier(entryName, typeName, mSkinApkPkgName);
        if (skinBackgroundId == 0) {
            //皮肤包中不存在该资源，则返回宿主app中的资源
            return mContext.getResources().getDrawable(backgroundId);
        }


        return mSkinResource.getDrawable(skinBackgroundId);
    }

    public int getColor(int colorId) {
        if (mSkinResource == null) {
            return mContext.getResources().getColor(colorId);
        }

        String entryName = mContext.getResources().getResourceEntryName(colorId);
        String typeName = mContext.getResources().getResourceTypeName(colorId);
        int skinColorId = mSkinResource.getIdentifier(entryName, typeName, mSkinApkPkgName);
        if (skinColorId == 0) {
            return mContext.getResources().getColor(colorId);
        }
        return mSkinResource.getColor(skinColorId);
    }


    /**
     * 从skinApkPath指定的路径加载资源包
     *
     * @param skinApkPath
     */
    public void load(String skinApkPath) {

        if (mContext == null) {
            throw new IllegalStateException("please first call init method");
        }

        //1.  获取skinApk的包名
        mSkinApkPkgName = getSkinApkPkgName(skinApkPath);
        //2. 生成skinApk的Resources
        mSkinResource = createSkinResource(skinApkPath);


    }

    private String getSkinApkPkgName(String skinApkPath) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(skinApkPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo == null) {
            throw new IllegalArgumentException(skinApkPath + " is not a legal apk");
        }
        return packageInfo.packageName;
    }

    private Resources createSkinResource(String skinApkPath) {

        //如何创建一个Resources
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //通过调用 public int addAssetPath(String path) 方法将外部apk包路径传递给assetManager，这样就可以获得其中的资源了
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, skinApkPath);

            Resources hostResources = mContext.getResources();
            Resources resources = new Resources(assetManager,
                    hostResources.getDisplayMetrics(),
                    hostResources.getConfiguration());
            return resources;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

    /**
     *
     *  public void setFactory(Factory factory) {
     *         if (mFactorySet) {
     *             throw new IllegalStateException("A factory has already been set on this LayoutInflater");
     *         }
     *         if (factory == null) {
     *             throw new NullPointerException("Given factory can not be null");
     *         }
     *         mFactorySet = true;
     *         if (mFactory == null) {
     *             mFactory = factory;
     *         } else {
     *             mFactory = new FactoryMerger(factory, null, mFactory, mFactory2);
     *         }
     *     }
     *
     *
     * @param context
     * @return
     */
    public static SkinFactory getSkinFactory(Context context){
        LayoutInflater.Factory2 factory=LayoutInflater.from(context).getFactory2();
        SkinFactory skinFactory=null;
        /*
         *   此处返回值说明
         * 若调用LayoutInflater.from(context).setFactory2时已经设置过Factory2那么mFactory不为null
         * 所有 mFactory 就是FactoryMerger，若mFactory为null则就是自己设置的Factory2
         */
        if (factory instanceof SkinFactory){
            skinFactory= (SkinFactory) factory;
        }else {
            try {
                Field mF12Field=factory.getClass().getDeclaredField("mF12");
                mF12Field.setAccessible(true);
                LayoutInflater.Factory2 factory2= (LayoutInflater.Factory2) mF12Field.get(factory);
                if (factory2 instanceof SkinFactory){
                    skinFactory= (SkinFactory) factory2;

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
       return skinFactory;
    }
}
