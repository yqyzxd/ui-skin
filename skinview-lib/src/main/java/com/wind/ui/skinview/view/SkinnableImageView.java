package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;


/**
 * 继承TextView兼容包，9.0源码中也是如此
 * 参考：AppCompatViewInflater.java
 * 86行 + 138行 + 206行
 */
public class SkinnableImageView extends AppCompatImageView implements Skinnable {

    private SkinAttributes mSkinAttributes;

    public SkinnableImageView(Context context) {
        this(context, null);
    }

    public SkinnableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 根据自定义属性，匹配控件属性的类型集合，如：src
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableImageView,
                defStyleAttr, 0);
        mSkinAttributes = new SkinAttributes(typedArray,R.styleable.SkinnableImageView);

        typedArray.recycle();
    }

    @Override
    public void skin() {
        // 根据自定义属性，获取styleable中的src属性
        int key = R.styleable.SkinnableImageView[R.styleable.SkinnableImageView_android_src];
        // 根据styleable获取控件某属性的resourceId
        int srcResourceId = mSkinAttributes.getResourceId(key);
        if (srcResourceId > 0) {
            Object skinResourceId = Skins.getInstance().getBackground(srcResourceId);
            if (skinResourceId instanceof Integer) {
                int color = (int) skinResourceId;
                setImageResource(color);
                // setImageBitmap(); // Bitmap未添加
            } else {
                Drawable drawable = (Drawable) skinResourceId;
                setImageDrawable(drawable);
            }
        }
    }
}
