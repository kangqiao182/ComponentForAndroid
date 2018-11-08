package com.zp.android.base.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.io.*

/**
 * Created by zhaopan on 2018/11/7.
 */

class SPStorage(val context: Application, val fileName: String, mode: Int = Context.MODE_PRIVATE) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(fileName, mode)
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        prefs.edit().clear().apply()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        prefs.edit().remove(key).apply()
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(): Map<String, *> {
        return prefs.all
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, default: T): T = with(prefs) {
        return when (default) {
            is String -> getString(key, default)
            is Boolean -> getBoolean(key, default)
            is Int -> getInt(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            else -> deSerialization(getString(key, serialize(default)))
        } as T
    }

    @SuppressLint("CommitPrefEdits")
    fun <T> put(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            //is Set<*> -> putStringSet(key, value as Set<String>)
            else -> putString(key, serialize(value))
        }
        commit()
    }

    fun putStringSet(key: String, set: Set<String>) {
        prefs.edit().putStringSet(key, set).commit()
    }

    fun putStringSet(key: String, vararg set: String) {
        prefs.edit().putStringSet(key, set.toMutableSet()).commit()
    }

    /**
     * 序列化对象
     * @param person
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun <T> serialize(obj: T): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <T> deSerialization(str: String): T {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as T
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}