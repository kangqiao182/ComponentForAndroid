package com.zp.android.component.service

import java.io.Serializable

/**
 * Created by zhaopan on 2018/12/8.
 */

interface HandleCallBack<T> {
    fun onResult(result: BackResult<T>)
}

data class BackResult<T> @JvmOverloads constructor(
    val code: Int = -1,
    val message: String = "",
    val data: T? = null
) : Serializable {
    fun isOk() = code == 0
}