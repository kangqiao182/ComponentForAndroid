package com.zp.android.base.common

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.zp.android.base.utils.ImageLoaderUtil

object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:url")
    fun setImageUrl(view: ImageView, url: String) {
        Glide.with(view).load(url).into(view)
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
