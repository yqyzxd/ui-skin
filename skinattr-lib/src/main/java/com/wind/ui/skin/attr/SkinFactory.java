package com.wind.ui.skin.attr;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By wind
 * on 2020-02-22
 * 自定义的Factory2用于拦截系统创建view的过程
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private AppCompatDelegate mAppCompatDelegate;
    /**
     * 保存view构造函数的参数
     */
    final Object[] mConstructorArgs = new Object[2];

    /**
     * 缓存view的构造函数
     */
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    /**
     * view的构造参数的参数
     */
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    /**
     * 系统view的包名
     */
    private static final String[] prefixs = new String[]{
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    //保存需要换肤的view
    private List<ViewAttrHolder> mViewAttrHolderList=new ArrayList<>();

    public SkinFactory() {
    }
    public SkinFactory(AppCompatDelegate delegate) {
        this.mAppCompatDelegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name,
                             @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        View view=null;
        if (mAppCompatDelegate!=null) {
            view= mAppCompatDelegate.createView(parent, name, context, attrs);
        }
        if (view == null) {
            mConstructorArgs[0] = context;
            if (-1 == name.indexOf('.')) {
                view = createView(context, name, prefixs, attrs);
            } else {
                view = createView(context, name, null, attrs);
            }
        }

        if (view != null) {
            collectSkinView(context, attrs, view);
        }

        return view;
    }

    public final View createView(Context context, String name, String[] prefixs, AttributeSet attrs) {

        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz = null;

        if (constructor == null) {
            try {
                if (prefixs != null && prefixs.length > 0) {
                    for (String prefix : prefixs) {
                        clazz = context.getClassLoader().loadClass(
                                prefix != null ? (prefix + name) : name).asSubclass(View.class);
                        if (clazz != null) break;
                    }
                } else {
                    if (clazz == null) {
                        clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                    }
                }
//                Log.i("Zero", "clazz: " + clazz);
                if (clazz == null) {
                    return null;
                }
                constructor = clazz.getConstructor(mConstructorSignature);
//                Log.i("Zero", "constructor: " + constructor);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            constructor.setAccessible(true);
            sConstructorMap.put(name, constructor);
        }
        Object[] args = mConstructorArgs;
        args[1] = attrs;
        try {
            final View view = constructor.newInstance(args);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void collectSkinView(Context context, AttributeSet attrs, View view) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Skinable);

        //查找被app:skinable=true标记的view
        boolean skinable = typedArray.getBoolean(R.styleable.Skinable_skinable, false);
        if (skinable) {
            //保存属性的 map
            Map<String, String> attrMap = new HashMap<>();
            //取出该view的所有属性
            int attributeCount = attrs.getAttributeCount();
            for (int i = 0; i < attributeCount; i++) {
                String attrName = attrs.getAttributeName(i);
                String attrValue = attrs.getAttributeValue(i);

                attrMap.put(attrName, attrValue);
            }
            ViewAttrHolder viewAttrHolder=new ViewAttrHolder(view,attrMap);
            mViewAttrHolderList.add(viewAttrHolder);

        }

        typedArray.recycle();
    }


    /**
     * 保存需要换肤的view及其属性
     */
    public static class ViewAttrHolder{
        private Map<String, String> attrMap;
        private View view;
        public ViewAttrHolder(View view,Map<String, String> attrMap){
            this.view=view;
            this.attrMap=attrMap;
        }

        public Map<String, String> getAttrMap() {
            return attrMap;
        }

        public View getView() {
            return view;
        }

        /**
         * 换肤换肤到底换的是什么东西
         * 1.background
         * 2.textColor
         */
        public void toggleSkin() {
            if (!TextUtils.isEmpty(attrMap.get("background"))){
                String sBackground=attrMap.get("background").substring(1);
                int backgroundId=Integer.parseInt(sBackground);
                String resourceTypeName=view.getResources().getResourceTypeName(backgroundId);
                if (TextUtils.equals("drawable",resourceTypeName)){
                    view.setBackgroundDrawable(Skins.getInstance().getDrawable(backgroundId));
                }else if (TextUtils.equals("color",resourceTypeName)){
                    view.setBackgroundColor(Skins.getInstance().getColor(backgroundId));
                }


            }
            if (view instanceof TextView){
                if (!TextUtils.isEmpty(attrMap.get("textColor"))) {
                    int textColorId = Integer.parseInt(attrMap.get("textColor").substring(1));
                    ((TextView) view).setTextColor(Skins.getInstance().getColor(textColorId));
                }
            }
        }
    }


    /**
     * 切换皮肤
     */
    public void toggleSkin(){
        for (ViewAttrHolder viewAttrHolder : mViewAttrHolderList) {
            viewAttrHolder.toggleSkin();

        }
    }

    /**
     * 无须理会这个方法
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }
}
