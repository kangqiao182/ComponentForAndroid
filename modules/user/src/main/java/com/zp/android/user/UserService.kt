package com.zp.android.user

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.zp.android.base.utils.SPStorage
import com.zp.android.component.RouterPath
import com.zp.android.component.service.IUserService
import com.zp.android.store.wanandroid.Constant.Companion.LOGIN_KEY
import com.zp.android.store.wanandroid.Constant.Companion.USERNAME_KEY
import org.koin.core.KoinContext
import org.koin.standalone.StandAloneContext

/**
 * Created by zhaopan on 2018/11/8.
 */

//@Route(path = RouterPath.Service.USER)
//class UserService(): IUserService {
// 采用Koin的single方式单实例UserService
class UserService(val spStorage: SPStorage, val server: ServerAPI): IUserService {

    /*
    private val spStorage by lazy {
        (StandAloneContext.koinContext as KoinContext).get<SPStorage>()
    }
    */

    override fun init(context: Context) {
        Log.w("UserService", "spStorage=${spStorage}")
    }

    override fun isLogin() = spStorage.contains(LOGIN_KEY)

    override fun getUserName() = spStorage.get(USERNAME_KEY, "")

    override fun logout() {
        server.logout()
    }
}