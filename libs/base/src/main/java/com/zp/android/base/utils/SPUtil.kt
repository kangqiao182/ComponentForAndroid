package com.zp.android.base.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.zp.android.base.BaseApp

/**
 * Created by zhaopan on 2018/6/25.
 */
object SPUtil {
    private const val FILE_NAME = "__sp_util"

    private val sp by lazy { context().getSharedPreferences(FILE_NAME, MODE_PRIVATE) }

    fun context(): Context = BaseApp.application

    fun contains(key: String) = sp.contains(key)

    fun remove(key: String) = sp.edit().remove(key)

    fun clear() = sp.edit().clear().commit()

    fun getAll() = sp.all

    fun getBoolean(key: String, default: Boolean = false) = sp.getBoolean(key, default)

    fun getFloat(key: String, default: Float = 0F) = sp.getFloat(key, default)

    fun getInt(key: String, default: Int = 0) = sp.getInt(key, default)

    fun getLong(key: String, default: Long = 0L) = sp.getLong(key, default)

    fun getString(key: String, default: String = "") = sp.getString(key, default)

    fun getStringSet(key: String, default: Set<String>? = null) = sp.getStringSet(key, default)

    fun <T> get(key: String, default: T){
        when(default) {
            is Boolean -> sp.getBoolean(key, default)
            is Float -> sp.getFloat(key, default)
            is Int -> sp.getInt(key, default)
            is Long -> sp.getLong(key, default)
            is String -> sp.getString(key, default)
            else -> sp.getString(key, default.toString())
        }
    }

    fun <T> put(key: String, value: T) {
        val editor = sp.edit()
        when{
            value is Boolean -> editor.putBoolean(key, value)
            value is Float -> editor.putFloat(key, value)
            value is Int -> editor.putInt(key, value)
            value is Long -> editor.putLong(key, value)
            value is String -> editor.putString(key, value)
            else -> editor.putString(key, value.toString())
        }
        editor.commit()
    }

    fun putStringSet(key: String, set: Set<String>){
        sp.edit().putStringSet(key, set).commit()
    }
}