package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;

/**
 * 继承TextView兼容包，9.0源码中也是如此
 * 参考：AppCompatViewInflater.java
 * 86行 + 138行 + 206行
 */
public class SkinnableButton extends AppCompatButton implements Skinnable {

    private SkinAttributes mSkinAttributes;
    private Skins mSkins;

    public SkinnableButton(Context context) {
        this(context, null);
    }

    public SkinnableButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.buttonStyle);
    }

    public SkinnableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSkins = Skins.getInstance();
        // 根据自定义属性，匹配控件属性的类型集合，如：background + textColor
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableButton,
                defStyleAttr, 0);
        mSkinAttributes = new SkinAttributes(typedArray, R.styleable.SkinnableButton);


        typedArray.recycle();
    }

    @Override
    public void skin() {
        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinnableButton[R.styleable.SkinnableButton_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = mSkinAttributes.getResourceId(key);
        mSkins.setBackground(this, backgroundResourceId);


        // 根据自定义属性，获取styleable中的textColor属性
        key = R.styleable.SkinnableButton[R.styleable.SkinnableButton_android_textColor];
        int textColorResourceId = mSkinAttributes.getResourceId(key);
        mSkins.setTextColor(this, textColorResourceId);

        // 根据自定义属性，获取styleable中的字体 custom_typeface 属性
        key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_skin_typeface];
        int textTypefaceResourceId = mSkinAttributes.getResourceId(key);
        mSkins.setTypeface(this, textTypefaceResourceId);
    }


}
