package com.zp.android.knowledge.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.base.utils.ViewUtil
import com.zp.android.common.CommonFragmentStatePageAdapter
import com.zp.android.component.RouterPath
import com.zp.android.knowledge.KnowledgeTreeBody
import com.zp.android.knowledge.R
import kotlinx.android.synthetic.main.knowledge_activity_detail.*

/**
 * Created by zhaopan on 2018/10/28.
 */

@Route(path = RouterPath.Knowledge.LIST, name = "知识体系详情列表")
class DetailListActivity : BaseActivity() {

    companion object {
        const val TAG = "DetailListActivity"
        const val CONTENT_TITLE_KEY = RouterPath.Knowledge.PARAM.LIST_TITLE
        const val CONTENT_DATA_KEY = RouterPath.Knowledge.PARAM.LIST_CONTENT_DATA
        fun open(context: Context, body: KnowledgeTreeBody) {
            context.startActivity(Intent(context, DetailListActivity::class.java).run {
                putExtra(CONTENT_TITLE_KEY, body.name)
                putExtra(CONTENT_DATA_KEY, body)
            })
        }

        fun open(body: KnowledgeTreeBody) {
            ARouter.getInstance().build(RouterPath.Knowledge.LIST)
                .withString(CONTENT_TITLE_KEY, body.name)
                .withSerializable(CONTENT_DATA_KEY, body)
                .navigation()
        }
    }

    @Autowired
    @JvmField var title = ""
    @Autowired(name = CONTENT_DATA_KEY)
    @JvmField var body: KnowledgeTreeBody? = null
    private val viewPagerAdapter by lazy {
        val tabList = body?.children?.map { it.name } ?: arrayListOf()
        val fragmentList = body?.children?.map { DetailFragment.newInstance(it.id) } ?: arrayListOf()
        CommonFragmentStatePageAdapter(supportFragmentManager, tabList, fragmentList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.knowledge_activity_detail)
        initView()
    }

    private fun initView() {
        toolbar.run {
            title = title
            setSupportActionBar(this)
            // StatusBarUtil2.setPaddingSmart(this@KnowledgeActivity, toolbar)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener{ finish() }
        }
        viewPager.run {
            adapter = viewPagerAdapter
            // addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = viewPagerAdapter.count
        }
        ViewUtil.reflex(tabLayout)
        tabLayout.run {
            setupWithViewPager(viewPager)
            // TabLayoutHelper.setUpIndicatorWidth(tabLayout)
            // addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {}

                override fun onTabUnselected(tab: TabLayout.Tab) {}

                override fun onTabSelected(tab: TabLayout.Tab) {
                    // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
                    viewPager.setCurrentItem(tab.position!!, false)
                }
            })
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_type_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}