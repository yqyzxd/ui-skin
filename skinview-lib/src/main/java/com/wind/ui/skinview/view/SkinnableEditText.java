package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;

/**
 * Created By wind
 * on 2020-02-25
 */
public class SkinnableEditText extends AppCompatEditText implements Skinnable {

    private SkinAttributes mSkinAttributes;
    public SkinnableEditText(Context context) {
        this(context,null);
    }

    public SkinnableEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SkinnableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取attrs中设置的可以换肤的属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SkinnableEditView,defStyleAttr,0);

        mSkinAttributes=new SkinAttributes(typedArray,R.styleable.SkinnableEditView);

        typedArray.recycle();

    }


    @Override
    public void skin() {

        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinnableEditView[R.styleable.SkinnableEditView_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setBackground(this,backgroundResourceId);

        // 根据自定义属性，获取styleable中的textColor属性
        key = R.styleable.SkinnableEditView[R.styleable.SkinnableEditView_android_textColor];
        int textColorResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setTextColor(this,textColorResourceId);

        // 根据自定义属性，获取styleable中的字体 skin_typeface 属性
        key = R.styleable.SkinnableEditView[R.styleable.SkinnableEditView_skin_typeface];
        int textTypefaceResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setTypeface(this,textTypefaceResourceId);





    }
}
