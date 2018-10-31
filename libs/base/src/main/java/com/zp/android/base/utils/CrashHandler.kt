package com.zp.android.base.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.text.TextUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.concurrent.Executors

/**
 * Created by zhaopan on 2018/6/20.
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    private lateinit var mContext: Context
    private var defaultUncaughtExceptionHandler: UncaughtExceptionHandler? = null
    private val executors = Executors.newSingleThreadExecutor()
    private val mInfo = HashMap<String, String>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss")

    companion object {
        val instance: CrashHandler by lazy { CrashHandler() }
    }

    /**
     * 初始化
     */
    fun init(context: Context) {
        mContext = context
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 1. 收集错误信息
     * 2. 保存错误信息
     * 3. 上传到服务器
     */
    override fun uncaughtException(t: Thread, e: Throwable?) {
        if (e == null) {
            // 未处理，调用系统默认的处理器处理
            if (defaultUncaughtExceptionHandler != null) {
                defaultUncaughtExceptionHandler?.uncaughtException(t, e)
            }
        } else {
            // 人为处理异常
            executors.execute {
                Looper.prepare()
                //Toast.makeText(mContext, "UnCrashException", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
//            collectErrorInfo()
//            saveErrorInfo(e)
            try {
                Thread.sleep(1000)
            } catch (e1: InterruptedException) {
                e1.printStackTrace()
            }

            //todo 未从App中移值完成.
            //exitApp(mContext)
            //Process.killProcess(Process.myPid())
            //System.exit(1)
        }
    }

    private fun saveErrorInfo(e: Throwable) {
        val sbf = StringBuffer()
        for (entry in mInfo.entries) {
            val keyName = entry.key
            val value = entry.value
            sbf.append(keyName + "=" + value + "\n")
        }
        sbf.append("\n-----Crash Log Begin-----\n")
        val stringWriter = StringWriter()
        val writer = PrintWriter(stringWriter)
        e.printStackTrace(writer)
        var cause: Throwable? = e.cause
        while (cause != null) {
            cause.printStackTrace(writer)
            cause = e.cause
        }
        writer.close()
        val string = stringWriter.toString()
        sbf.append(string)
        sbf.append("\n-----Crash Log End-----")
        val format = dateFormat.format(Date())
        val fileName = "crash-$format.log"

        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val path = mContext.getFilesDir().toString() + File.separator + "crash"
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            var fou: FileOutputStream? = null
            try {
                fou = FileOutputStream(File(path, fileName))
                fou.write(sbf.toString().toByteArray())
                fou.flush()
            } catch (e1: FileNotFoundException) {
                e1.printStackTrace()
            } catch (e1: IOException) {
                e1.printStackTrace()
            } finally {
                try {
                    fou?.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    private fun collectErrorInfo() {
        val pm = mContext.getPackageManager()
        try {
            val info = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES)
            if (info != null) {
                val versionName = if (TextUtils.isEmpty(info.versionName)) "未设置版本名称" else info.versionName
                val versionCode = info.versionCode.toString() + ""
                mInfo.put("versionName", versionName)
                mInfo.put("versionCode", versionCode)
            }
            // 获取 Build 类中所有的公共属性
            val fields = Build::class.java.fields
            if (fields != null && fields.size > 0) {
                for (field in fields) {
                    field.isAccessible = true
                    mInfo.put(field.name, field.get(null).toString())
                }
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }
}