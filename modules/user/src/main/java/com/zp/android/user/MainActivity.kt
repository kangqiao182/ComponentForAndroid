package com.zp.android.user

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterExtras
import com.zp.android.component.RouterPath
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by zhaopan on 2018/11/7.
 */

@Deprecated("测试, 做模块独立运行入口.")
@Route(path = RouterPath.User.MAIN, name = "User首页", extras = RouterExtras.FLAG_LOGIN)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        relativeLayout {
            val btnLogin = button("登录") {
                id = View.generateViewId()
                onClick { LoginActivity.open() }
            }.lparams(matchParent, wrapContent){
                centerInParent()
            }

            button("注册") {
                onClick { RegisterActivity.open() }
            }.lparams(matchParent, wrapContent) {
                bottomOf(btnLogin)
            }
        }

    }
}
