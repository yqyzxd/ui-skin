package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;

public class SkinnableRelativeLayout extends RelativeLayout implements Skinnable {

    private SkinAttributes mSkinAttributes;

    public SkinnableRelativeLayout(Context context) {
        this(context, null);
    }

    public SkinnableRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 根据自定义属性，匹配控件属性的类型集合，如：background
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableRelativeLayout,
                defStyleAttr, 0);
        mSkinAttributes = new SkinAttributes(typedArray,R.styleable.SkinnableRelativeLayout);

        typedArray.recycle();
    }

    @Override
    public void skin() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinnableRelativeLayout[R.styleable.SkinnableRelativeLayout_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setBackground(this,backgroundResourceId);
    }
}
