package com.zp.android.app

import android.util.Log
import com.zp.android.base.MainApp

/**
 * Created by zhaopan on 2018/10/10.
 */

class APP : MainApp() {
    companion object {
        const val TAG = "APP"
    }

    override fun onCreate() {
        super.onCreate()
        //com.zp.android.api.initOkGoRequestApi(this/*, net.idik.lib.cipher.so.CipherClient.signkey()*/)
        Log.e(TAG, BuildConfig.APPLICATION_ID)
        Log.e(TAG, com.zp.android.home.BuildConfig.APPLICATION_ID)
        Log.e(TAG, com.zp.android.user.BuildConfig.APPLICATION_ID)
        Log.e(TAG, com.zp.android.knowledge.BuildConfig.APPLICATION_ID)
        Log.e(TAG, com.zp.android.project.BuildConfig.APPLICATION_ID)
    }
}