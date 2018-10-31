package com.zp.android.net

import com.squareup.moshi.Json

/**
 * Created by zhaopan on 2018/10/28.
 */

data class HttpResult<T>(@Json(name = "data") val data: T,
                         @Json(name = "errorCode") val errorCode: Int,
                         @Json(name = "errorMsg") val errorMsg: String)