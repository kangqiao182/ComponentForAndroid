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
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by zhaopan on 2018/6/9.
 */

abstract class BaseServer {

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
    fun <T> request(method: HttpMethod, uri: String, respType: Class<EedResult<T>>, params: HttpParams?, headers: HttpHeaders?, cacheMode: CacheMode?, cacheKey: String?): Observable<T> {
        val url = ApiUtils.getRequestUrl(SERVER, uri)
        val request: Request<EedResult<T>, out Request<*, *>> = when (method) {
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

        //val resultClazz = object : EedResult<T>(){}.javaClass.genericSuperclass
        val convert = object : ResultConvert<EedResult<T>>(respType){}
        request.converter(convert)

        return request
                .adapt(ObservableBody())
                .onErrorReturn {
                    Log.e("exception:", it.message)
                    if (it is IOException) {
                        return@onErrorReturn EedResult(RespResult.RESP_FAILED_CUSTOM, ApiUtils.getString(R.string.api_sys_check_net))
                    } else {
                        return@onErrorReturn EedResult(RespResult.RESP_FAILED_CUSTOM, ApiUtils.getString(R.string.api_unexpected_error))
                    }
                }
                .map(Function {
                    return@Function handleResponse(it)
                })
    }

    open fun <R> handleResponse(response: EedResult<R>): R?{
        return when{
            response.isSuccess() -> response.toData()
            else -> throw RespException(response.errorCode, response.errorMsg + if (BuildConfig.DEBUG) "(${response.errorCode})" else "")
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
    fun <T> get(url: String, respType: Class<EedResult<T>>, params: HttpParams?, headers: HttpHeaders? = null, cacheMode: CacheMode? = null, cacheKey: String? = null): Observable<T> {
        return request(HttpMethod.GET, url, respType, params,headers, cacheMode, cacheKey)
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
    fun <T> post(url: String, respType: Class<EedResult<T>>, params: HttpParams?, headers: HttpHeaders? = null, cacheMode: CacheMode? = null, cacheKey: String? = null): Observable<T> {
        return request(HttpMethod.POST, url, respType, params, headers, cacheMode, cacheKey)
    }

}