package com.wind.ui.skinview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.wind.ui.skinview.R;
import com.wind.ui.skinview.SkinAttributes;
import com.wind.ui.skinview.Skinnable;
import com.wind.ui.skinview.Skins;

/**
 * Created By wind
 * on 2020-02-25
 */
public class SkinnableTextView extends AppCompatTextView implements Skinnable {

    private SkinAttributes mSkinAttributes;
    public SkinnableTextView(Context context) {
        this(context,null);
    }

    public SkinnableTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SkinnableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取attrs中设置的可以换肤的属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SkinnableTextView,defStyleAttr,0);

        mSkinAttributes=new SkinAttributes(typedArray,R.styleable.SkinnableTextView);

        String  typefacePath=typedArray.getString(R.styleable.SkinnableTextView_skin_typeface);
        if (!TextUtils.isEmpty(typefacePath)) {
            setTypeface(Typeface.createFromAsset(getResources().getAssets(), typefacePath));

        }
        typedArray.recycle();
    }


    @Override
    public void skin() {


        //日间/夜间模式 使用以下方式获取background
       /* int backgroundKey=R.styleable.SkinableTextView[R.styleable.SkinableTextView_android_background];
        int backgroundResourceId=mSkinAttributes.getResourceId(backgroundKey);
        if (backgroundResourceId>0){
            Drawable drawable=ContextCompat.getDrawable(getContext(),backgroundResourceId);
            setBackground(drawable);
        }
        //获取textColor
        int textColorId=R.styleable.SkinableTextView[R.styleable.SkinableTextView_android_textColor];
        int textColorResourceId=mSkinAttributes.getResourceId(textColorId);
        if (textColorResourceId>0){
            ColorStateList color=ContextCompat.getColorStateList(getContext(),textColorResourceId);
            setTextColor(color);
        }*/

        // 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setBackground(this,backgroundResourceId);

        // 根据自定义属性，获取styleable中的textColor属性
        key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_android_textColor];
        int textColorResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setTextColor(this,textColorResourceId);

        // 根据自定义属性，获取styleable中的字体 skin_typeface 属性
        key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_skin_typeface];
        int textTypefaceResourceId = mSkinAttributes.getResourceId(key);
        Skins.getInstance().setTypeface(this,textTypefaceResourceId);





    }
}
