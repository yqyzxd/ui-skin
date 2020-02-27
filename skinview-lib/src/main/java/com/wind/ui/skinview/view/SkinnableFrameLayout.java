package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;

public class SkinnableFrameLayout extends FrameLayout implements Skinnable {

    private SkinAttributes mSkinAttributes;

    public SkinnableFrameLayout(Context context) {
        this(context, null);
    }

    public SkinnableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 根据自定义属性，匹配控件属性的类型集合，如：background
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableFrameLayout,
                defStyleAttr, 0);
        mSkinAttributes = new SkinAttributes(typedArray,R.styleable.SkinnableFrameLayout);

        typedArray.recycle();
    }

    @Override
    public void skin() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinnableFrameLayout[R.styleable.SkinnableFrameLayout_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setBackground(this,backgroundResourceId);
    }
}
