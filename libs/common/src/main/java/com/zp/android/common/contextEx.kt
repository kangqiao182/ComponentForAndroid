package com.zp.android.common

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.zp.android.common.topsnackbar.TSnackbar
import org.jetbrains.anko.contentView
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

/**
 * Created by zhaopan on 2018/8/20.
 */

@JvmOverloads
inline fun snackBarToast(view: View, content: String, duration: Int = TSnackbar.LENGTH_SHORT): TSnackbar {
    var snackbar = TSnackbar.make(view, content, duration)
    var snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.argb(220, 245,98, 98))
    val textView = snackbarView.findViewById(R.id.snackbar_text) as TextView
    textView.gravity = Gravity.CENTER
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
    textView.setTextColor(Color.WHITE)
    snackbar.show()
    return snackbar
}

@Deprecated("使用函数形式")
fun Activity.toastOnSnackBar(content: String? = null, duration: Int = TSnackbar.LENGTH_SHORT){
    if(!content.isNullOrBlank() && null != this.contentView) {
        snackBarToast(this.contentView!!, content!!, duration)
    }
}

fun Activity.fail(toastmsg: String? = null): Nothing{
    if(!TextUtils.isEmpty(toastmsg)) this.toast(toastmsg!!)
    throw RuntimeException(toastmsg?: " interrupt run !!!")
}

fun Fragment.fail(toastmsg: String? = null): Nothing{
    if(!TextUtils.isEmpty(toastmsg)) this.toast(toastmsg!!)
    throw RuntimeException(toastmsg?: " interrupt run !!!")
}

fun Activity.snackbarFail(view: View, content: String, duration: Int = TSnackbar.LENGTH_SHORT): Nothing{
    if(content.isNotEmpty()) snackBarToast(view, content, duration)
    throw RuntimeException(content)
}

fun Fragment.snackbarFail(view: View, content: String, duration: Int = TSnackbar.LENGTH_SHORT): Nothing{
    if(content.isNotEmpty()) snackBarToast(view, content, duration)
    throw RuntimeException(content)
}


fun Activity.copy(content: String){
    val cmb = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cmb.setText(content.trim())
}

fun Activity.paste(): CharSequence?{
    val cmb = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return cmb.primaryClip?.getItemAt(0)?.coerceToText(this)
}

fun Fragment.copy(content: String){
    val cmb = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cmb.setText(content.trim())
}

fun Fragment.paste(): CharSequence?{
    val cmb = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return cmb.primaryClip?.getItemAt(0)?.coerceToText(activity)
}