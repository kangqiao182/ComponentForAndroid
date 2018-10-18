package com.zp.android.app.ui.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.zp.android.app.R
import com.zp.android.base.BaseFragment
import com.zp.android.common.AKBaseQuickAdapter
import com.zp.android.common.AKItemViewUI
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by zhaopan on 2018/10/17.
 */
@Deprecated("用home模块的")
class HomeFragment : BaseFragment(){
    companion object {
        const val TAG: String = "HomeFragment"
        fun newInstance(): SupportFragment {
            val fragment = HomeFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return HomeFragmentUI().createView(AnkoContext.create(_mActivity, this))
    }

    override fun initView() {

    }

}

class HomeFragmentUI: AnkoComponent<HomeFragment>{
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recycleView: RecyclerView
    override fun createView(ui: AnkoContext<HomeFragment>) = with(ui){
        swipeRefreshLayout {
            refreshLayout = this
            layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
            recycleView = recyclerView {
                layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
                backgroundColorResource = R.color.base_red

            }
        }
    }

}