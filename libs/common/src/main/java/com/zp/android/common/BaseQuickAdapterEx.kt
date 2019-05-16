package com.zp.android.common

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

/**
 * Created by zhaopan on 2018/5/16.
 */

//////////////////////////////////////////////////////
// 基于DataBinding的BaseQuickAdapter中ViewHolder实现. //
//////////////////////////////////////////////////////
/**
 * DataBinding中BaseQuickAdapter专用的ViewHolder基类.
 */
open class DBViewHolder(view: View) : BaseViewHolder(view) {

    var binding: ViewDataBinding? = null

    init {
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

/**
 * 判断指定View布局中是否实现了DataBinding绑定.
 */
fun isBindingView(view: View): Boolean {
    val binding = DataBindingUtil.getBinding<ViewDataBinding>(view)
    if (binding != null) return true
    // DataBinding的实现过程中都会往RootView中注入"layout/....."字符
    return (view.tag as? String)?.startsWith("layout/") ?: false
}

/**
 * 注: 在使用Databinding适配Adapter时, 主项目依赖子项目时, 不能使用RuntimeOnly依赖,
 * 否则无法生成DataBinderMapperImpl实现类, 即无法加载子项目生成的DataBinding绑定类.
 */

/**
 * 用于Databinding适配的BaseQuickAdapter
 */
open abstract class DataBindingQuickAdapter<T> constructor(@LayoutRes layoutResId: Int, data: List<T>? = null): BaseQuickAdapter<T, DataBindingViewHolder>(layoutResId, data) {

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false) ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }
}

open class DataBindingViewHolder(view: View): BaseViewHolder(view) {

    val binding: ViewDataBinding?
        get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as? ViewDataBinding

    fun <T> bindTo(brId: Int, item: T) {
        binding?.apply {
            setVariable(brId, item)
            executePendingBindings()
        }
    }
}

/////////////////////////////////////////////////////////
// 基于Anko的BaseQuickAdapter中ViewHolder和ItemView实现. //
/////////////////////////////////////////////////////////
/**
 * ItemView的Anko实现接口.
 */
interface AKItemViewUI<T> : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View

    fun bind(akViewHolder: AKViewHolder<T>, item: T)
}

/**
 * Anko中用的BaseQuickAdapter的ViewHolder基类.
 */
class AKViewHolder<T>(val akItemView: AKItemViewUI<T>, view: View) : BaseViewHolder(view) {
    fun bind(item: T) {
        akItemView.bind(this, item)
    }

    fun addChildClickListener(view: View, flag: String) {
        if(NO_ID == view.id) view.id = View.generateViewId()
        view.setTag(flag)
        addOnClickListener(view.id)
    }

    fun addChildLongClickListener(view: View, flag: String) {
        if(NO_ID == view.id) view.id = View.generateViewId()
        view.setTag(flag)
        addOnLongClickListener(view.id)
    }
}

/**
 * Anko代码中使用的BaseQuickAdapter基类.
 */
abstract class AKBaseQuickAdapter<T> : BaseQuickAdapter<T, AKViewHolder<T>>(-1) {

    /**
     * 在Anko的RecyclerView中使用AkBaseQuickAdapter时, 仅需要为每个Item指定创建AkItemView实例即可.
     */
    abstract fun onCreateItemView(): AKItemViewUI<T>

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): AKViewHolder<T> {
        return if (layoutResId > 0) {
            super.createBaseViewHolder(parent, layoutResId)
        } else {
            val akContext = AnkoContext.create(parent.context, parent)
            val akItemView = onCreateItemView()
            val view = akItemView.createView(akContext)
            AKViewHolder(akItemView, view)
        }
    }

    override fun convert(helper: AKViewHolder<T>, item: T) {
        helper.bind(item)
    }
}