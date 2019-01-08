package com.zp.android.base.widget.dialog

import android.view.View
import com.zp.android.base.CtxUtil
import com.zp.android.base.R
import com.zp.android.base.utils.StringUtil

/**
 * Created by zhaopan on 2019/1/8.
 * CustomDialog基础属性. 依个人需求可对其扩展, 扩展如下MyDialogInfo中
 */

//测试扩展Dialog数据类例子一 name是新增的信息. title, content则是父类的属性, 需要一起构造时, 主构造函数中要声明它.
//private class MyDialogInfo(title: String, content: String, val name: String): BaseDialogInfo(title=title, content=content)
//private val info = MyDialogInfo("MyTitle", "MyContent", "MyName")
//测试扩展Dialog数据类例子二 name是新增的信息. 二级构造中要调用主构造函数, 用super.为父类属性赋值.
//private class MyDialogInfo2(name: String): BaseDialogInfo(){
//    constructor(title: String, content: String, name: String): this(name){
//        super.title = title
//        super.content = content
//    }
//}
//private val info = MyDialogInfo2("MyTitle", "MyContent", "MyName")

open class BaseDialogInfo(override var binder: CustomDialog.DialogBinder = CustomDialog.DialogBinder(),
                          override var title: String = "",
                          override var content: String = "",
                          override var closeListener: View.OnClickListener = View.OnClickListener { binder.dialog?.dismiss() },
                          override var bindingR: Int? = null,
                          var subtitle: String = "",
                          var confirm: String = CtxUtil.getString(R.string.base_confirm),
                          var cancel: String = CtxUtil.getString(R.string.base_cancel),
                          var error: String = CtxUtil.getString(R.string.base_error),
                          var cancelListener: View.OnClickListener? = null,
                          var confirmListener: View.OnClickListener? = null
) : CustomDialog.IDialogInfo {
    //扩展属性
    val showTitle: Boolean
        get() = StringUtil.notEmpty(title) || StringUtil.notEmpty(subtitle)

    fun reBindDialogInfo(){
        bindingR?.let {
            binder.dialog?.bindVariable(it, this)
        }
    }

    fun dismissDialog(){
        binder.dialog?.dismiss()
    }
}