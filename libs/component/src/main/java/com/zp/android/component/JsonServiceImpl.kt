package com.zp.android.component

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import java.lang.reflect.Type

/**
 * Created by zhaopan on 2019/1/6.
 */

@Route(path = RouterPath.Service.JSON, name = "SerializationService")
class JsonServiceImpl : SerializationService {

    override fun init(context: Context?) {}

    override fun <T : Any?> json2Object(input: String, clazz: Class<T>): T {
        return JsonMappingUtil.fromJson(input, clazz)
    }

    override fun object2Json(instance: Any): String {
        return JsonMappingUtil.toJson(instance)
    }

    override fun <T : Any?> parseObject(input: String, clazz: Type): T {
        return JsonMappingUtil.fromJson(input, clazz)
    }

}