package com.zp.android.net.interceptor

import com.zp.android.net.NetUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author chenxz
 * @date 2018/11/15
 * @desc NetCacheInterceptor: 有网的时候缓存
 */
class NetCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (NetUtils.isNetworkAvailable()) {
            val maxAge = 60 * 3
            // 有网络时 设置缓存超时时间0时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
        }

        return response
    }
}
