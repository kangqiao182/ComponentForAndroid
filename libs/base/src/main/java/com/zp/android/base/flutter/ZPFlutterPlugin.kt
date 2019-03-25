package com.zp.android.base.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.zp.android.net.NetUtils
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.lang.ref.WeakReference
import java.util.concurrent.*

/**
 * Created by zhaopan on 2019/2/12.
 */

@SuppressLint("StaticFieldLeak")
object ZPFlutterPlugin : MethodCallHandler {

    const val TAG = "ZPFlutterPlugin"
    const val CHANNEL_NAME = "plugins.flutter.io/zp_container"
    private var channel: MethodChannel? = null
    private var context: Context? = null

    @JvmStatic
    fun registerWith(registrar: PluginRegistry.Registrar) {
        context = registrar.context()
        channel = MethodChannel(registrar.messenger(), CHANNEL_NAME).apply {
            setMethodCallHandler(this@ZPFlutterPlugin)
        }
    }

    fun destory() {
        channel?.setMethodCallHandler(null)
        channel = null
        context = null
    }

    /**
     * 调用Flutter的方法
     */
    fun invokeFlutter(method: String, callback: ResultCallback? = null, vararg args: Object) {
        channel?.invokeMethod(method, args, callback)
    }

    private var executor: ExecutorService =
        Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

    fun <T> submit(task: () -> T): Future<T> = executor.submit(task)

    fun <T> invokeFlutter2(method: String, vararg args: Object): Future<T> {
        return executor.submit<T>{
            val future: Future<T>? = null
            channel?.invokeMethod(method, args, object : Result {
                override fun notImplemented() {

                }

                override fun error(p0: String?, p1: String?, p2: Any?) {
                }

                override fun success(p0: Any?) {
                }
            })

            return@submit future?.get()
        }
    }

    /*
    override fun notImplemented() {
        Log.d(TAG, "notImplemented")
    }

    override fun error(p0: String?, p1: String?, p2: Any?) {
        Log.d(TAG, "error($p0, $p1, $p2)")
    }

    override fun success(p0: Any?) {
        Log.d(TAG, "success($p0)")
    }
    */


    /**
     * 接受来自Flutter的调用.
     */
    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            METHOD_BASE_URL -> {
                result.success("")
            }
            METHOD_HTTP_HEADER -> {
                result.success("")
            }
            METHOD_TOKEN -> {
                result.success("")
            }
            METHOD_COOKIE -> {
                val domain = call.argument("domain") ?: ""
                result.success(NetUtils.getConfig(domain, ""))
            }
            METHOD_ROUTE -> {
                val isBack = call.argument("exit") ?: false //默认不返回
                val route = call.argument("goto") ?: "" //获取路由信息
                if (isBack && context is Activity) {
                    (context as Activity).finish()
                }
                if (route.isNotEmpty()) {
                    goto(route)
                }
                result.success(RESULT_SUCCESS)
            }
        }
    }

    private fun goto(route: String) {
        when (route) {

        }
    }


}
