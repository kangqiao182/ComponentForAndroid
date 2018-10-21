package com.zp.android.net

import android.app.Application
import okhttp3.OkHttpClient

/**
 * Created by zhaopan on 2018/10/20.
 */

const val KEY_TOKEN = "token"
const val KEY_CLOUD_TOKEN = "cloud_token"
const val KEY_UUID = "uuid"
const val KEY_USER_ID = "user_id"
const val KEY_KICK_OFF = "kick_off"
const val SETTING_DEBUG_API = "setting_debug_api"

lateinit var appContext: Application
private var netSignKey: String? = null
val BASE_URL: String by lazy { NetUtils.getString(R.string.api_host) }

fun initNetConfig(application: Application, signKey: String? = null){
    appContext = application
    netSignKey = signKey
}