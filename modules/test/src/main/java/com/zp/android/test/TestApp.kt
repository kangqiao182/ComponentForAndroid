package com.zp.android.test

import android.app.Application
import com.zp.android.api.ApiUtils
import com.zp.android.api.initOkGoRequestApi
import com.zp.android.base.ModuleApp

/**
 * Created by zhaopan on 2018/10/10.
 */

/**
 * Created by zhaopan on 2018/9/18.
 */
class TestApp : ModuleApp() {


    override fun onCreate() {
        super.onCreate()
        //todo 独立运行时, 测试环境设置. 此处可设置测试网络.
        initOkGoRequestApi(this, "zp")
        TestApi.setBaseUrl("http://192.168.0.1:8888")
    }

    override fun initModuleApp(application: Application) {

    }

    override fun initModuleData(application: Application) {

    }
}