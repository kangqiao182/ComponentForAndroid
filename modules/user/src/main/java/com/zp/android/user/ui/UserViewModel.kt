package com.zp.android.user.ui

import androidx.lifecycle.MutableLiveData
import com.zp.android.base.mvvm.*
import com.zp.android.base.utils.RxUtil
import com.zp.android.base.utils.SPStorage
import com.zp.android.net.exception.ExceptionHandle
import com.zp.android.store.wanandroid.Constant.Companion.LOGIN_KEY
import com.zp.android.store.wanandroid.Constant.Companion.PASSWORD_KEY
import com.zp.android.store.wanandroid.Constant.Companion.USERNAME_KEY
import com.zp.android.user.LoginData
import com.zp.android.user.ServerAPI

/**
 * Created by zhaopan on 2018/11/7.
 */

class UserViewModel(
    val server: ServerAPI,
    val spStorage: SPStorage
) : RxViewModel() {

    val events = SingleLiveEvent<ViewModelEvent>()
    val loginData = MutableLiveData<LoginData>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    init {
        loginData.postValue(spStorage.get(LOGIN_KEY, LoginData()))
        username.postValue(spStorage.get(USERNAME_KEY, ""))
        password.postValue(spStorage.get(PASSWORD_KEY, ""))
    }

    private fun updateUserData(data: LoginData) {
        loginData.value = data
        username.value = data.username
        password.value = data.password
        spStorage.put(LOGIN_KEY, data)
        spStorage.put(USERNAME_KEY, data.username)
        spStorage.put(PASSWORD_KEY, data.password)
    }

    fun isEnableLogin() = !username.value.isNullOrBlank() && !password.value.isNullOrBlank()

    fun loginWanAndroid() {
        events.value = LoadingEvent
        if (username.value.isNullOrBlank() || password.value.isNullOrBlank()) {
            events.value = FailedEvent("用户名和密码不能为空!!!")
            return
        }
        launch {
            server.loginWanAndroid(username.value!!, password.value!!)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if (it.isSuccess()) {
                        updateUserData(it.data)
                        events.value = SuccessEvent
                    } else {
                        events.value = FailedEvent(it.errorMsg)
                    }
                }, {
                    events.value = FailedEvent(ExceptionHandle.handleException(it))
                })
        }
    }

    fun registerWanAndroid(userNameStr: String, passWordStr: String, passWordStr2: String) {
        events.value = LoadingEvent
        launch {
            server.registerWanAndroid(userNameStr, passWordStr, passWordStr2)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()) {
                        updateUserData(it.data)
                        events.value = SuccessEvent
                    } else {
                        events.value = FailedEvent(it.errorMsg)
                    }
                }, {
                    events.value = FailedEvent(ExceptionHandle.handleException(it))
                })
        }
    }

    fun loginOut() {
        launch {
            server.logout()
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    if(it.isSuccess()){
                        updateUserData(LoginData())
                        spStorage.clearPreference()
                    }
                }, {

                })
        }
    }
}