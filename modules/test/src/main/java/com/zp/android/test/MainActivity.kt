package com.zp.android.test

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.base.utils.RxUtil
import com.zp.android.common.snackBarToast
import com.zp.android.component.RouterExtras
import com.zp.android.component.RouterPath
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber

@Deprecated("测试, 做模块独立运行入口.")
@Route(path = RouterPath.TEST.MAIN, name = "Test首页", extras = RouterExtras.FLAG_LOGIN)
class MainActivity : BaseActivity() {

    companion object {
        fun open(){
            ARouter.getInstance().build(RouterPath.TEST.MAIN).navigation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        Timber.d("进入Test")

    }
}
