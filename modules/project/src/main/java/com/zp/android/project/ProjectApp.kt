package com.zp.android.project

import android.app.Application
import com.zp.android.base.ModuleApp
import com.zp.android.base.utils.RxUtil
import com.zp.android.base.utils.SPStorage
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject

/**
 * Created by zhaopan on 2018/11/07.
 */

class ProjectApp : ModuleApp(), KoinComponent {

    private val serverApi: ServerAPI by inject()
    private val spStorage: SPStorage by inject()

    override fun onCreate() {
        super.onCreate()
        //todo 独立运行时, 测试环境设置. 此处可设置测试网络.
        initModuleApp(this)
        initModuleData(this)
    }

    override fun initModuleApp(application: Application) {
        //startKoin(application.applicationContext, moduleList)
        //application.startKoin(application, moduleList)
        StandAloneContext.loadKoinModules(moduleList)
    }

    override fun initModuleData(application: Application) {
        serverApi.getProjectTree()
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if(it.isSuccess()) {

                } else {
                    //view?.setCategoryList(null)
                }
            }, { error ->
                //view?.showError(error)
            })
    }
}
