package com.zp.android.base

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.zp.android.base.widget.CustomToast

/**
 * Created by zhaopan on 2018/5/12.
 */
object CtxUtil {

    @JvmStatic
    val context: Context by lazy { BaseApp.application }

    @Deprecated("建议使用context")
    @JvmStatic
    fun context(): Context = context

    @JvmStatic
    fun getString(id: Int): String = context.getString(id)

    @JvmStatic
    fun getString(id: Int, vararg args: String) = context.getString(id, *args)

    @JvmStatic
    fun getDrawable(id: Int): Drawable? = ContextCompat.getDrawable(context, id)

    @JvmStatic
    fun getColor(id: Int) = ContextCompat.getColor(context, id)

    @JvmStatic
    fun showToast(content: String) {
        CustomToast(context, content).show()
    }

    @JvmStatic
    fun showToast(strResId: Int) {
        CustomToast(context, getString(strResId)).show()
    }

}