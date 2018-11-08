package com.zp.android.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.app.R
import com.zp.android.app.R.id.toolbar
import com.zp.android.base.BaseFragment
import com.zp.android.component.RouterPath
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by zhaopan on 2018/10/17.
 */

@Deprecated("使用MainActivity了")
class MainFragment : BaseFragment() {
    companion object {
        const val TAG: String = "MainFragment"
        const val FIRST = 0
        const val SECOND = 1
        const val THIRD = 2
        const val FOURTH = 3
        fun newInstance(): SupportFragment {
            val fragment = MainFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private val mFragments = arrayOfNulls<SupportFragment>(4)
    private var currentTab = FIRST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val homeFragment =  ARouter.getInstance().build(RouterPath.Home.HOME).navigation() as SupportFragment
        val knowledgeFragment =  ARouter.getInstance().build(RouterPath.Knowledge.HOME).navigation() as SupportFragment
        val firstFragment: SupportFragment? = findChildFragment(homeFragment.javaClass)
        Log.d("zp:::", " homeFragment = ${homeFragment} firstFragment = ${firstFragment}, class = ${homeFragment.javaClass} savedInstanceState = ${savedInstanceState}")
        if (firstFragment == null) {
            mFragments[FIRST] = homeFragment
            mFragments[SECOND] = knowledgeFragment
            mFragments[THIRD] = ProjectFragment.newInstance()
            mFragments[FOURTH] = MineFragment.newInstance()

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
            mFragments[SECOND] = findChildFragment(knowledgeFragment.javaClass)
            mFragments[THIRD] = findChildFragment(ProjectFragment::class.java)
            mFragments[FOURTH] = findChildFragment(MineFragment::class.java)
        }
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.app_name)
            //setSupportActionBar(this)
        }
        val tabIdArray = arrayOf(R.id.action_home, R.id.action_knowledge_system, R.id.action_project, R.id.action_mine)
        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener {
                val selectTab = tabIdArray.indexOf(it.itemId)
                if(selectTab in FIRST..FOURTH){
                    showHideFragment(mFragments[selectTab], mFragments[currentTab])
                    currentTab = selectTab
                    return@setOnNavigationItemSelectedListener true
                } else {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()

    }

}