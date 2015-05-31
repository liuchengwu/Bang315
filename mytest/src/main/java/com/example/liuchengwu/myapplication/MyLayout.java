package com.example.liuchengwu.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by liuchengwu on 2015/5/30.
 */
public class MyLayout extends LinearLayout {


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
