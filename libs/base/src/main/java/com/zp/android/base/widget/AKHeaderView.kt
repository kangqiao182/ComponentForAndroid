package com.zp.android.base.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewManager
import android.widget.*
import com.zp.android.base.R
import com.zp.android.common.gone
import com.zp.android.common.setVisible
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

/**
 * Created by zhaopan on 2018/7/18.
 */

class AKHeaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var imgBack: ImageButton
    lateinit var tvLeft: TextView
    lateinit var tvTitle: TextView
    lateinit var tvSubTitle: TextView
    lateinit var tvRight: TextView
    lateinit var vBottomLine: View
    val headerHeight = dimen(R.dimen.base_header_height)
    var leftColor = ContextCompat.getColor(context, R.color.base_text_title)
        set(value) {
            field = value
            tvLeft.setTextColor(value)
        }
    var titleColor = ContextCompat.getColor(context, R.color.base_text_title)
        set(value) {
            field = value
            tvTitle.setTextColor(value)
        }
    var subTitleColor = ContextCompat.getColor(context, R.color.base_text_primary)
        set(value) {
            field = value
            tvSubTitle.setTextColor(value)
        }
    var rightColor = ContextCompat.getColor(context, R.color.base_text_primary)
        set(value) {
            field = value
            tvRight.setTextColor(value)
        }

    private val customStyle = { v: Any ->
        when (v) {
            is TextView -> {
                v.textSize = 16f
                v.textColor = R.color.base_text_title
            }
        //is EditText -> v.textSize = 24f
        }
    }

    init {
        apply {
            relativeLayout {
                backgroundColorResource = R.color.base_bg_white
                layoutParams = RelativeLayout.LayoutParams(matchParent, headerHeight)

                imgBack = imageButton(R.mipmap.base_ic_back) {
                    id = View.generateViewId()
                    backgroundResource = R.drawable.base_selector_btn_white
                }.lparams(headerHeight, headerHeight) {
                    alignParentLeft()
                }

                tvLeft = textView {
                    textSize = 16f
                    gravity = CENTER
                    textColor = leftColor
                    //padding = dip(5)
                    //compoundDrawablePadding = dip(5)
                    //setCompoundDrawablesWithIntrinsicBounds(R.mipmap.base_ic_back, 0, 0, 0)
                    backgroundResource = R.drawable.base_selector_btn_white
                }.lparams(wrapContent, matchParent) {
                    rightOf(imgBack)
                    centerVertically()
                }

                verticalLayout {
                    gravity = Gravity.CENTER
                    tvTitle = textView {
                        textSize = 18f
                        textColor = titleColor
                    }.lparams(wrapContent, wrapContent)

                    tvSubTitle = textView {
                        gone()
                        textSize = 14f
                        textColor = subTitleColor
                    }.lparams(wrapContent, wrapContent)
                }.lparams(wrapContent, matchParent) {
                    centerInParent()
                }

                tvRight = textView {
                    textSize = 16f
                    textColor = rightColor
                    gravity = CENTER
                    leftPadding = dip(15)
                    rightPadding = dip(15)
                }.lparams(wrapContent, matchParent) {
                    alignParentRight()
                    centerVertically()
                }

                vBottomLine = view {
                    backgroundColor = bottomLineColor
                }.lparams(matchParent, dimen(R.dimen.base_divider_line_height)) {
                    alignParentBottom()
                }
            }
        }
        //.applyRecursively(customStyle)
        //AnkoContext.createDelegate(this.relativeLayout{})
    }

    var left: CharSequence
        get() = tvLeft.text
        set(value) {
            tvLeft.setText(value)
        }
    var title: CharSequence
        get() = tvTitle.text
        set(value) {
            tvTitle.setText(value)
        }
    var subTitle: CharSequence
        get() = tvSubTitle.text
        set(value) {
            tvSubTitle.setVisible(value.isNotEmpty())
            tvSubTitle.setText(value)
        }
    var right: CharSequence
        get() = tvRight.text
        set(value) {
            tvRight.setText(value)
        }

    fun setLeftResource(value: Int){
        left = context.getString(value)
    }

    fun setTitleResource(value: Int) {
        title = context.getString(value)
    }

    fun setSubTitleResource(value: Int) {
        subTitle = context.getString(value)
    }

    fun setRightResource(value: Int){
        right = context.getString(value)
    }

    var bottomLineColor: Int = ContextCompat.getColor(context, R.color.base_line)
        set(value) {
            vBottomLine.backgroundColor = value
        }

    fun setBackBackgroundResource(value: Int) {
        imgBack.backgroundResource = value
    }

    fun setLeftBackgroundResource(value: Int) {
        tvLeft.backgroundResource = value
    }

    fun showDivier(isShow: Boolean) {
        vBottomLine.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun onBackPress(listener: (v: android.view.View) -> Unit){
        imgBack.setOnClickListener{ listener(imgBack) }
    }

    fun onLeftClick(listener: (v: android.view.View) -> Unit){
        tvLeft.setOnClickListener{ listener(tvLeft) }
    }

    fun onTitleClick(listener: (v: android.view.View) -> Unit){
        tvTitle.setOnClickListener{ listener(tvTitle) }
    }

    fun onRightClick(listener: (v: android.view.View) -> Unit){
        tvRight.setOnClickListener{ listener(tvRight) }
    }

}

inline fun ViewManager.akHeaderView(theme: Int = 0) = akHeaderView(theme, {})
inline fun ViewManager.akHeaderView(theme: Int = 0, init: AKHeaderView.() -> Unit): AKHeaderView {
    return ankoView({ AKHeaderView(it) }, theme, init)
}

