package com.zp.android.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.base.BaseFragment
import com.zp.android.base.mvvm.*
import com.zp.android.base.showToast
import com.zp.android.component.RouterPath
import com.zp.android.lib.statusview.*
import org.jetbrains.anko.AnkoContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by zhaopan on 2018/10/17.
 */

@Route(path = RouterPath.Home.HOME, name = "Home模块首页入口")
class HomeFragment : BaseFragment() {

    private val ui by lazy { HomeFragmentUI() }
    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var statusView: StatusView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ui.createView(AnkoContext.create(_mActivity, this))
    }

    override fun initView(view: View) {
        statusView = initStatusView(ui.recyclerView)
        statusView.config(StatusViewBuilder.Builder()
            .setOnEmptyRetryClickListener {
                requestHomeData(true, 0)
            }
            .setOnErrorRetryClickListener {
                requestHomeData(true, 0)
            }
            .build())
        viewModel.events.observe(this, Observer { event ->
            when (event) {
                is LoadingEvent -> { /*显示加载中...*/
                    statusView.showLoadingView()
                }
                is SuccessEvent -> { /*加载完成.*/
                    statusView.showContentView()
                }
                is FailedEvent -> {
                    showToast(event.errorMsg)
                    statusView.showErrorView()
                }
                is ExceptionEvent -> {
                    Timber.e(event.error)
                }
            }
        })
        viewModel.articleData.observe(this, Observer {
            it?.let { ui.updateArticleData(it) }
        })
        viewModel.bannerList.observe(this, Observer {
            it?.let {
                ui.updateBannerList(it)
            }
        })

        requestHomeData(true, 0)
    }

    fun requestHomeData(isRefresh: Boolean, num: Int) {
        viewModel.getArticleData(num)
        if (isRefresh) viewModel.getBannerList()
    }

}
