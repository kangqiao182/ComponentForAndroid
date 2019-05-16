package com.zp.android.base.widget.dialog

import android.content.Context
import androidx.annotation.LayoutRes
import android.support.annotation.StyleRes
import com.zp.android.base.R

/**
 * Created by zhaopan on 2019/1/8.
 */

/**
 * 显示自定义的对话框.
 */
fun <T : CustomDialog.IDialogInfo> showCustomDialog(context: Context, @LayoutRes layoutId: Int, bindItemId: Int, bindObj: T, @StyleRes theme: Int = R.style.base_custom_dialog): CustomDialog {
    return CustomDialog(context, layoutId).apply {
        bindVariable(bindItemId, bindObj)
        show()
    }
}

