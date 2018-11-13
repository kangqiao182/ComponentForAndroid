package com.zp.android.user

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.base.CtxUtil
import com.zp.android.base.RxBus
import com.zp.android.base.utils.RxUtil
import com.zp.android.base.utils.SPStorage
import com.zp.android.component.RouterPath
import com.zp.android.component.event.LogoutSuccessEvent
import com.zp.android.component.service.IUserService
import com.zp.android.net.exception.ExceptionHandle
import com.zp.android.store.wanandroid.Constant.Companion.LOGIN_KEY
import com.zp.android.store.wanandroid.Constant.Companion.USERNAME_KEY
import org.koin.core.KoinContext
import org.koin.standalone.StandAloneContext
import timber.log.Timber

/**
 * Created by zhaopan on 2018/11/8.
 */

@Route(path = RouterPath.Service.USER)
open class UserService2: IUserService {

    //手动注入需要的spStorage和server
    private val spStorage by lazy {
        (StandAloneContext.koinContext as KoinContext).get<SPStorage>()
    }
    private val server by lazy {
        (StandAloneContext.koinContext as KoinContext).get<ServerAPI>()
    }

    override fun init(context: Context) {
        Log.w("UserService", "spStorage=${spStorage}")
    }

    override fun isLogin() = spStorage.contains(LOGIN_KEY)

    override fun getUserName() = spStorage.get(USERNAME_KEY, "")

    override fun logout() {
        server.logout()
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if(it.isSuccess()){
                    spStorage.clearPreference()
                    RxBus.post(LogoutSuccessEvent)
                } else {
                }
            }, {
                Timber.e(it)
            })
    }

    override fun addCollectArticle(id: Int) {
        server.addCollectArticle(id)
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if(it.isSuccess()){
                    CtxUtil.showTaost(R.string.collect_success)
                } else {
                    CtxUtil.showTaost(it.errorMsg)
                }
            }, {
                CtxUtil.showTaost(ExceptionHandle.handleException(it))
            })
    }

    override fun cancelCollectArticle(id: Int) {
        server.cancelCollectArticle(id)
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if(it.isSuccess()){
                    CtxUtil.showTaost(R.string.cancel_collect_success)
                } else {
                    CtxUtil.showTaost(it.errorMsg)
                }
            }, {
                CtxUtil.showTaost(ExceptionHandle.handleException(it))
            })
    }
}

// 采用Koin的single方式自动注入spStorage和server单实例
class UserService(val spStorage: SPStorage, val server: ServerAPI): IUserService {

    override fun init(context: Context) {
        Log.w("UserService", "spStorage=${spStorage}")
    }

    override fun isLogin() = spStorage.contains(LOGIN_KEY)

    override fun getUserName() = spStorage.get(USERNAME_KEY, "")

    override fun logout() {
        server.logout()
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if(it.isSuccess()){
                    spStorage.clearPreference()
                    RxBus.post(LogoutSuccessEvent)
                } else {
                }
            }, {
                Timber.e(it)
            })
    }

}