package com.zp.android.common

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUrl(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }


    @JvmStatic
    @BindingAdapter("android:drawableRes")
    fun setDrawableRes(view: ImageView, @DrawableRes drawRes: Int) {
        //Glide.with(view.context).load(drawRes).into(view)
        view.setImageResource(drawRes)
    }
}
