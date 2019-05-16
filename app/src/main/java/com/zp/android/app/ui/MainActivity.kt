package com.zp.android.app.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.app.R
import com.zp.android.app.ui.search.SearchActivity
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterPath
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.SupportFragment
import androidx.core.app.ActivityCompat
import android.widget.TextView
import com.zp.android.base.RxBus
import com.zp.android.base.flutter.ZPFlutterFragment
import com.zp.android.base.utils.RxUtil
import com.zp.android.component.ServiceManager
import com.zp.android.component.event.LoginSuccessEvent
import com.zp.android.component.event.LogoutSuccessEvent
import com.zp.android.component.service.BackResult
import com.zp.android.component.service.HandleCallBack
import me.yokeyword.fragmentation.ISupportFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick


@Route(path = RouterPath.APP.MAIN, name = "App首页")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val TAG = "MainActivity"
        const val BOTTOM_INDEX = "bottom_index"
        const val FIRST = 0
        const val SECOND = 1
        const val THIRD = 2
        const val FOURTH = 3
    }

    private val mFragments = arrayOfNulls<ISupportFragment>(4)
    private var currentTab = FIRST
    private lateinit var tvNavUsername: TextView
    private val userService by lazy { ServiceManager.getUserService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currentTab = savedInstanceState?.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        setContentView(R.layout.activity_main)
        /*setContentView(R.layout.layout_root_container)
        if (null == findFragment(MainFragment::class.java)){
            loadRootFragment(R.id.root_container, newInstance())
        }*/

        val homeFragment = ARouter.getInstance().build(RouterPath.Home.HOME).navigation() as SupportFragment
        val knowledgeFragment = ARouter.getInstance().build(RouterPath.Knowledge.HOME).navigation() as SupportFragment
        val projectFragment = ARouter.getInstance().build(RouterPath.Project.HOME).navigation() as SupportFragment
        val wechatFragment = ZPFlutterFragment.newInstance(RouterPath.Flutter.Router.WeChat)
        val firstFragment: SupportFragment? = findFragment(homeFragment.javaClass)
        Log.d(
            "zp:::",
            " homeFragment = ${homeFragment} firstFragment = ${firstFragment}, class = ${homeFragment.javaClass} savedInstanceState = ${savedInstanceState}"
        )
        if (firstFragment == null) {
            mFragments[FIRST] = homeFragment
            mFragments[SECOND] = knowledgeFragment
            mFragments[THIRD] = projectFragment
            mFragments[FOURTH] = wechatFragment//MineFragment.newInstance()

            loadMultipleRootFragment(
                R.id.container, currentTab,
                mFragments[FIRST],
                mFragments[SECOND],
                mFragments[THIRD],
                mFragments[FOURTH]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findFragment(knowledgeFragment.javaClass)
            mFragments[THIRD] = findFragment(projectFragment.javaClass)
            //mFragments[FOURTH] = findFragment(MineFragment::class.java)
            mFragments[FOURTH] = findFragment(wechatFragment.javaClass)
        }

        initView(savedInstanceState)
        registerEvents()
    }

    private fun initView(savedInstanceState: Bundle?) {
        toolbar.run {
            title = getString(R.string.app_wan_name)
            setSupportActionBar(this)
        }

        val tabIdArray = arrayOf(R.id.action_home, R.id.action_knowledge_system, R.id.action_project, R.id.action_wechat)
        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener {
                val selectTab = tabIdArray.indexOf(it.itemId)
                if (selectTab in FIRST..FOURTH) {
                    if (selectTab != currentTab) { //切换Tab
                        showHideFragment(mFragments[selectTab], mFragments[currentTab])
                        toolbar.title = it.title
                        currentTab = selectTab
                    } else { //重复选择当前Tab, TabReselected.
                        mFragments[selectTab]?.run {
                            if (supportFragmentManager.backStackEntryCount > 1) { //说明当前Tab非引模块首页, fragment回退栈数量大于1
                                // todo Do what you can do
                            }
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                } else {
                    return@setOnNavigationItemSelectedListener false
                }
            }
            toolbar.title = menu.findItem(tabIdArray[currentTab]).title
            //selectedItemId = tabIdArray[currentTab]  // error: Fragment has not been attached yet.
        }

        drawer_layout.run {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }

        nav_view.run {
            setNavigationItemSelectedListener(this@MainActivity)
            tvNavUsername = getHeaderView(0).find(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = userService.isLogin()
        }
        tvNavUsername?.run {
            text = if (!userService.isLogin()) getString(R.string.login) else userService.getUserName()
            onClick {
                if (!userService.isLogin()) {
                    ARouter.getInstance().build(RouterPath.User.LOGIN).navigation()
                } else {
                }
            }
        }

    }

    private fun registerEvents() {
        RxBus.toObservableSticky(LoginSuccessEvent.javaClass)
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe {
                nav_view.menu.findItem(R.id.nav_logout).isVisible = userService.isLogin()
                tvNavUsername.text = if (!userService.isLogin()) getString(R.string.login) else userService.getUserName()
            }
        RxBus.toObservable(LogoutSuccessEvent.javaClass)
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe {
                nav_view.menu.findItem(R.id.nav_logout).isVisible = userService.isLogin()
                tvNavUsername.text = if (!userService.isLogin()) getString(R.string.login) else userService.getUserName()
            }
    }

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BOTTOM_INDEX, currentTab)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                SearchActivity.open()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_collect -> {
                ARouter.getInstance().build(RouterPath.User.COLLECT_LIST).navigation()
            }
            R.id.nav_setting -> {

            }
            R.id.nav_about_us -> {

            }
            R.id.nav_logout -> {
                userService.logout(object: HandleCallBack<Boolean>{
                    override fun onResult(result: BackResult<Boolean>) {
                        //toast(result.message)
                    }
                })
            }
            R.id.nav_night_mode -> {

            }
            R.id.nav_todo -> {

            }
        }
        return true
    }
}