package com.zp.android.knowledge

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zp.android.base.BaseFragment
import com.zp.android.base.WebActivity
import com.zp.android.base.mvvm.FailedEvent
import com.zp.android.base.mvvm.LoadingEvent
import com.zp.android.base.mvvm.SuccessEvent
import com.zp.android.common.DBViewHolder
import com.zp.android.common.widget.SpaceItemDecoration
import com.zp.android.component.RouterPath
import kotlinx.android.synthetic.main.knowledge_fragment_refresh_layout.*
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by zhaopan on 2018/10/30.
 */

@Route(path = RouterPath.Knowledge.DETAIL, name = "某知识详细信息")
class DetailFragment : BaseFragment() {

    companion object {
        const val CONTENT_CID_KEY = RouterPath.Knowledge.PARAM.DETAIL_CID
        fun newInstance(cid: Int): SupportFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(CONTENT_CID_KEY, cid)
                }
            }
        }

        fun getInstance(cid: Int): SupportFragment {
            return ARouter.getInstance().build(RouterPath.Knowledge.DETAIL)
                .withInt(CONTENT_CID_KEY, cid)
                .navigation() as SupportFragment
        }
    }

    @Autowired(name = CONTENT_CID_KEY)
    @JvmField var cid: Int = -1

    private val viewModel by viewModel<ViewModel>()
    private lateinit var adapter: BaseQuickAdapter<Article, DBViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this)
        return inflater.inflate(R.layout.knowledge_fragment_refresh_layout, container, false)
    }

    override fun initView() {
        swipeRefreshLayout.run {
            isRefreshing = true
            onRefresh {
                adapter.setEnableLoadMore(false)
                requestArticleData(true, 0)
            }
        }

        adapter = object : BaseQuickAdapter<Article, DBViewHolder>(R.layout.knowledge_item_detail) {
            override fun convert(holder: DBViewHolder, item: Article) {
                holder.bindTo(BR.item, item)
                holder.addOnClickListener(R.id.iv_like)
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(_mActivity)
            addItemDecoration(SpaceItemDecoration(_mActivity))
            //itemAnimator = DefaultItemAnimator()
        }.adapter = adapter.apply {
            setOnItemClickListener { adapter, view, position ->
                (adapter.getItem(position) as? Article)?.run {
                    // 打开某知识信息页面
                    WebActivity.open(link, title, id)
                }
            }

            onItemChildClickListener = object : BaseQuickAdapter.OnItemChildClickListener {
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                    (adapter.getItem(position) as? Article)?.run {

                    }
                    /*when (view.id) {
                        R.id.iv_like -> {
                            if (isLogin) {
                                if (!NetWorkUtil.isNetworkAvailable(KnowledgeApp.context)) {
                                    showSnackMsg(resources.getString(R.string.no_network))
                                    return@OnItemChildClickListener
                                }
                                val collect = data.collect
                                data.collect = !collect
                                knowledgeAdapter.setData(position, data)
                                if (collect) {
                                    mPresenter.cancelCollectArticle(data.id)
                                } else {
                                    mPresenter.addCollectArticle(data.id)
                                }
                            } else {
                                Intent(activity, LoginActivity::class.java).run {
                                    startActivity(this)
                                }
                                showToast(resources.getString(R.string.login_tint))
                            }
                        }
                    }*/
                }
            }

            setOnLoadMoreListener({
                swipeRefreshLayout.isRefreshing = false
                val page = adapter.data.size / 20
                requestArticleData(false, page)
            }, recyclerView)

            setEmptyView(R.layout.fragment_view_empty)
        }

        viewModel.run {
            events.observe(this@DetailFragment, Observer { event ->
                when (event) {
                    is LoadingEvent -> { /*显示加载中...*/
                    }
                    is SuccessEvent -> { /*加载完成.*/
                    }
                    is FailedEvent -> {
                        Timber.e(event.error)
                        toast("加载失败")
                    }
                }
            })

            articleData.observe(this@DetailFragment, Observer {
                it?.run {
                    updateArticleData(it)
                }
            })
        }
        requestArticleData(true, 0)
    }

    fun requestArticleData(isRefresh: Boolean, num: Int) {
        viewModel.requestArticleData(num, cid)
    }

    fun updateArticleData(body: ArticleResponseBody) {
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