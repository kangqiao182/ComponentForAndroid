package com.zp.android.common

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import timber.log.Timber

/**
 * Created by zhaopan on 2018/8/17.
 */

fun View?.setVisible(visible: Boolean = true) {
    if (this != null) visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * Sets the visibility of a [View] to [View.VISIBLE]
 */
fun View?.visible() {
    if (this != null) visibility = View.VISIBLE
}

/**
 * Sets the visibility of a [View] to [View.INVISIBLE]
 */
fun View?.invisible() {
    if (this != null) visibility = View.INVISIBLE
}

/**
 * Sets the visibility of a [View] to [View.GONE]
 */
fun View?.gone() {
    if (this != null) visibility = View.GONE
}

/**
 * Sets the visibility of a [View] to [View.GONE] depending on a predicate
 *
 * @param func If true, the visibility of the [View] will be set to [View.GONE], else [View.VISIBLE]
 */
fun View?.goneIf(func: () -> Boolean) {
    if (func()) {
        if (this != null) visibility = View.GONE
    } else {
        if (this != null) visibility = View.VISIBLE
    }
}

/**
 * Sets the visibility of a [View] to [View.INVISIBLE] depending on a predicate
 *
 * @param func If true, the visibility of the [View] will be set to [View.INVISIBLE], else [View.VISIBLE]
 */
fun View?.invisibleIf(func: () -> Boolean) {
    if (func()) {
        if (this != null) visibility = View.GONE
    } else {
        if (this != null) visibility = View.INVISIBLE
    }
}

/**
 * Allows a [ViewGroup] to inflate itself without all of the unneeded ceremony of getting a
 * [LayoutInflater] and always passing the [ViewGroup] + false. True can optionally be passed if
 * needed.
 *
 * @param layoutId The layout ID as an [Int]
 * @return The inflated [View]
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

/**
 * Returns the current [String] entered into an [EditText]. Non-null, ie can return an empty String.
 */
fun EditText?.getTextString(): String {
    return this?.text.toString()
}

/**
 * This disables the soft keyboard as an input for a given [EditText]. The method
 * [EditText.setShowSoftInputOnFocus] is officially only available on >API21, but is actually hidden
 * from >API16. Here, we attempt to set that field to false, and catch any exception that might be
 * thrown if the Android implementation doesn't include it for some reason.
 */
@SuppressLint("NewApi")
fun EditText.disableSoftKeyboard() {
    try {
        showSoftInputOnFocus = false
    } catch (e: Exception) {
        Timber.e(e)
    }
}

/**
 * Returns a physics-based [SpringAnimation] for a given [View].
 *
 * @param property The [DynamicAnimation.ViewProperty] you wish to animate, such as rotation,
 * X or Y position etc.
 * @param finalPosition The end position for the [View] after animation complete
 * @param stiffness The stiffness of the animation, see [SpringForce]
 * @param dampingRatio The damping rate of the animation, see [SpringForce]
 */
fun View.createSpringAnimation(
    property: DynamicAnimation.ViewProperty,
    finalPosition: Float,
    stiffness: Float,
    dampingRatio: Float
) = SpringAnimation(this, property).apply {
    spring = SpringForce(finalPosition).apply {
        this.stiffness = stiffness
        this.dampingRatio = dampingRatio
    }
}

/**获取ActionBar的高度*/
inline fun View.attrDimen(@AttrRes attr: Int, def: Int = 0): Int {
    val attrs = arrayListOf(attr).toIntArray()
    val values = context.theme.obtainStyledAttributes(attrs)
    try {
        return values.getDimensionPixelSize(0, def)//第一个参数数组索引，第二个参数 默认值
    } finally {
        values.recycle()
    }
}
