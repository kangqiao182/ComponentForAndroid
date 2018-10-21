package com.zp.android.common.widget

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 * Created by zhaopan on 2018/10/21.
 */

class GlideImageLoader: ImageLoader(){

    interface GetImageUrl {
        fun getImageUrl(): String
    }

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        var imageUrl = path
        if (path is GetImageUrl) {
            imageUrl = (path as GetImageUrl).getImageUrl()
        }
        //Glide 加载图片简单用法
        Glide.with(context).load(imageUrl).into(imageView)
    }
}