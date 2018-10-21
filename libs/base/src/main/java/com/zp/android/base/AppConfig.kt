package com.zp.android.base

import android.app.Application

/**
 * Created by zhaopan on 2018/8/19.
 */

object AppConfig{
    const val TEST = "com.zp.android.test.TestApp"
    const val HOME = "com.zp.android.home.HomeApp"

    val MAIN_APP_CONFIG = arrayOf(
            TEST, HOME
    )

    fun prepareModules() = MAIN_APP_CONFIG

    fun initModuleApp(application: Application) {
        for (moduleApp in prepareModules()) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as? ModuleInitializer
                baseApp?.initModuleApp(application)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }

    fun initModuleData(application: Application) {
        for (moduleApp in prepareModules()) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as? ModuleInitializer
                baseApp?.initModuleData(application)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }
}