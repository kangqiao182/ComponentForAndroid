package com.zp.android.component.service

import com.zp.android.component.BaseService

/**
 * Created by zhaopan on 2018/11/8.
 */

interface IUserService: BaseService {

    fun isLogin(): Boolean = false

    fun getUserName(): String = ""

    fun logout() {}

    fun addCollectArticle(id: Int) {}

    fun cancelCollectArticle(id: Int) {}
}

class EmptyUserService: IUserService