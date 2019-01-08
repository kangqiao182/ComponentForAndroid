package com.zp.android.base.widget.dialog

import android.view.View
import com.zp.android.common.DBViewHolder

/**
 * Created by zhaopan on 2019/1/8.
 */

//对话框基础属性.
interface IDialogInfo {
    var title: String
    var content: String
    var closeListener: View.OnClickListener
}

//ListDialogInfo相关的ItemClickListener
interface DialogOnItemClickListener<in T> {
    fun onItemClick(dialog: CustomDialog, view: View, item: T): Boolean
}

interface AdapterBinder<in T> {
    fun bindViewHolder(holder: DBViewHolder, item: T, select: T?)
}