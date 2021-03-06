package com.zp.android.user.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zp.android.base.BaseActivity
import com.zp.android.base.CtxUtil
import com.zp.android.base.mvvm.ExceptionEvent
import com.zp.android.base.mvvm.FailedEvent
import com.zp.android.base.mvvm.LoadingEvent
import com.zp.android.base.mvvm.SuccessEvent
import com.zp.android.base.showToast
import com.zp.android.base.ui.WebActivity
import com.zp.android.common.DBViewHolder
import com.zp.android.common.snackBarToast
import com.zp.android.common.widget.SpaceItemDecoration
import com.zp.android.component.RouterPath
import com.zp.android.component.ServiceManager
import com.zp.android.component.service.BackResult
import com.zp.android.component.service.HandleCallBack
import com.zp.android.net.NetUtils
import com.zp.android.user.CollectionArticle
import com.zp.android.user.R
import com.zp.android.user.BR
import com.zp.android.user.CollectionResponseBody
import kotlinx.android.synthetic.main.user_activity_refresh_layout.*
import org.jetbrains.anko.support.v4.onRefresh
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by zhaopan on 2018/11/9.
 */

@Route(path = RouterPath.User.COLLECT_LIST, name = "收藏列表页面")
class CollectListActivity: BaseActivity() {

    companion object {
        const val TAG = "CollectListActivity"
        fun open() {
            ARouter.getInstance().build(RouterPath.User.COLLECT_LIST).navigation()
        }
    }

    private val vm by viewModel<CollectViewModel>()
    private lateinit var adapter: BaseQuickAdapter<CollectionArticle, DBViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity_refresh_layout)

        initView()

        requestCollectList(true, 0)
    }

    private fun initView() {
        toolbar.run {
            title = getString(R.string.nav_my_collect)
            setSupportActionBar(this)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener{ finish() }
        }

        swipeRefreshLayout.run {
            isRefreshing = true
            onRefresh {
                adapter.setEnableLoadMore(false)
                requestCollectList(true, 0)
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CollectListActivity)
            addItemDecoration(SpaceItemDecoration(this@CollectListActivity))
        }.adapter = object : BaseQuickAdapter<CollectionArticle, DBViewHolder>(R.layout.user_item_collect_article) {
            override fun convert(holder: DBViewHolder, item: CollectionArticle) {
                holder.bindTo(BR.item, item)
                holder.addOnClickListener(R.id.iv_like)
            }
        }.apply {
            adapter = this

            setOnItemClickListener { adapter, view, position ->
                (adapter.getItem(position) as? CollectionArticle)?.run {
                    // 打开某知识信息页面
                    WebActivity.open(link, title, id)
                }
            }

            setOnItemChildClickListener { adapter, view, position ->
                (adapter.getItem(position) as? CollectionArticle)?.run {
                    if(view.id == R.id.iv_like) {
                        if (ServiceManager.getUserService().isLogin()) {
                            if (!NetUtils.isNetworkAvailable(CtxUtil.context())) {
                                snackBarToast(recyclerView, CtxUtil.getString(R.string.no_network))
                                return@setOnItemChildClickListener
                            }
                            ServiceManager.getUserService()
                                .collectOrCancelArticle(this.id, false, object : HandleCallBack<String> {
                                    override fun onResult(result: BackResult<String>) {
                                        if (result.isOk()) {
                                            adapter.remove(position)
                                        }
                                        result.data?.let { CtxUtil.showToast(it) }
                                    }
                                })
                        } else {
                            ARouter.getInstance().build(RouterPath.User.LOGIN).navigation()
                            CtxUtil.showToast(R.string.login_tint)
                        }
                    }
                }
            }

        }

        vm.run {
            events.observe(this@CollectListActivity, Observer { event ->
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

            collectBody.observe(this@CollectListActivity, Observer {
                it?.run {
                    updateCollectList(it)
                }
            })
        }
    }

    fun requestCollectList(isRefresh: Boolean, num: Int) {
        vm.getCollectList(num)
    }

    fun updateCollectList(body: CollectionResponseBody<CollectionArticle>) {
        body.datas?.let {
            adapter.run {
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                if (it.size < body.size) { //如果返回数据小于每页总数, 说明没有新数据了.
                    loadMoreEnd(swipeRefreshLayout.isRefreshing)
                } else { //还有新数据
                    loadMoreComplete()
                }
            }
        }
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
            adapter.setEnableLoadMore(true)
        }
    }

}