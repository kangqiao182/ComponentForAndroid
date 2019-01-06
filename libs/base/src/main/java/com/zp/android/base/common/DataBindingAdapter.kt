package com.zp.android.base.common

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zp.android.base.utils.ImageLoaderUtil
import com.zp.android.common.GlideApp

object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUrl(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
        /*GlideApp.with(view.context)
            .load(url)
            .into(view)*/
        //ImageLoaderUtil.roundedCorners(view, url)
    }


    @JvmStatic
    @BindingAdapter("android:drawableRes")
    fun setDrawableRes(view: ImageView, @DrawableRes drawRes: Int) {
        //Glide.with(view.context).load(drawRes).into(view)
        view.setImageResource(drawRes)
    }
}
