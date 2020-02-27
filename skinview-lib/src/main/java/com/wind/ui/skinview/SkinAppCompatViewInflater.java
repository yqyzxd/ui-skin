package com.wind.ui.skinview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.wind.ui.skinview.view.SkinnableButton;
import com.wind.ui.skinview.view.SkinnableEditText;
import com.wind.ui.skinview.view.SkinnableFrameLayout;
import com.wind.ui.skinview.view.SkinnableImageView;
import com.wind.ui.skinview.view.SkinnableLinearLayout;
import com.wind.ui.skinview.view.SkinnableRelativeLayout;
import com.wind.ui.skinview.view.SkinnableTextView;

/**
 * Created By wind
 * on 2020-02-25
 *
 * 模仿AppCompatViewInflater 将系统的控件替换成我们自己的控件
 *
 */
public class SkinAppCompatViewInflater {



    public View createSkinView(View parent,Context context, String name, AttributeSet attrs) {
        View view=null;
        switch (name) {
            case "TextView":
                view = createSkinTextView(context, attrs);
                break;
            case "ImageView":
                view = createSkinImageView(context, attrs);
                break;
            case "Button":
                view = createSkinButton(context, attrs);
                break;
            case "EditText":
                view = createSkinEditText(context, attrs);
                break;
            case "Spinner":
               // view = createSpinner(context, attrs);
                break;
            case "ImageButton":
                view = createSkinImageButton(context, attrs);
                break;
            case "CheckBox":
               // view = createCheckBox(context, attrs);
                break;
            case "RadioButton":
               // view = createRadioButton(context, attrs);
                break;
            case "CheckedTextView":
               // view = createCheckedTextView(context, attrs);
                break;
            case "AutoCompleteTextView":
              //  view = createAutoCompleteTextView(context, attrs);
                break;
            case "MultiAutoCompleteTextView":
               // view = createMultiAutoCompleteTextView(context, attrs);
                break;
            case "RatingBar":
               // view = createRatingBar(context, attrs);
                break;
            case "SeekBar":
               // view = createSeekBar(context, attrs);
                break;
            case "ToggleButton":
               // view = createToggleButton(context, attrs);
                break;

            case "LinearLayout":
                view=createSkinLinearLayout(context,attrs);
                break;
            case "RelativeLayout":
                view=createSkinRelativeLayout(context,attrs);
                break;
            case "FrameLayout":
                view=createSkinFrameLayout(context,attrs);
                break;
        }


        return view;
    }

    private View createSkinFrameLayout(Context context, AttributeSet attrs) {
        return new SkinnableFrameLayout(context,attrs);
    }

    private View createSkinRelativeLayout(Context context, AttributeSet attrs) {
        return new SkinnableRelativeLayout(context,attrs);
    }

    private View createSkinImageButton(Context context, AttributeSet attrs) {
        return null;
    }

    private View createSkinEditText(Context context, AttributeSet attrs) {
        return new SkinnableEditText(context,attrs);
    }

    private View createSkinButton(Context context, AttributeSet attrs) {
        return new SkinnableButton(context,attrs);
    }

    private View createSkinImageView(Context context, AttributeSet attrs) {
        return new SkinnableImageView(context,attrs);
    }

    private View createSkinLinearLayout(Context context, AttributeSet attrs) {
        return new SkinnableLinearLayout(context,attrs);
    }

    private View createSkinTextView(Context context, AttributeSet attrs) {

        return new SkinnableTextView(context,attrs);
    }



}
