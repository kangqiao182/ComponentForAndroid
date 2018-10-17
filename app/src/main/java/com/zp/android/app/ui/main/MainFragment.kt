package com.zp.android.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zp.android.app.R
import com.zp.android.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by zhaopan on 2018/10/17.
 */
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
    private var preTabIndex = FIRST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var firstFragment = findChildFragment(HomeFragment::class.java)
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFragment.newInstance()
            mFragments[SECOND] = KnowledgeTreeFragment.newInstance()
            mFragments[THIRD] = ProjectFragment.newInstance()
            mFragments[FOURTH] = MineFragment.newInstance()

            loadMultipleRootFragment(
                R.id.container, preTabIndex,
                mFragments[FIRST],
                mFragments[SECOND],
                mFragments[THIRD],
                mFragments[FOURTH]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(KnowledgeTreeFragment::class.java)
            mFragments[THIRD] = findChildFragment(ProjectFragment::class.java)
            mFragments[FOURTH] = findChildFragment(MineFragment::class.java)
        }
    }

    override fun initView() {
        val tabIdArray = arrayOf(R.id.action_home, R.id.action_knowledge_system, R.id.action_project, R.id.action_mine)
        bottom_navigation.run {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener {
                val currentTabIndex = tabIdArray.indexOf(it.itemId)
                if(currentTabIndex in FIRST..FOURTH){
                    showHideFragment(mFragments[currentTabIndex], mFragments[preTabIndex])
                    preTabIndex = currentTabIndex
                    return@setOnNavigationItemSelectedListener true
                } else {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
}