package com.zp.android.user

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterExtras
import com.zp.android.component.RouterPath

@Deprecated("测试, 做模块独立运行入口.")
@Route(path = RouterPath.User.MAIN, name = "User首页", extras = RouterExtras.FLAG_LOGIN)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
