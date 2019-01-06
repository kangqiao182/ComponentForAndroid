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
import com.zp.android.component.service.BackResult
import com.zp.android.component.service.HandleCallBack
import com.zp.android.component.service.IUserService
import com.zp.android.net.exception.ExceptionHandle
import com.zp.android.store.wanandroid.Constant.Companion.LOGIN_KEY
import com.zp.android.store.wanandroid.Constant.Companion.USERNAME_KEY
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

/**
 * Created by zhaopan on 2018/11/8.
 */

@Route(path = RouterPath.Service.USER, name = "用户服务类")
class UserService2 : IUserService, KoinComponent {

    //手动注入需要的spStorage和server
    private val spStorage: SPStorage by inject()
    private val server: ServerAPI by inject()

    override fun init(context: Context) {
        Log.w("UserService", "spStorage=${spStorage}")
    }

    override fun isLogin() = spStorage.contains(LOGIN_KEY)

    override fun getUserName() = spStorage.get(USERNAME_KEY, "")

    override fun logout(callBack: HandleCallBack<Boolean>) {
        server.logout()
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if (it.isSuccess()) {
                    spStorage.clearPreference()
                    RxBus.post(LogoutSuccessEvent)
                } else {
                }
                callBack.onResult(BackResult(it.errorCode, it.errorMsg, it.isSuccess()))
            }, {
                Timber.e(it)
            })
    }

    //收藏或取消指定的文章
    override fun collectOrCancelArticle(id: Int, collect: Boolean, callBack: HandleCallBack<String>) {
        val observable = if(collect) server.addCollectArticle(id) else server.cancelCollectArticle(id)
        val resultMsg by lazy {
            if (collect) CtxUtil.getString(R.string.collect_success) else CtxUtil.getString(R.string.cancel_collect_success)
        }
        observable
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                val result = if (it.isSuccess()) resultMsg else it.errorMsg
                callBack.onResult(BackResult(it.errorCode, it.errorMsg, result))
            }, {
                CtxUtil.showToast(ExceptionHandle.handleException(it))
            })
    }
}

// 采用Koin的single方式自动注入spStorage和server单实例
class UserService(val spStorage: SPStorage, val server: ServerAPI) : IUserService {

    override fun init(context: Context) {
        Log.w("UserService", "spStorage=${spStorage}")
    }

    override fun isLogin() = spStorage.contains(LOGIN_KEY)

    override fun getUserName() = spStorage.get(USERNAME_KEY, "")

    override fun logout(callBack: HandleCallBack<Boolean>) {
        server.logout()
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                if (it.isSuccess()) {
                    spStorage.clearPreference()
                    RxBus.post(LogoutSuccessEvent)
                } else {
                }
                callBack.onResult(BackResult(it.errorCode, it.errorMsg, it.isSuccess()))
            }, {
                Timber.e(it)
            })
    }

    //收藏或取消指定的文章
    override fun collectOrCancelArticle(id: Int, collect: Boolean, callBack: HandleCallBack<String>) {
        val observable = if(collect) server.addCollectArticle(id) else server.cancelCollectArticle(id)
        val resultMsg by lazy {
            if (collect) CtxUtil.getString(R.string.collect_success) else CtxUtil.getString(R.string.cancel_collect_success)
        }
        observable
            .compose(RxUtil.applySchedulersToObservable())
            .subscribe({
                val result = if (it.isSuccess()) resultMsg else it.errorMsg
                callBack.onResult(BackResult(it.errorCode, it.errorMsg, result))
            }, {
                CtxUtil.showToast(ExceptionHandle.handleException(it))
            })
    }
}