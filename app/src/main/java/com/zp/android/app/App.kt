package com.zp.android.app

import com.zp.android.api.initOkGoRequestApi
import com.zp.android.base.BaseApp

/**
 * Created by zhaopan on 2018/10/10.
 */

class APP : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        initOkGoRequestApi(this/*, net.idik.lib.cipher.so.CipherClient.signkey()*/)
    }
}