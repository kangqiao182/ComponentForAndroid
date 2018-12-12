package com.zp.android.base.widget

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zp.android.base.R
import com.zp.android.common.attrDimen
import com.zp.android.common.gone
import com.zp.android.common.toolbarV7
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design.themedAppBarLayout

/**
 * Created by zhaopan on 2018/12/10.
 */

class AKAppToolBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var appBarLayout: AppBarLayout
    lateinit var toolbar: Toolbar
    lateinit var tvTitle: TextView

    init {
        appBarLayout = themedAppBarLayout(R.style.base_AppTheme_AppBarOverlay) {
            layoutParams = RelativeLayout.LayoutParams(matchParent, wrapContent)
            id = View.generateViewId()
            fitsSystemWindows = true
            elevation = dip(0).toFloat()

            toolbar = toolbarV7 {
                id = View.generateViewId()
                fitsSystemWindows = true
                popupTheme = R.style.base_AppTheme_PopupOverlay

                tvTitle = textView {
                    ellipsize = TextUtils.TruncateAt.MARQUEE
                    singleLine = true
                    textColorResource = R.color.white
                    textSize = 18f
                    gone()
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, attrDimen(android.R.attr.actionBarSize)) {
                scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }
        }
    }

}

inline fun ViewManager.akAppToolBarLayout(theme: Int = 0) = akAppToolBarLayout(theme, {})
inline fun ViewManager.akAppToolBarLayout(theme: Int = 0, init: AKAppToolBarLayout.() -> Unit): AKAppToolBarLayout {
    return ankoView({ AKAppToolBarLayout(it) }, theme, init)
}

 