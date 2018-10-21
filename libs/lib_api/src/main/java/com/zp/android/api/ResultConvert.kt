package com.zp.android.api

import com.lzy.okgo.convert.Converter
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by zhaopan on 2018/10/20.
 */

// T 是响应格式
abstract class ResultConvert<T>(
    val resultType: Type? = null
) : Converter<T> {
    internal var type: Class<T>

    init {
        this.type = javaClass as Class<T>
    }

    inline fun throwRespException(response: Response): Nothing {
        throw RespException(response.code(), response.message())
    }

    override fun convertResponse(response: Response): T? {
        if (response.code() == 404
            || response.code() >= 500
            || null == response.body()
        ){
            throwRespException(response)
        }

        try {
            //如果有指定响应类型, 首页用指定类型解析.
            if (null != resultType) {
                return parseType(response, resultType)
            }

            //解析泛型指定的参数类型, 会出现类型擦除, 慎用!!!
            val genType = javaClass.genericSuperclass
            val type = (genType as? ParameterizedType)?.actualTypeArguments?.get(0)

            return when (type) {
                is ParameterizedType -> parseParameterizedType(response, type)
                is Class<*> -> parseClass(response, type as Class<T>)
                else -> throwRespException(response)
            }
        } catch (e : IOException) {
            throwRespException(response)
        } finally {
            response.close()
        }
    }

    private fun parseType(response: Response, resultType: Type): T? {
        if(null == response.body() || resultType != RespResult::class.java){
            throwRespException(response)
        }
        return JsonMappingUtil.fromJson<T>(response.body()!!.charStream(), resultType)
    }

    private fun parseClass(response: Response, clzz: Class<T>): T?{
        if(null == response.body() || clzz != RespResult.javaClass){
            throwRespException(response)
        }
        return JsonMappingUtil.fromJson<T>(response.body()!!.charStream(), clzz)
    }

    private fun parseParameterizedType(response: Response, type: ParameterizedType): T? {
        if(null == response.body() || null == type){
            throwRespException(response)
        }
        val reader = response.body()!!.charStream()
        val rawType = type.rawType // 泛型的实际类型
        val typeArgument = type.actualTypeArguments.get(0) // 泛型的参数
        if(rawType == RespResult::class.java) {
            return JsonMappingUtil.fromJson<T>(reader, type)
        } else {
            throwRespException(response)
            // todo 考虑做 void.class 或以JsonObject, String的形式返回.
            val jResp = JSONObject(response.body()!!.string())
        }
    }
}