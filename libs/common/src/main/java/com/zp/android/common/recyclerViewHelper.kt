package com.zp.android.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

/**
 * Created by zhaopan on 2018/5/16.
 */
open class DBViewHolder(view: View) : BaseViewHolder(view) {

    var binding: ViewDataBinding? = null

    init {
        //int position = getPosition();
        if (isBindingView(view)) {
            binding = DataBindingUtil.bind(view)
        }
    }

    fun <T> bindTo(brId: Int, item: T) {
        binding?.apply {
            setVariable(brId, item)
            executePendingBindings()
        }
    }
}

fun isBindingView(view: View): Boolean {
    val binding = DataBindingUtil.getBinding<ViewDataBinding>(view)
    if (binding != null) return true
    val tagObj = view.tag
    return (tagObj as? String)?.startsWith("layout/") ?: false
}


class AKViewHolder<T, ItemView : AKItemViewUI<T>>(val akItemView: ItemView, view: View) : BaseViewHolder(view) {
    fun bind(item: T) {
        akItemView.bind(item)
    }
}

interface AKItemViewUI<T> : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View

    fun bind(item: T)
}

abstract class AKBaseQuickAdapter<T, ItemView: AKItemViewUI<T>> : BaseQuickAdapter<T, AKViewHolder<T, ItemView>>(-1) {

    abstract fun onCreateItemView(): ItemView

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): AKViewHolder<T, ItemView> {
        if (layoutResId > 0) {
            return super.createBaseViewHolder(parent, layoutResId)
        } else {
            val akContext = AnkoContext.create(parent.context, parent)
            val akItemView = onCreateItemView()
            val view = akItemView.createView(akContext)
            val akVH = AKViewHolder(akItemView, view)
            return akVH
        }
    }

    override fun convert(helper: AKViewHolder<T, ItemView>, item: T) {
        helper.bind(item)
    }
}


//class AKBaseViewHolder<T>(view: View, var bind: (vh: AKBaseViewHolder<T>, item: T) -> Unit) : BaseViewHolder(view)


/*inline fun <T : ViewGroup> T.akViewHolder(theme: Int = 0, init: AKBaseViewHolder.() -> View): AnkoContext<View> {
    //return ankoView({CandyView(it)}, theme, init)
    //return BaseViewHolder(this.context).apply { init() }
    AnkoContext.createDelegate()

}*/

//fun <T, H : AKBaseViewHolder<T>> ViewManager.akBaseQuickAdapter(layoutId: Int = 0, init: (ViewGroup) -> View, bind: H.(H, T) -> Unit): BaseQuickAdapter<T, H> {
//
//    return object : BaseQuickAdapter<T, H>(layoutId) {
//        override fun convert(helper: H, item: T) {
//            helper.bind(helper, item)
//        }
//
//        override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
//            if (layoutId > 0) {
//                return super.getItemView(layoutResId, parent)
//            } else {
//                //return ankoView({init(parent)}, theme){bind }
//                return AKBaseViewHolder<T>(ankoView({init(parent)}, 0, {})){
//                    bind
//                }.itemView
//            }
//        }
//    }
//}