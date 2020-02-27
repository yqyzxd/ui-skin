package com.wind.ui.skinview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.wind.ui.skinview.bean.SkinPlugin;
import com.wind.ui.skinview.utils.ActionBarUtils;
import com.wind.ui.skinview.utils.NavigationUtils;
import com.wind.ui.skinview.utils.StatusBarUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By wind
 * on 2020-02-25
 */
public class Skins {

    private boolean mSkinEnabled=true;
    private static Skins sInstance=null;
    /**app内置的Resources*/
    private Resources mAppResources;
    /**皮肤包的Resources*/
    private Resources mSkinResources;
    private String mSkinPkgName;
    /**是否使用app内置资源*/
    private boolean mDefaultSkin;
    /**皮肤插件包缓存*/
    private Map<String, SkinPlugin> mSkinPluginMap;
    private Application mApplication;
    private Skins(Application application){
        this.mApplication=application;
        mAppResources=application.getResources();
        mSkinPluginMap=new HashMap<>();
    }

    /**
     * Applicatio onCreate中进行初始化
     * @param application
     * @return
     */
    public static Skins init(Application application){
        if (sInstance==null){
            synchronized (Skins.class){
                if (sInstance==null){
                    sInstance=new Skins(application);
                }
            }
        }
        return sInstance;
    }

    public static Skins getInstance(){
        if (sInstance==null){
            throw new IllegalStateException("have you call init(application)?");
        }
        return sInstance;
    }


    private SkinAppCompatViewInflater mSkinAppCompatViewInflater;
    /**
     * 需要在super.onCreate()之前调用
     * @param context
     * @param factory2
     */
    public void injectFactory2(Context context, LayoutInflater.Factory2 factory2){
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        LayoutInflaterCompat.setFactory2(layoutInflater,factory2);
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs){
        if (mSkinAppCompatViewInflater==null){
            mSkinAppCompatViewInflater=new SkinAppCompatViewInflater();
        }
        View view=mSkinAppCompatViewInflater.createSkinView(parent,context,name,attrs);
        return view;
    }


    /**
     * 加载皮肤包 皮肤包其实也是一个apk文件
     * @param skinPath
     */
    private void loadSkin(String skinPath){

        if (mSkinPluginMap.containsKey(skinPath)){
            SkinPlugin skinPlugin=mSkinPluginMap.get(skinPath);
            if (skinPlugin!=null) {
                mDefaultSkin=false;
                mSkinPkgName = skinPlugin.getPackageName();
                mSkinResources = skinPlugin.getResources();
                return;
            }
        }


        mSkinResources=newSkinResoures(skinPath);
        //获取皮肤包 包名
        PackageInfo packageInfo =mApplication.getPackageManager().getPackageArchiveInfo(
                skinPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo==null){
            mDefaultSkin=true;
        }else {
            mDefaultSkin=false;
            mSkinPkgName=packageInfo.packageName;
            SkinPlugin skinPlugin=new SkinPlugin(mSkinPkgName,mSkinResources);
            mSkinPluginMap.put(skinPath,skinPlugin);
        }


    }

    /**
     * 对整个activity执行换肤
     * @param skinPath
     * @param activity
     * @param themeColorId
     */
    public void skin(String skinPath, Activity activity, int themeColorId){
        loadSkin(skinPath);

        if (themeColorId!=0){
            int themeColor=getColor(themeColorId);
            StatusBarUtils.forStatusBar(activity, themeColor);
            NavigationUtils.forNavigation(activity, themeColor);
            if (activity instanceof AppCompatActivity)
                ActionBarUtils.forActionBar((AppCompatActivity) activity, themeColor);
        }

        doSkin(activity.getWindow().getDecorView());
    }


    private void doSkin(View view){
        if (view instanceof Skinnable){
            Skinnable skinable= (Skinnable) view;
            skinable.skin();
        }
        if (view instanceof ViewGroup){
            ViewGroup parent= (ViewGroup) view;
            for (int i=0;i<parent.getChildCount();i++){
                doSkin(parent.getChildAt(i));
            }
        }
    }

    private Resources newSkinResoures(String skinPath){
        Resources skinResources=null;
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            //反射public int addAssetPath(String path)
            Method addAssetPathMethod=AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            addAssetPathMethod.invoke(assetManager,skinPath);


            skinResources=new Resources(assetManager,mAppResources.getDisplayMetrics(),
                    mAppResources.getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return skinResources;
    }


    /**
     * 通过 宿主app的resourceId 获取 皮肤包中相对应的skinResourceId
     * @param resourceId
     * @return
     */
    private int getSkinResouresId(int resourceId){
        if (mDefaultSkin){
            return resourceId;
        }

        String resourceEntryName=mAppResources.getResourceEntryName(resourceId);
        String resourceTypeName=mAppResources.getResourceTypeName(resourceId);

        int skinResoureId=mSkinResources.getIdentifier(resourceEntryName,resourceTypeName,mSkinPkgName);

        mDefaultSkin=skinResoureId==0;

        return skinResoureId==0? resourceId:skinResoureId;

    }


    public int getColor(int resourceId){
        int skinResouresId=getSkinResouresId(resourceId);
        return mDefaultSkin?mAppResources.getColor(skinResouresId):mSkinResources.getColor(skinResouresId);
    }

    public ColorStateList getColorStateList(int resourceId) {
        int ids = getSkinResouresId(resourceId);
        return mDefaultSkin ? mAppResources.getColorStateList(ids) : mSkinResources.getColorStateList(ids);
    }

    public Drawable getDrawable(int resourceId) {
        int ids = getSkinResouresId(resourceId);
        return mDefaultSkin ? mAppResources.getDrawable(ids) : mSkinResources.getDrawable(ids);
    }

    public Object getBackground(int resourceId){
        String resourceTypeName=mAppResources.getResourceTypeName(resourceId);
        Object resource=null;
        switch (resourceTypeName){
            case "color":
                resource= getColor(resourceId);
                break;
            case "mipmap":
            case "drawable":
                resource=getDrawable(resourceId);
                break;
        }
        return resource;
    }
    public String getString(int resourceId) {
        int ids = getSkinResouresId(resourceId);
        return mDefaultSkin ? mAppResources.getString(ids) : mSkinResources.getString(ids);
    }
    // 获得字体
    public Typeface getTypeface(int resourceId) {
        // 通过资源ID获取资源path，参考：resources.arsc资源映射表
        String skinTypefacePath = getString(resourceId);
        // 路径为空，使用系统默认字体
        if (TextUtils.isEmpty(skinTypefacePath)) return Typeface.DEFAULT;
        return mDefaultSkin ? Typeface.createFromAsset(mAppResources.getAssets(), skinTypefacePath)
                : Typeface.createFromAsset(mSkinResources.getAssets(), skinTypefacePath);
    }


    public void setSkinEnabled(boolean enabled){
        mSkinEnabled=enabled;
    }
    public boolean isSkinEnabled() {
        return mSkinEnabled;
    }


    /**
     * 设置给的view的背景资源
     * @param view
     * @param backgroundResourceId
     */
    public void setBackground(View view,int backgroundResourceId){
        if (backgroundResourceId > 0) {
            // 获取皮肤包资源
            Object skinResourceId =getBackground(backgroundResourceId);
            // 兼容包转换
            if (skinResourceId instanceof Integer) {
                int color = (int) skinResourceId;
                view.setBackgroundColor(color);
            } else {
                Drawable drawable = (Drawable) skinResourceId;
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    /**
     * 设置给定textView的文字颜色
     * @param textView
     * @param textColorResourceId
     */
    public void setTextColor(TextView textView,int textColorResourceId){
        if (textColorResourceId > 0) {
            ColorStateList color =getColorStateList(textColorResourceId);
            textView.setTextColor(color);
        }
    }

    /**
     * 设置给定textView的文字字体
     * @param textView
     * @param textTypefaceResourceId
     */
    public void setTypeface(TextView textView,int textTypefaceResourceId){
        if (textTypefaceResourceId > 0) {
            textView.setTypeface(getTypeface(textTypefaceResourceId));
        }
    }

}
