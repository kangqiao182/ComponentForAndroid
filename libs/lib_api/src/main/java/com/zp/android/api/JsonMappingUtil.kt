package com.zp.android.api

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.DataInput
import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.lang.reflect.Type

/**
 * Created by zhaopan on 2018/7/3.
 */
object JsonMappingUtil{
    private val mapper = ObjectMapper()

    private fun create(): ObjectMapper {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper
    }

    @Throws(IOException::class)
    fun <T> fromJson(json: String, valueType: Class<T>): T {
        return create().readValue(json, valueType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(json: String, type: Type): T {
        val javaType = create().getTypeFactory().constructType(type)
        return create().readValue(json, javaType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: Reader, valueType: Class<T>): T {
        return create().readValue(src, valueType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: Reader, type: Type): T {
        val javaType = create().getTypeFactory().constructType(type)
        return create().readValue(src, javaType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: InputStream, valueType: Class<T>): T {
        return create().readValue(src, valueType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: InputStream, type: Type): T {
        val javaType = create().getTypeFactory().constructType(type)
        return create().readValue(src, javaType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: ByteArray, valueType: Class<T>): T {
        return create().readValue(src, valueType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: ByteArray, type: Type): T {
        val javaType = create().getTypeFactory().constructType(type)
        return create().readValue(src, javaType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: DataInput, valueType: Class<T>): T {
        return create().readValue(src, valueType)
    }

    @Throws(IOException::class)
    fun <T> fromJson(src: DataInput, type: Type): T {
        val javaType = create().getTypeFactory().constructType(type)
        return create().readValue(src, javaType)
    }

    @Throws(JsonProcessingException::class)
    fun toJson(value: Any): String {
        return create().writeValueAsString(value)
    }
}