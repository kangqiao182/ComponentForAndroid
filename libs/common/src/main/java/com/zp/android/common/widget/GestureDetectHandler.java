package com.zp.android.common.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by zhaopan on 16/9/24.
 * e-mail: kangqiao610@gmail.com
 * 手势识别转换处理类
 */
public class GestureDetectHandler extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private static final String TAG = "GestureDetectHandler";
    public static final boolean DEBUG = true;

    //最小缩放距离, 单位:像素
    public static final int MIN_SCALE_DISTANCE = 20;
    //最短缩放时间, 单位:毫秒
    public static final int MIN_SCALE_TIME_MILLI = 10;
    //最小移动距离
    public static final int MIN_MOVE_DISTANCE = 5;

    //X轴的坐标位移大于FLING_MIN_DISTANCE
    public static final int FLING_MIN_DISTANCE = 100;
    //移动速度大于FLING_MIN_VELOCITY个像素/秒
    public static final int FLING_MIN_VELOCITY = 150;

    public static final long CUSTOM_LONG_PRESS_TIMEOUT = 300;
    public static final long CUSTOM_SCALE_BEGIN_TIMEOUT = 0;
    public static final long CUSTOM_DELAYED_CANCEL_TIMEOUT_AFTER_LONG_PRESS = 100;

    private static final int LONG_PRESS = 1;
    private static final int SCALE_BEGIN = 2;
    private static final int DELAYED_CANCEL = 3;

    private long mLongPressTimeout = CUSTOM_LONG_PRESS_TIMEOUT;
    private long mScaleBeginTimeout = CUSTOM_SCALE_BEGIN_TIMEOUT;
    private long mDelayedCancelTimeoutAfterLongPress = CUSTOM_DELAYED_CANCEL_TIMEOUT_AFTER_LONG_PRESS;
    private boolean mInLongPressProgress = false;
    private boolean mInMovingProgress = false;
    private boolean mInScaleProgress = false;
    private boolean mInDoubleTapProgress = false;
    private boolean mIsCanceled = true;
    private boolean mIsCustomLongPressEnabled = true;

    private float lastSpan;
    private MotionEvent showPressEvent;
    private View mDetectedView;
    private Handler mHandler;
    private GestureOperateListener mGestureOptListener;
    private GestureDetector mGestureDetector;//单击和双击事件手势识别
    private ScaleGestureDetector mScaleGestureDetector;//缩放事件手势识别

    public GestureDetectHandler(View detectedView, GestureOperateListener operator) {
        assert detectedView != null : Log.e(TAG, "detectedView is null in constructor!");
        assert operator != null : Log.e(TAG, "GestureOperateListener is null in constructor!");

        mDetectedView = detectedView;
        detectedView.setOnTouchListener(this);
        mHandler = new GestureHandler(detectedView.getContext().getMainLooper());
        mGestureOptListener = operator;

        mGestureDetector = new GestureDetector(mDetectedView.getContext(), this);
        mScaleGestureDetector = new ScaleGestureDetector(mDetectedView.getContext(), this);

        setIsCustomLongPressEnabled(true); //默认使用自定义的长按操作
    }

    private class GestureHandler extends Handler {
        GestureHandler() {
            super();
        }

        GestureHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LONG_PRESS:
                    dispatchLongPress();
                    break;
                case SCALE_BEGIN:
                    dispatchScaleBegin();
                    break;
                case DELAYED_CANCEL:
                    dispatchCancel();
                    break;
                default:
                    throw new RuntimeException("Unknown message " + msg); //never
            }
        }
    }

    /**
     * OnTouchListener 仅仅作为补充操作,
     * 防止长按后滑动和长按滑动后onFling, 这两个操作后续没有明确的结束回调, 即Up或Cancel时GestureDetector并不回调操作.
     * 不建议使用, 如是mDetectedView有去设置OnTouchListener则会覆盖此实现. 即会导致此中功能失效.
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    @Deprecated
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            loge("onTouch", actionToStr(action));
            //当mDetectedView的onTouchEvent事件突然交到父控件处理时, 紧急执行清理操作.
            dispatchCancel();
        }
        else if(action == MotionEvent.ACTION_UP){
            //长按后的滑动操作, 执行延时清理操作
            if(mIsCustomLongPressEnabled && mInLongPressProgress){
                sendDelayedCancelMessage();
            }
        }
        //改由GestureDetector的每一步单独处理, 减少重复的绘制操作.
        //mGestureOptListener.postInvalidate();
        return false;
    }

    /**
     * 接收系统的onTouchEvent事件, 并返回true表示处理, 不再传递
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        //return super.onTouchEvent(event);//不管返回值是什么，都能接收down事件，都能触发onDown、onShowPress、onLongPress
        return true;//但只有返回true才能继续接收move,up等事件，也才能响应ScaleGestureDetector事件及GestureDetector中与move,up相关的事件
    }

    private void dispatchLongPress() {
        mDetectedView.getParent().requestDisallowInterceptTouchEvent(true);
        mInLongPressProgress = true;
        onLongPress(showPressEvent);
    }

    private void dispatchMoving() {
        mDetectedView.getParent().requestDisallowInterceptTouchEvent(true);
        mInMovingProgress = true;
    }

    private void dispatchScaleBegin() {
        mDetectedView.getParent().requestDisallowInterceptTouchEvent(true);
        mInScaleProgress = true;
        if(mInLongPressProgress) {
            mInLongPressProgress = false;
            mHandler.removeMessages(LONG_PRESS);
            mGestureOptListener.onUpOrCancel();
            mGestureOptListener.postInvalidate();
        }
    }

    //ACTION_CANCEL and ACTION_UP
    private void dispatchCancel() {
        loge("dispatchCancel", "dispatchCancel");
        if (!mIsCanceled) {
            mIsCanceled = true;
            mDetectedView.getParent().requestDisallowInterceptTouchEvent(false);
            mHandler.removeMessages(DELAYED_CANCEL);
            mHandler.removeMessages(LONG_PRESS);
            mHandler.removeMessages(SCALE_BEGIN);
            mInDoubleTapProgress = false;
            mInLongPressProgress = false;
            mInMovingProgress = false;
            mInScaleProgress = false;
            mGestureOptListener.onUpOrCancel();
        }
        mGestureOptListener.postInvalidate();
    }

    /**
     * 设置是否使用自己的长按处理.
     *
     * @param isCustomLongPressEnabled
     */
    public void setIsCustomLongPressEnabled(boolean isCustomLongPressEnabled) {
        if (isCustomLongPressEnabled) {
            //如果自定义自己的长按处理, 先禁用系统的长按触发.
            mGestureDetector.setIsLongpressEnabled(false);
        } else {
            //如果不自定义自己的长按处理, 默认启用系统的长按
            mGestureDetector.setIsLongpressEnabled(true);
        }
        //mGestureDetector.setIsLongpressEnabled(!isCustomLongPressEnabled);
        mIsCustomLongPressEnabled = isCustomLongPressEnabled;
    }

    /**
     * 设置是否使用系统的长按处理.
     *
     * @param isSystemLongPressEnabled
     */
    public void setIsLongpressEnabled(boolean isSystemLongPressEnabled) {
        mGestureDetector.setIsLongpressEnabled(isSystemLongPressEnabled);
        mIsCustomLongPressEnabled = false;
    }

    public void setScaleBeginTimeout(long scaleBeginTimeout) {
        if (scaleBeginTimeout >= 0) this.mScaleBeginTimeout = scaleBeginTimeout;
    }

    public void setLongPressTimeout(long longPressTimeout) {
        if (longPressTimeout >= 0) this.mLongPressTimeout = longPressTimeout;
    }

    /**
     * 设置长按后的处理操作 需要延时多长时间执行清理操作. 默认大于等于100毫秒. 且会自动开启自定义的长按操作处理.
     *
     * @param cancelTimeout
     */
    public void setDelayedCancelTimeoutAfterLongPress(long cancelTimeout) {
        if (cancelTimeout >= CUSTOM_DELAYED_CANCEL_TIMEOUT_AFTER_LONG_PRESS) {
            setIsCustomLongPressEnabled(true);
            this.mDelayedCancelTimeoutAfterLongPress = cancelTimeout;
        }
    }

    private void sendDelayedCancelMessage() {
        mHandler.removeMessages(DELAYED_CANCEL);
        mHandler.sendEmptyMessageDelayed(DELAYED_CANCEL, mDelayedCancelTimeoutAfterLongPress);
    }

    public GestureDetector getGestureDetector() {
        return mGestureDetector;
    }

    public ScaleGestureDetector getScaleGestureDetector() {
        return mScaleGestureDetector;
    }

    /**
     * 注意：
     * 1. onSingleTapConfirmed（单击）和onSingleTapUp都是在down后既没有滑动onScroll，又没有长按onLongPress时， up 时触发的
     * 2. 非常快的点击一下：onDown->onSingleTapUp->onSingleTapConfirmed
     * 3. 稍微慢点的点击一下：onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed（最后一个不一定会触发）
     */
    ////////OnGestureListener/////////////////////////////

    /**
     * Touch down时触发
     * 按下（onDown）： 刚刚手指接触到触摸屏的那一刹那，就是触的那一下
     *
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e) {
        loge("view-手势", "onDown event.getAction=" + actionToStr(e.getAction()) + ", event.getX()=" + e.getX() + ", event.getY()=" + e.getY());
        mInLongPressProgress = false;
        mIsCanceled = false;
        return super.onDown(e);
    }

    /**
     * onScroll一点距离后，【抛掷时】触发（若是轻轻的、慢慢的停止活动，而非抛掷，则很可能不触发）
     * 参数为手指接触屏幕、离开屏幕一瞬间的动作事件，及手指水平、垂直方向移动的速度，像素/秒
     * 抛掷（onFling）： 手指在触摸屏上迅速移动，并松开的动作，onDown -> onScroll ... -> onFling
     * 长按后滚动（onScroll）： 手指在触摸屏上滑动，onDown -> onShowPress -> onLongPress -> onScroll ... [onFling]
     *
     * @param downEvent
     * @param event
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent event, float velocityX, float velocityY) {
        loge("view-手势", "onFling newAction=" + actionToStr(event.getAction()) + ", downEvent.getRawX()=" + downEvent.getRawX() + ", event.getRawX()=" + event.getRawX() + ", velocityX=" + velocityX + ", velocityY=" + velocityY);
        mGestureOptListener.onFling(downEvent, event, velocityX, velocityY);
        mGestureOptListener.postInvalidate();
        if (mIsCustomLongPressEnabled && mInLongPressProgress) {
            //sendDelayedCancelMessage(); //长按后滑动, 最后onFling了一下, 执行延时清理操作.
        } else {
            dispatchCancel(); //正常的onFling, 直接执行清理操作.
        }
        /*if ((event.getRawX() - downEvent.getRawX()) > FLING_MIN_DISTANCE *//*&& Math.abs(velocityX) > FLING_MIN_VELOCITY*//*) {
            loge("view-手势", "onFling-从左往右滑");
            return true;
        } else if (downEvent.getRawX() - event.getRawX() > FLING_MIN_DISTANCE *//*&& Math.abs(velocityX) > FLING_MIN_VELOCITY*//*) {
            loge("view-手势", "onFling-从右往左滑");
            return true;
        }*/
        return super.onFling(downEvent, event, velocityX, velocityY);
    }

    /**
     * Touch了滑动时触发，e1代表触摸时的事件，是不变的，e2代表滑动过程中的事件，是时刻变化的
     * distance是当前event2与上次回调时的event2之间的距离，代表上次回调之后到这次回调之前移动的距离
     * 缩放(onScroll) onDown -> onScaleBegin -> (onScroll ... -> onScale) ... -> onScaleEnd
     * 长按后滚动（onScroll）： 手指在触摸屏上滑动，onDown -> onShowPress -> onLongPress -> onScroll ... [onFling]
     * 滚动（onScroll）： 手指在触摸屏上滑动，onDown -> onScroll ...
     *
     * @param downEvent
     * @param event
     * @param distanceX
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent downEvent, MotionEvent event, float distanceX, float distanceY) {
        if (mInScaleProgress) {
            // TODO: 16/9/27 缩放中的同步滑动
            loge("view-手势", "onScroll 缩放中>>> " + actionToStr(event.getAction()) + ", -X-" + (int) downEvent.getX() + "/" + (int) event.getX() + "/" + (int) distanceX
                    + ", >>> -Y-" + (int) downEvent.getY() + "/" + (int) event.getY() + "/" + (int) distanceY);
        } else if (mInLongPressProgress) { //长按后的滑动.
            loge("view-手势", "onScroll 长按中>>> " + actionToStr(event.getAction()) + ", -X-" + (int) downEvent.getX() + "/" + (int) event.getX() + "/" + (int) distanceX
                    + ", >>> -Y-" + (int) downEvent.getY() + "/" + (int) event.getY() + "/" + (int) distanceY);
            mGestureOptListener.onMoveAfterLongPress(event);
            mGestureOptListener.postInvalidate(); //后续仍有移动, 执行刷新UI操作
            if (mIsCustomLongPressEnabled) {
                //sendDelayedCancelMessage(); //长按后的滑动操作, 执行延时清理操作
            } else {
                // TODO: 16/9/27 注: 没有自定义长按操作, 如果没有后续操作是不会有Up或Cancel动作, 即没有执行清理操作的机会了.
            }
        } else { //普通滑动
            if(!mInMovingProgress){
                dispatchMoving();
            }
            loge("view-手势", "onScroll 滑动中>>> " + actionToStr(event.getAction()) + ", -X-" + (int) downEvent.getX() + "/" + (int) event.getX() + "/" + (int) distanceX
                    + ", >>> -Y-" + (int) downEvent.getY() + "/" + (int) event.getY() + "/" + (int) distanceY);
            mGestureOptListener.onMove(downEvent, event, distanceX, distanceY);
            mGestureOptListener.postInvalidate(); //后续仍有移动, 执行刷新UI操作
            // TODO: 16/9/27 注: 普通的滑动操作, 如果没有后续操作是不会有Up或Cancel动作, 即没有执行清理操作的机会了.
        }
        return super.onScroll(downEvent, event, distanceX, distanceY);
    }

    /**
     * ouch了不移动一直Touch down时触发
     * 长按（onLongPress）： 手指按在持续一段时间，并且没有松开
     * 1. onDown -> onShowPress -> onLongPress -> onSingleTapUp -> onSingleTapConfirmed
     * 2. onDown -> onShowPress -> onLongPress -> onScroll ...
     * 3. onDown -> onShowPress -> onLongPress -> onScroll ... -> onFling
     *
     * @param event
     */
    @Override
    public void onLongPress(MotionEvent event) {
        loge("view-手势", "onLongPress event.getAction=" + actionToStr(event.getAction()) + ", event.getX()=" + event.getX() + ", event.getY()=" + event.getY());
        mGestureOptListener.onLongPress(event);
        mGestureOptListener.postInvalidate();
        super.onLongPress(event);
    }

    /**
     * Touch了还没有滑动时触发
     * 按住（onShowPress）： 手指按在触摸屏上，在按下起效，在长按前失效，
     * onDown -> onShowPress -> onLongPress
     *
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {
        loge("view-手势", "onShowPress event.getAction=" + actionToStr(e.getAction()) + ",  event.getX()=" + e.getX() + ", event.getY()=" + e.getY());
        if (mIsCustomLongPressEnabled && !mInDoubleTapProgress) { //如果自定义长按启用, 并且不是在双击的第二下长按的, 即执行长按操作.
            mHandler.sendEmptyMessageAtTime(LONG_PRESS, e.getDownTime() + mLongPressTimeout);
            showPressEvent = e;
        }
        super.onShowPress(e);
    }

    /**
     * 在touch down后又没有滑动（onScroll），又没有长按（onLongPress），然后Touchup时触发。
     * 抬起（onSingleTapUp）：手指离开触摸屏的那一刹那
     *
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        loge("view-手势", "onSingleTapUp event.getAction=" + actionToStr(e.getAction()) + ", event.getX()=" + e.getX() + ", event.getY()=" + e.getY());
        return super.onSingleTapUp(e);
    }

    ////////OnDoubleTapListener/////////////////////////////

    /**
     * 在touch down后又没有滑动（onScroll），又没有长按（onLongPress），然后Touchup时触发。
     * 单击确认，即很快的按下并抬起，但并不连续点击第二下
     *
     * @param event
     * @return
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        loge("view-手势", "onSingleTapConfirmed event.getAction=" + actionToStr(event.getAction()) + ", event.getX()=" + event.getX() + ", event.getY()=" + event.getY());
        if (mInLongPressProgress) {
            //长按进行中... 如果设置了自定义长按操作, 执行延时清理操作, 否则直接清理.
            if (mIsCustomLongPressEnabled) {
                sendDelayedCancelMessage();
            } else {
                dispatchCancel();
            }
        } else {
            //执行单击操作.
            mGestureOptListener.onClick(event);
            dispatchCancel(); //后续没有操作并已经Up或Cancel了, 所以执行清理操作.
        }

        return super.onSingleTapConfirmed(event);
    }

    /**
     * 双击的【第二下】Touch down时触发（只执行一次）
     *
     * @param e
     * @return
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        loge("view-手势", "onDoubleTap event.getAction=" + actionToStr(e.getAction()) + ", event.getX()=" + e.getX() + ", event.getY()=" + e.getY());
        mInDoubleTapProgress = true;
        return super.onDoubleTap(e);
    }

    /**
     * 双击的【第二下】Touch down和up都会触发（执行次数不确定）。
     *
     * @param event
     * @return
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        loge("view-手势", "onDoubleTapEvent event.getAction=" + actionToStr(event.getAction()) + ", event.getX()=" + event.getX() + ", event.getY()=" + event.getY());
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            mGestureOptListener.onDoubleClick(event);
            dispatchCancel(); //双击操作完成并Up或Cancel, 所以执行清理操作
        }
        return super.onDoubleTapEvent(event);
    }

    ////////OnContextClickListener/////////////////////////////
    @Override
    public boolean onContextClick(MotionEvent e) {
        loge("view-手势", "onContextClick event.getX()=" + e.getX() + ", event.getY()=" + e.getY());
        return super.onContextClick(e);
    }

    ////////OnScaleGestureListener/////////////////////////////
    /**
     * http://www.cnblogs.com/baiqiantao/p/5630506.html
     * public float getCurrentSpan () 返回手势过程中，组成该手势的两个触点的当前距离。
     * public long getEventTime () 返回事件被捕捉时的时间。
     * public float getFocusX () 返回当前手势焦点的 X 坐标。 如果手势正在进行中，焦点位于组成手势的两个触点之间。 如果手势正在结束，焦点为仍留在屏幕上的触点的位置。若 isInProgress() 返回 false，该方法的返回值未定义。
     * public float getFocusY ()  返回当前手势焦点的 Y 坐标。
     * public float getPreviousSpan () 返回手势过程中，组成该手势的两个触点的前一次距离。
     * public float getScaleFactor () 返回从前一个伸缩事件至当前伸缩事件的伸缩比率。该值定义为  getCurrentSpan() / getPreviousSpan()。
     * public long getTimeDelta () 返回前一次接收到的伸缩事件距当前伸缩事件的时间差，以毫秒为单位。
     * public boolean isInProgress () 如果手势处于进行过程中，返回 true。否则返回 false。
     */

    /**
     * 开始缩放
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (mInScaleProgress) { //缩放中...
            float distance = detector.getCurrentSpan() - lastSpan;
            if (Math.abs(distance) > MIN_SCALE_DISTANCE) {
                lastSpan = detector.getCurrentSpan();
                if (detector.getScaleFactor() < 1) { //缩小
                    Log.e("view-缩放", "onScale，缩小" + detector.getScaleFactor() + ", curSpan=" + detector.getCurrentSpan() + ", preSpan=" + detector.getPreviousSpan() + ", curSpan-preSpan=>" + distance + ", timeDelta=" + detector.getTimeDelta());
                } else { //放大
                    Log.e("view-缩放", "onScale，放大" + detector.getScaleFactor() + ", curSpan=" + detector.getCurrentSpan() + ", preSpan=" + detector.getPreviousSpan() + ",  curSpan-preSpan=>" + distance + ", timeDelta=" + detector.getTimeDelta());
                }
                mGestureOptListener.onZoom(detector);
                mGestureOptListener.postInvalidate(); //并没Up或Cancel且后续仍在Zoom, 执行刷新UI操作
            }
            return true;
        }
        return false;
    }

    /**
     * 缩放开始, 一次缩放仅执行一次
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        loge("view-缩放", "onScaleBegin");
        mHandler.sendEmptyMessageAtTime(SCALE_BEGIN, detector.getEventTime() + mScaleBeginTimeout);
        return true;
    }

    /**
     * 缩放结束, 一次缩放仅执行一次
     *
     * @param detector
     */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        loge("view-缩放", "onScaleEnd");
        dispatchCancel(); //已经停止缩放操作并Up或Cancel, 执行清理操作.
    }

    /**
     * @see ScaleGestureDetector#getCurrentSpanX()
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCurrentSpanX(ScaleGestureDetector scaleGestureDetector) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return scaleGestureDetector.getCurrentSpanX();
        } else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }

    /**
     * @see ScaleGestureDetector#getCurrentSpanY()
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCurrentSpanY(ScaleGestureDetector scaleGestureDetector) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return scaleGestureDetector.getCurrentSpanY();
        } else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }

    private static void loge(String TAG, String msg) {
        if (DEBUG) Log.e("zp>>>" + TAG, msg);
    }

    private static String actionToStr(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "DOWN_" + action;
            case MotionEvent.ACTION_UP:
                return "UP_" + action;
            case MotionEvent.ACTION_MOVE:
                return "MOVE_" + action;
            case MotionEvent.ACTION_CANCEL:
                return "CANCEL_" + action;
        }
        return "<" + action + ">";
    }

    /**
     * Created by zhaopan on 16/9/22.
     * e-mail: kangqiao610@gmail.com
     */

    public interface GestureOperateListener {

        /**
         * 只要有 ACTION_UP 或 ACTION_CANCEL
         */
        void onUpOrCancel();

        /**
         * 缩放
         * if true zoom in, else zoom out
         *
         * @param detector
         */
        void onZoom(ScaleGestureDetector detector);

        /**
         * 滑动, 注意: 之前没有进行长按.
         *
         * @param downEvent
         * @param event
         * @param distanceX
         * @param distanceY
         */
        void onMove(MotionEvent downEvent, MotionEvent event, float distanceX, float distanceY);

        /**
         * 长按后滑动, 回调多次
         *
         * @param event
         */
        void onMoveAfterLongPress(MotionEvent event);

        /**
         * 长按 仅回调一次
         *
         * @param event
         */
        void onLongPress(MotionEvent event);

        /**
         * 快速点击. 第一次 touch up时, 后续又没有任何操作(滑动, 不处理长按中, 缩放)时回调
         *
         * @param event
         */
        void onClick(MotionEvent event);

        /**
         * 双击. 第二次 touch up时, 回调
         *
         * @param event
         */
        void onDoubleClick(MotionEvent event);

        /**
         * 快速滑动后的最后一个动作
         *
         * @param downEvent 起点
         * @param event     终点
         * @param velocityX 水平方向移动的速度，像素/秒
         * @param velocityY 垂直方向移动的速度，像素/秒
         */
        void onFling(MotionEvent downEvent, MotionEvent event, float velocityX, float velocityY);

        /**
         * 重绘UI
         */
        void postInvalidate();
    }

    public static class SimpleGestureOperateListener implements GestureOperateListener{

        @Override
        public void onUpOrCancel() {

        }

        @Override
        public void onZoom(ScaleGestureDetector detector) {

        }

        @Override
        public void onMove(MotionEvent downEvent, MotionEvent event, float distanceX, float distanceY) {

        }

        @Override
        public void onMoveAfterLongPress(MotionEvent event) {

        }

        @Override
        public void onLongPress(MotionEvent event) {

        }

        @Override
        public void onClick(MotionEvent event) {

        }

        @Override
        public void onDoubleClick(MotionEvent event) {

        }

        @Override
        public void onFling(MotionEvent downEvent, MotionEvent event, float velocityX, float velocityY) {

        }

        @Override
        public void postInvalidate() {

        }
    }
}