package com.zp.android.base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zp.android.base.R
import org.jetbrains.anko.*

/**
 * Created by zhaopan on 2018/11/9.
 */

class CustomToastView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var container: LinearLayout
    lateinit var tvPrompt: TextView

    init {
        apply {
            container = this
            gravity = Gravity.CENTER
            layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
            tvPrompt = textView {
                layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent)
                backgroundResource = R.drawable.base_bg_toast_custom
                leftPadding = dip(20)
                rightPadding = dip(20)
                topPadding = dip(24)
                bottomPadding = dip(24)
                textSize = 14f
                textColor = Color.WHITE
            }
        }
    }
}