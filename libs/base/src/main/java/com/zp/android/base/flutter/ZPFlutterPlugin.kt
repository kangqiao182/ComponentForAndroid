package com.zp.android.base.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.ui.WebActivity
import com.zp.android.component.RouterPath
import com.zp.android.net.BASE_URL
import com.zp.android.net.NetUtils
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.*

/**
 * Created by zhaopan on 2019/2/12.
 */

@SuppressLint("StaticFieldLeak")
object ZPFlutterPlugin : MethodCallHandler {

    const val TAG = "ZPFlutterPlugin"
    const val CHANNEL_NAME = "plugins.flutter.io.zp_container"
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

    private var executor: ExecutorService = Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

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

    /**
     * 接受来自Flutter的调用.
     */
    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            METHOD_BASE_URL -> {
                result.success(BASE_URL)
            }
            METHOD_HTTP_HEADER -> {
                result.success(NetUtils.getHttpHeader())
            }
            METHOD_TOKEN -> {
                result.success("")
            }
            METHOD_COOKIE -> {
                result.success(NetUtils.cookies)
            }
            METHOD_ROUTE -> {
                val isBack = call.argument("exit") ?: false //默认不返回
                val goto = call.argument("goto") ?: "" //获取路由信息
                if (isBack && context is Activity) {
                    (context as Activity).finish()
                }
                if (goto.isNotEmpty()) {
                    val param = call.argument("param") ?: "" //获取路由参数
                    route(goto, param)
                }
                result.success(RESULT_SUCCESS)
            }
        }
    }

    private fun route(goto: String, param: String) {
        Log.e(TAG, ">>>"+param)
        when (goto) {
            FlutterRoute.WEB -> {
                if (param.isNotEmpty()) {
                    try {
                        JSONObject(param).apply {
                            val url = optString(FlutterRoute.PARAM.URL)
                            val title = optString(FlutterRoute.PARAM.TITLE)
                            val id = optInt(FlutterRoute.PARAM.ID)
                            if (url.isNotEmpty()) {
                                WebActivity.open(url = url, title = title, id = id)
                            }
                        }
                    } catch (e: JSONException) {
                        Log.d(TAG, "${e.message}")
                    }
                }
            }
            FlutterRoute.LOGIN -> {
                ARouter.getInstance().build(RouterPath.User.LOGIN).navigation()
            }
        }
    }

}
