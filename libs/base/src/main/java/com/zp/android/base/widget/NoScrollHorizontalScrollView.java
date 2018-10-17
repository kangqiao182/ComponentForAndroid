package com.zp.android.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by zhaopan on 2018/4/23.
 */
public class NoScrollHorizontalScrollView extends HorizontalScrollView {
    public NoScrollHorizontalScrollView(Context context) {
        super(context);
    }

    public NoScrollHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}