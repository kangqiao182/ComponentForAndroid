package com.zp.android.component

import android.content.Context
import android.os.Environment
import android.util.Log
import com.didi.virtualapk.PluginManager
import java.io.File
import androidx.databinding.DataBindingUtil

/**
 * Created by zhaopan on 2018/8/26.
 */

const val TAG = "virtualAPK"
const val PLUGIN_TEST = "Test.apk"
const val PLUGIN_TEST_PACKAGE_NAME = "com.zp.test"
const val PLUGIN_TEST_HOME_ACTIVITY = "com.zp.test.HomeActivity"

fun initVirtualApk(context: Context){
    PluginManager.getInstance(context).init()
}

fun loadTestByExternalStorage(context: Context, pluginName: String = "Test.apk"){
    val pluginPathDir = Environment.getExternalStorageDirectory().getAbsolutePath()
    val pluginFile = File(pluginPathDir, pluginName)
    if(pluginFile.exists()) {
        Log.w("loadTestByES", pluginFile.absolutePath)
        PluginManager.getInstance(context).loadPlugin(pluginFile)
    } else {
        Log.e("loadTestByES", "not found $pluginName")
    }
}

// 升级后需要先清除应用缓存中的apk文件, 重新从assets文件中拷贝新的插件.
fun cleanCachePlugin(context: Context, pluginPath: String){
    Log.d(TAG, "entry cleanCachePlugin.")
    PluginLoadUtil.getExternalFile(context, pluginPath)?.let {
        PluginLoadUtil.delFileRecurision(it)
        Log.w(TAG, "!!!Deleted the plug-in(${it.absolutePath}) from local caching. ")
    }
}

fun loadPlugin(context: Context, packageName: String, pluginPath: String){
    if(null == PluginManager.getInstance(context).getLoadedPlugin(packageName)) {
        // 从应用目录中获取插件.
        var pluginFile = PluginLoadUtil.getExternalFile(context, pluginPath)

        // 如果不存在, 将Assets资源中的pluginPath导出到应用目录中, 作为插件加载
        if(null == pluginFile || !pluginFile.exists()){
            Log.e(TAG, "$pluginPath not found in external dir, and will export it from assets.")
            pluginFile = PluginLoadUtil.saveAsFileFromAssets(context, pluginPath, pluginPath)
        }

        // 测试用 *** 如果Asset中不存在, 到手机外存根目录中找pluginPath.
        if(null == pluginFile || !pluginFile.exists()){
            val rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            pluginFile = File(rootPath, pluginPath)
        }

        if(null != pluginFile && pluginFile.exists()) {
            Log.w(TAG, "startup: " + pluginFile.absolutePath)
            PluginManager.getInstance(context).loadPlugin(pluginFile)
            Log.w(TAG, "over: " + pluginFile.absolutePath)
        } else {
            Log.e(TAG, "not found $pluginPath")
        }
    } else {
        Log.w(TAG, "$packageName plugin already loaded!!!")
    }
}