package com.zp.android.base.widget.dialog

import android.app.Dialog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.annotation.StyleRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zp.android.base.R

/**
 * Created by zhaopan on 2018/8/21.
 */


open class CustomDialog @JvmOverloads constructor(
    context: Context,
    @LayoutRes open val layoutId: Int,
    @StyleRes open val theme: Int = R.style.base_custom_dialog
) : Dialog(context, theme) {
    protected val binding: ViewDataBinding

    init {
        binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        (context as? AppCompatActivity)?.lifecycle?.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy(owner: LifecycleOwner) {
                dismiss()
            }
        })
    }

    protected open fun initView() {}

    open fun <T : IDialogInfo> bindVariable(variableId: Int, @Nullable obj: T): CustomDialog {
        obj.bindingR = variableId
        obj.binder.dialog = this
        binding.setVariable(variableId, obj)
        binding.executePendingBindings()
        return this
    }

    override fun show() {
        super.show()
        /*var lp = window.attributes
        lp.width = (ViewUtil.screenWidth() *0.7f).toInt()
        window.attributes = lp*/
    }

    class DialogBinder {
        var dialog: CustomDialog? = null
    }

    //对话框基础属性.
    interface IDialogInfo {
        var binder: DialogBinder
        var title: String
        var content: String
        var closeListener: View.OnClickListener
        var bindingR: Int?
    }
}



