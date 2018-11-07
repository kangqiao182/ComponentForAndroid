package com.zp.android.app.ui.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.app.R
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterPath
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

/**
 * Created by zhaopan on 2018/11/1.
 */

@Route(path = RouterPath.APP.SEARCH, name = "搜索页面")
class SearchActivity : BaseActivity() {
    companion object {
        fun open() {
            ARouter.getInstance().build(RouterPath.APP.SEARCH).navigation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }
}