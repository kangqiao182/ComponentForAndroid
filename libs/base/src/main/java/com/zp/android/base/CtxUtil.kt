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

    fun context(): Context = BaseApp.application

    fun getString(id: Int): String = context().getString(id)

    fun getString(id: Int, vararg args: String) = context().getString(id, *args)

    fun getDrawable(id: Int): Drawable? = ContextCompat.getDrawable(context(), id)

    fun getColor(id: Int) = ContextCompat.getColor(context(), id)

    fun showTaost(content: String) {
        CustomToast(context(), content).show()
    }

    fun showTaost(strResId: Int) {
        CustomToast(context(), getString(strResId)).show()
    }

}