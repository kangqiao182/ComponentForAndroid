package com.zp.android.project

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterExtras
import com.zp.android.component.RouterPath
import com.zp.android.project.ui.CategoryTabFragment
import org.jetbrains.anko.*

@Deprecated("测试, 做模块独立运行入口.")
@Route(path = RouterPath.Project.MAIN, name = "Project模块首页", extras = RouterExtras.FLAG_LOGIN)
class MainActivity : BaseActivity() {

    companion object {
        fun open(){
            ARouter.getInstance().build(RouterPath.Project.MAIN).navigation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)

        val container = verticalLayout {
            id = View.generateViewId()
        }

        if (null == findFragment(CategoryTabFragment::class.java)){
            loadRootFragment(container.id, CategoryTabFragment.getInstance())
        }
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }
}
