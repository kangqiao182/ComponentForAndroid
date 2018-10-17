package com.zp.android.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by zhaopan on 2018/4/23.
 */
public class LinkedHorizontalScrollView extends HorizontalScrollView {
    private LinkScrollChangeListener listener;

    public LinkedHorizontalScrollView(Context context) {
        super(context);
    }

    public LinkedHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkedHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollChangeListener(LinkScrollChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener)
            listener.onscroll(this, l, t, oldl, oldt);
    }

    /**
     * 控制滑动速度
     */
    @Override
    public void fling(int velocityY) {
        //super.fling(velocityY / 2);
        super.fling(velocityY);
    }

    public interface LinkScrollChangeListener {
        void onscroll(LinkedHorizontalScrollView view, int l, int t, int oldl, int oldt);
    }
}