package com.zp.android.app.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.app.R
import com.zp.android.app.ui.main.MainFragment
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterPath

@Route(path = RouterPath.APP.MAIN, name = "App首页")
class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        setContentView(R.layout.layout_root_container)

        if (null == findFragment(MainFragment::class.java)){
            loadRootFragment(R.id.root_container, MainFragment.newInstance())
        }
    }

}


/*tv_debug.onClick {
    DebugActivity.open(this@MainActivity)
}*/
