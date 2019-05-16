package com.zp.android.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView

/**
 * Created by zhaopan on 2018/12/23.
 */

@PublishedApi
internal object `$$Anko$Factories$V7WidgetView` {
    val V7_WIDGET_TOOLBAR = { ctx: Context -> _ToolbarV7(ctx) }
}

inline fun ViewManager.toolbarV7(): Toolbar = toolbarV7() {}
inline fun ViewManager.toolbarV7(init: (@AnkoViewDslMarker _ToolbarV7).() -> Unit): Toolbar {
    return ankoView(`$$Anko$Factories$V7WidgetView`.V7_WIDGET_TOOLBAR, theme = 0) { init() }
}

open class _ToolbarV7(ctx: Context): Toolbar(ctx) {

    inline fun <T: View> T.lparams(
        c: Context?,
        attrs: AttributeSet?,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(c!!, attrs!!)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        c: Context?,
        attrs: AttributeSet?
    ): T {
        val layoutParams = Toolbar.LayoutParams(c!!, attrs!!)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(width, height)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
    ): T {
        val layoutParams = Toolbar.LayoutParams(width, height)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        gravity: Int,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(width, height, gravity)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        gravity: Int
    ): T {
        val layoutParams = Toolbar.LayoutParams(width, height, gravity)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        p: ViewGroup.LayoutParams?,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(p!!)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        p: ViewGroup.LayoutParams?
    ): T {
        val layoutParams = Toolbar.LayoutParams(p!!)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        source: ViewGroup.MarginLayoutParams?,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(source!!)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        source: ViewGroup.MarginLayoutParams?
    ): T {
        val layoutParams = Toolbar.LayoutParams(source!!)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        source: LinearLayout.LayoutParams?,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(source!!)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        source: LinearLayout.LayoutParams?
    ): T {
        val layoutParams = Toolbar.LayoutParams(source!!)
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        source: Toolbar.LayoutParams?,
        init: Toolbar.LayoutParams.() -> Unit
    ): T {
        val layoutParams = Toolbar.LayoutParams(source!!)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }

    inline fun <T: View> T.lparams(
        source: Toolbar.LayoutParams?
    ): T {
        val layoutParams = Toolbar.LayoutParams(source!!)
        this@lparams.layoutParams = layoutParams
        return this
    }

}
