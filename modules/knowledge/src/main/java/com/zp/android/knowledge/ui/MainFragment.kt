package com.zp.android.knowledge.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zp.android.base.BaseFragment
import com.zp.android.base.mvvm.ExceptionEvent
import com.zp.android.base.mvvm.FailedEvent
import com.zp.android.base.mvvm.LoadingEvent
import com.zp.android.base.mvvm.SuccessEvent
import com.zp.android.base.showToast
import com.zp.android.common.DBViewHolder
import com.zp.android.component.RouterPath
import com.zp.android.knowledge.BR
import com.zp.android.knowledge.KnowledgeTreeBody
import com.zp.android.knowledge.R
import kotlinx.android.synthetic.main.knowledge_fragment_refresh_layout.*
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.support.v4.onRefresh
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by zhaopan on 2018/10/28.
 */

@Route(path = RouterPath.Knowledge.HOME, name = "知识体系首页入口")
class MainFragment : BaseFragment() {

    companion object {
        const val TAG: String = "MainFragment"
        fun newInstance(): SupportFragment {
            return MainFragment().apply {
                arguments = Bundle()
            }
        }
    }

    private val viewModel by viewModel<ViewModel>()
    private lateinit var adapter: BaseQuickAdapter<KnowledgeTreeBody, DBViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.knowledge_fragment_refresh_layout, container, false)
    }

    override fun initView(view: View) {
        swipeRefreshLayout.onRefresh {
            onSupportVisible()
        }

        adapter = object : BaseQuickAdapter<KnowledgeTreeBody, DBViewHolder>(R.layout.knowledge_item_tree_list) {
            override fun convert(holder: DBViewHolder, item: KnowledgeTreeBody) {
                holder.bindTo(BR.item, item)
            }
        }.apply {
            setOnItemClickListener { adapter, view, position ->
                (adapter.getItem(position) as? KnowledgeTreeBody)?.run {
                    // 打开知识体系
                    DetailListActivity.open(this)
                }
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(_mActivity)
            addItemDecoration(DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(_mActivity, R.drawable.base_divider_line)?.let { setDrawable(it) }
            })
        }.adapter = adapter

        viewModel.run {
            events.observe(this@MainFragment, Observer { event ->
                when (event) {
                    is LoadingEvent -> { /*显示加载中...*/
                    }
                    is SuccessEvent -> { /*加载完成.*/
                    }
                    is FailedEvent -> {
                        showToast(event.errorMsg)
                    }
                    is ExceptionEvent -> {
                        Timber.e(event.error)
                    }
                }
            })

            knowledgeTreeList.observe(this@MainFragment, Observer {
                it?.run {
                    if (swipeRefreshLayout.isRefreshing) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    adapter.loadMoreComplete()
                    adapter.replaceData(this)
                }
            })
        }
    }

    override fun onSupportVisible() {
        viewModel.loadKnowledgeTree()
    }
}
