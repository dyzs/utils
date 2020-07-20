package com.dyzs.common.examples;

import android.util.Log;

import com.dyzs.common.component.TestAnnotation;


public class AnnotationExplainerImpl implements IAnnotationExplainer {
    private static final String TAG = "MyAnnotation";
    @Override
    public void valueOfGender(String gender) {
        Log.i(TAG, "value of gender:" + gender);
    }
}
