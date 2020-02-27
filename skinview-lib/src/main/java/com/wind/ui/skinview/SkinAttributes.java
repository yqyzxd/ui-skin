package com.wind.ui.skinview;

import android.content.res.TypedArray;
import android.util.SparseIntArray;

/**
 * Created By wind
 * on 2020-02-25
 */
public class SkinAttributes {


    private SparseIntArray mAttributeMap;
    public SkinAttributes(TypedArray typedArray, int[] styleable){
        mAttributeMap=new SparseIntArray();

        for (int i=0;i<typedArray.length();i++) {
            int index=styleable[i];
            int resourceId=typedArray.getResourceId(i,0);
            mAttributeMap.put(index,resourceId);
        }
    }


    public int getResourceId(int styleable){
        return mAttributeMap.get(styleable);
    }

}
