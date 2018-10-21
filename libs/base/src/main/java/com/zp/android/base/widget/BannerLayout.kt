package com.zp.android.base.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.zp.android.common.R
import com.zp.android.common.banner
import com.zp.android.common.widget.GlideImageLoader
import org.jetbrains.anko.*

/**
 * Created by zhaopan on 2018/10/21.
 */
class BannerLayout(context: Context) : _RelativeLayout(context) {

    lateinit var banner: Banner

    init {
        apply {
            layoutParams = RelativeLayout.LayoutParams(matchParent, wrapContent)
            leftPadding = dip(10)
            rightPadding = dip(10)
            banner = banner {
                setDelayTime(5000)
                setImageLoader(GlideImageLoader())
                setBannerAnimation(Transformer.Default)
                setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
            }.lparams(matchParent, dip(150))
        }
    }

    fun onBannerListener(listener: (position: Int) -> Unit) {
        banner.setOnBannerListener(listener)
    }

}