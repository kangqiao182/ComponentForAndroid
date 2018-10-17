package com.zp.android.test

import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.model.HttpParams
import com.zp.android.api.BaseApi
import io.reactivex.Observable


/**
 * Created by zhaopan on 2018/9/18.
 */

object TestApi: BaseApi() {

    //获取测试信息
    @JvmOverloads
    fun getTestInfo(name: String, cacheMode: CacheMode = CacheMode.NO_CACHE): Observable<TestData> {
        val params = HttpParams("name", name)
        return get("api/test/info", params, TestData::class.java, cacheMode)
    }

}