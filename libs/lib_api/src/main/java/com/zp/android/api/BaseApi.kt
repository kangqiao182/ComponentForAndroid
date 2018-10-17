package com.zp.android.api

import android.text.TextUtils
import android.util.Log
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpMethod
import com.lzy.okgo.model.HttpParams
import com.lzy.okgo.request.base.Request
import com.lzy.okrx2.adapter.ObservableBody
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by zhaopan on 2018/6/9.
 */

abstract class BaseApi {

    val DEBUG_API = ApiUtils.getSP().getString(SETTING_DEBUG_API, "http://www.wanandroid.com/")
    var SERVER = if (BuildConfig.DEBUG && !TextUtils.isEmpty(DEBUG_API)) DEBUG_API else ApiUtils.getString(R.string.api_host)

    fun setBaseUrl(url: String){
        SERVER = url
    }

    /**
     *
     * @param method    请求方法
     * @param uri       请求URL路径
     * @param type      响应类型
     * @param clazz     响应类型
     * @param params    请求参数
     * @param headers   请求头
     * @param cacheMode 缓存模式
     * @param <T>       响应类型泛型
     * @return          响应观察体
    </T> */
    fun <T> request(method: HttpMethod, uri: String, type: Type?, clazz: Class<T>?, params: HttpParams?, headers: HttpHeaders?, cacheMode: CacheMode?, cacheKey: String?): Observable<T> {
        val url = ApiUtils.getRequestUrl(SERVER, uri)
        val request: Request<CmdResponse<T>, out Request<*, *>> = when (method) {
            HttpMethod.GET -> OkGo.get(url)
            HttpMethod.POST -> OkGo.post(url)
            HttpMethod.PUT -> OkGo.put(url)
            HttpMethod.DELETE -> OkGo.delete(url)
            HttpMethod.HEAD -> OkGo.head(url)
            HttpMethod.PATCH -> OkGo.patch(url)
            HttpMethod.OPTIONS -> OkGo.options(url)
            HttpMethod.TRACE -> OkGo.trace(url)
            else -> OkGo.get(url)
        }

        //请求头设置
        var vHeaders = HttpHeaders()
        vHeaders.put(headers)
        if(!TextUtils.isEmpty(ApiUtils.token)) vHeaders.put("token", ApiUtils.token)
        request.headers(vHeaders)

        //增加公共的参数和追加时间戳, 并对所有参数进行签名.
        var vParams = HttpParams()
        vParams.put(params)
        vParams.put("time", System.currentTimeMillis().toString())
        vParams.put("sign", RequestParamUtil.signParams(vParams))
        request.params(vParams)

        if (null != cacheMode && CacheMode.NO_CACHE != cacheMode) {
            //确认要缓存, 先确定cacheKey, 优先用用户传入的cacheKey, 其次用baseurl+urlParamMap, 若都不可用则仅用request.baseUrl
            var key = cacheKey ?: ApiUtils.createUrlFromParams(request.baseUrl, params?.urlParamsMap)
            request.cacheKey(key)
            request.cacheMode(cacheMode)
        }

        when {
            type != null -> request.converter(JsonConvert(type))
            clazz != null -> request.converter(JsonConvert<CmdResponse<T>>(clazz))
            else -> request.converter(JsonConvert())
        }

        return request
                .adapt(ObservableBody())
                .onErrorReturn {
                    Log.e("exception:", it.message)
                    if (it is IOException) {
                        return@onErrorReturn CmdResponse(CmdResponse.RESP_FAILED_CUSTOM, ApiUtils.getString(R.string.api_sys_check_net))
                    } else {
                        return@onErrorReturn CmdResponse(CmdResponse.RESP_FAILED_CUSTOM, ApiUtils.getString(R.string.api_unexpected_error))
                    }
                }
                .map(Function {
                    return@Function handleResponse(it)
                })
    }

    open fun <R> handleResponse(response: CmdResponse<R>): R?{
        return when(response.code){
            0 -> response.data
            else -> throw CmdException(response.code, response.msg + if (BuildConfig.DEBUG) "(${response.code})" else "")
        }
    }

    /**
     * 公共的GET请求.
     *
     * @param url       请求地址RequestPa
     * @param params    请求参数
     * @param clazz     响应data类型
     * @param cacheMode 缓存模式
     * @param <T>       Rx转换CmdResponse时的泛型设定
     * @return Observable<T>
     */
    @JvmOverloads
    fun <T> get(url: String, params: HttpParams?, clazz: Class<T>, cacheMode: CacheMode? = null, cacheKey: String? = null): Observable<T> {
        return request(HttpMethod.GET, url, null, clazz, params, null, cacheMode, cacheKey)
    }

    /**
     * 公共的POST请求.
     *
     * @param url       请求地址
     * @param params    请求参数
     * @param clazz     响应data类型
     * @param cacheMode 缓存模式
     * @param <T>       Rx转换CmdResponse时的泛型设定
     * @return Observable<T>
     */
    @JvmOverloads
    fun <T> post(url: String, params: HttpParams?, clazz: Class<T>, cacheMode: CacheMode? = null, cacheKey: String? = null): Observable<T> {
        return request(HttpMethod.POST, url, null, clazz, params, null, cacheMode, cacheKey)
    }

    @JvmOverloads
    inline fun <reified T> request2(method: HttpMethod, uri: String, params: HttpParams?, headers: HttpHeaders?, cacheMode: CacheMode?, cacheKey: String?): Observable<T> {
        return request(method, uri, null, T::class.java, params, headers, cacheMode, cacheKey)
    }

    @JvmOverloads
    inline fun <reified T> get2(url: String, params: HttpParams?, cacheMode: CacheMode? = null, cacheKey: String? = null): Observable<T> {
        return request(HttpMethod.GET, url, null, T::class.java, params, null, cacheMode, cacheKey)
    }

    @JvmOverloads
    inline fun <reified T> post2(url: String, params: HttpParams?, cacheMode: CacheMode? = null, cacheKey: String? = null): Observable<T> {
        return request(HttpMethod.POST, url, null, T::class.java, params, null, cacheMode, cacheKey)
    }
}