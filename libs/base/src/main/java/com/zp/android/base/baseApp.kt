package com.zp.android.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.support.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.annotation.GlideModule
import com.zp.android.base.common.initLogger
import com.zp.android.base.utils.CrashHandler
import com.zp.android.base.utils.I18NUtil
import com.zp.android.base.utils.SPUtil
import me.yokeyword.fragmentation.Fragmentation
import timber.log.Timber

/**
 * Created by zhaopan on 2018/8/16.
 */

// 模块需要实现的初始化接口
interface ModuleInitializer{
    fun initModuleApp(application: Application)
    fun initModuleData(application: Application)
}

open abstract class ModuleApp: BaseApp(), ModuleInitializer {

}

open abstract class MainApp: BaseApp(){
    override fun onCreate() {
        super.onCreate()

        //不建议在Application初始化时加载插件, 应按需加载.
        //com.zp.android.component.loadPlugin(this)

        // 初始化组件 Application
        AppConfig.initModuleApp(this)
        // 其他操作
        // 所有 Application 初始化后的操作
        AppConfig.initModuleData(this)
    }
}

open abstract class BaseApp: MultiDexApplication() {
    companion object {
        open lateinit var application: Application private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        application = this
        com.zp.android.component.initVirtualApk(base)
        com.zp.android.net.initNetConfig(this)
    }

    //BaseApp的OnCreate中写相关
    override fun onCreate() {
        super.onCreate()

        // 初始化 ARouter
        if (BuildConfig.DEBUG) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 初始化 ARouter
        ARouter.init(this)

        AppManager.initActivityLifecycle()

        //初始化timber的Log打印.
        CrashHandler.initlize()
        initLogger(BuildConfig.DEBUG)

        // Fragmentation 初始设置(optional)
        Fragmentation.builder()
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .handleException({ e ->
                    Timber.e(e)
                    //upload carsh
                })
                .install()

        //初始化语言设置
        //I18NUtil.changeAppLanguage(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        I18NUtil.changeAppLanguage(this)
    }

}