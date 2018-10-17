package com.zp.android.base.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.ViewTreeObserver
import android.widget.EditText
import timber.log.Timber


/**
 * Created by zhaopan on 2018/5/29.
 */

fun Activity.hideSoftKeyboard() {
    val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    mInputMethodManager.hideSoftInputFromWindow(this.window.decorView.windowToken, 0/* InputMethodManager.HIDE_NOT_ALWAYS*/)
}

fun Context.hideSoftKeyboard() {
    val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val mActivity = this as Activity
    mInputMethodManager.hideSoftInputFromWindow(mActivity.window.decorView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.showSoftKeyboard(view: View) {
    val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.showSoftKeyboard(view: View) {
    val mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val mActivity = this as Activity
    mInputMethodManager.hideSoftInputFromWindow(mActivity.window.decorView.windowToken, InputMethodManager.SHOW_FORCED)
}

//仅当弹出/隐藏时回调一次
fun Activity.listenKeyboard(listener: KeyBoardListener) {
    val kbHandler = KeyboardHandler(this)
    kbHandler.setKeyBoardListener(listener)
}

interface KeyBoardListener {
    /**
     * @param isShow         键盘弹起
     * @param keyboardHeight 键盘高度
     */
    fun onKeyboardChange(isShow: Boolean, keyboardHeight: Int)
}

private class KeyboardHandler internal constructor(activity: Activity) : ViewTreeObserver.OnGlobalLayoutListener {

    private val mContentView: View?
    private var mOriginHeight: Int = 0
    private var mPreHeight: Int = 0
    private var mKeyBoardListener: KeyBoardListener? = null

    init {
        mContentView = activity.findViewById(android.R.id.content)
        mContentView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    internal fun setKeyBoardListener(keyBoardListen: KeyBoardListener) {
        this.mKeyBoardListener = keyBoardListen
    }

    override fun onGlobalLayout() {
        val currHeight = mContentView?.height?:0
        Timber.d("debug", "currHeight: $currHeight")
        if (currHeight == 0) return

        var hasChange = false
        if (mPreHeight == 0) {
            mPreHeight = currHeight
            mOriginHeight = currHeight
            Timber.d("debug", "mPreHeight: $mPreHeight")
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true
                mPreHeight = currHeight
            } else {
                hasChange = false
            }
        }
        if (hasChange) {
            val isShow = mOriginHeight != currHeight
            var keyboardHeight = mOriginHeight - currHeight
            mKeyBoardListener?.onKeyboardChange(isShow, keyboardHeight)
        }
    }
}

//当进入页面后, 仅为etView首先弹出键盘
fun Activity.firstPopupKeyboard(etView: EditText){
    etView.setFocusable(true)
    etView.setFocusableInTouchMode(true)
    etView.requestFocus()
    val act = this
    etView.viewTreeObserver.addOnGlobalLayoutListener(object : KeyBoardChangeOnGlobalLayoutListener(act) {
        override fun onKeyboardChangeOnGlobalLayout(isShow: Boolean, keyboardHeight: Int) {
            if(!isShow){ //如果没有显示键盘, 则一直弹出.
                act.showSoftKeyboard(etView)
            } else { //否则移除监听事件, 不再管理键盘.
                etView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}


private abstract class KeyBoardChangeOnGlobalLayoutListener internal constructor(activity: Activity) : ViewTreeObserver.OnGlobalLayoutListener {

    private val mContentView: View?
    private var mOriginHeight: Int = 0
    private var mPreHeight: Int = 0

    init {
        mContentView = activity.findViewById(android.R.id.content)
        //mContentView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val currHeight = mContentView?.height?:0
        Timber.d("debug", "currHeight: $currHeight")
        if (currHeight == 0) return

        var hasChange = false
        if (mPreHeight == 0) {
            mPreHeight = currHeight
            mOriginHeight = currHeight
            Timber.d("debug", "mPreHeight: $mPreHeight")
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true
                mPreHeight = currHeight
            } else {
                hasChange = false
            }
        }
        val isShow = mOriginHeight != currHeight
        var keyboardHeight = mOriginHeight - currHeight
        onKeyboardChangeOnGlobalLayout(isShow, keyboardHeight)
    }

    abstract fun onKeyboardChangeOnGlobalLayout(isShow: Boolean, keyboardHeight: Int)

}