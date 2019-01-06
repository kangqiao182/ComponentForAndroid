package com.zp.android.base.utils

import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.zp.android.common.GlideApp
import jp.wasabeef.glide.transformations.BlurTransformation
import com.bumptech.glide.load.MultiTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by zhaopan on 2019/1/6.
 * https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Glide-Library
 */

object ImageLoaderUtil {

    fun roundedCorners(view: ImageView, url: Any, radius: Int = 5, margin: Int = 0) {
        GlideApp.with(view)
            .load(url)
            .transform(RoundedCornersTransformation(radius, margin))
            .into(view)
    }

    fun crop(view: ImageView, url: Any) {
        GlideApp.with(view)
            .load(url)
            .transform(CircleCrop())
            .into(view)
    }

    fun blur(view: ImageView, url: Any) {
        GlideApp.with(view)
            .load(url)
            .transform(BlurTransformation())
            .into(view)
    }

    fun cropBlur(view: ImageView, url: Any) {
        GlideApp.with(view)
            .load(url)
            .transform(MultiTransformation(BlurTransformation(25), CircleCrop()))
            .into(view)
    }
}