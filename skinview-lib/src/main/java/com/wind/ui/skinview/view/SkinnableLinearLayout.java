package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;

/**
 * Created By wind
 * on 2020-02-26
 */
public class SkinnableLinearLayout extends LinearLayout implements Skinnable {
    private SkinAttributes mSkinAttributes;
    public SkinnableLinearLayout(Context context) {
        this(context,null);
    }

    public SkinnableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SkinnableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SkinnableLinearLayout);
        mSkinAttributes=new SkinAttributes(typedArray,R.styleable.SkinnableLinearLayout);

        typedArray.recycle();
    }



    @Override
    public void skin() {

        int key = R.styleable.SkinnableLinearLayout[R.styleable.SkinnableLinearLayout_android_background];
        int backgroundResourceId = mSkinAttributes.getResourceId(key);

        Skins.getInstance().setBackground(this,backgroundResourceId);
    }
}
