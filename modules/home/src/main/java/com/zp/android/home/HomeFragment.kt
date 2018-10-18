package com.zp.android.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.base.BaseFragment
import com.zp.android.common.AKBaseQuickAdapter
import com.zp.android.common.AKItemViewUI
import com.zp.android.component.RouterPath
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by zhaopan on 2018/10/17.
 */

@Route(path = RouterPath.Home.HOME, name = "Home模块首页入口")
class HomeFragment : BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return HomeFragmentUI().createView(AnkoContext.create(_mActivity, this))
    }

}

class HomeFragmentUI: AnkoComponent<HomeFragment>{
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recycleView: RecyclerView
    lateinit var akAdapter: AKBaseQuickAdapter<Article, AKItemViewUI<Article>>

    override fun createView(ui: AnkoContext<HomeFragment>) = with(ui){
        swipeRefreshLayout {
            refreshLayout = this
            layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
            recycleView = recyclerView {
                layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
                backgroundColorResource = R.color.base_green
                addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL).apply {
                    ContextCompat.getDrawable(ctx, R.drawable.base_divider_line)?.let { setDrawable(it) }
                })
                akAdapter = object : AKBaseQuickAdapter<Article, AKItemViewUI<Article>>(){
                    override fun onCreateItemView() = ArticleAKItemViewUI()
                }
                akAdapter.setOnItemClickListener{adapter, view, position ->
                    var candy = adapter.getItem(position) as? Article
                    candy?.let {
                        // 打开文章链接.
                    }
                }
                Log.d("zp:::", "akAdapter = ${akAdapter}")
                adapter = akAdapter
            }
        }
    }

}

class ArticleAKItemViewUI: AKItemViewUI<Article>{
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui){
        relativeLayout {

        }
    }

    override fun bind(item: Article) {

    }

}