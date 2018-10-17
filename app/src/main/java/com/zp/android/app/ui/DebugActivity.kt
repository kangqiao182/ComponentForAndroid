package com.zp.android.app.ui

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterPath
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by zhaopan on 2018/10/17.
 */
@Route(path = RouterPath.APP.DEBUG, name = "Debug测试页")
class DebugActivity: BaseActivity() {

    companion object {
        fun open(activity: Activity){
            ARouter.getInstance().build(RouterPath.APP.DEBUG).navigation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UI (true) {
            linearLayout {
                button("打开Test模块的首页"){
                    onClick {
                        ARouter.getInstance().build(RouterPath.TEST.MAIN).navigation()
                    }
                }.lparams(matchParent, wrapContent)
            }
        }
    }
}