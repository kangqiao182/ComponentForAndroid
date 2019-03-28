package com.zp.android.base.flutter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.component.RouterPath
import io.flutter.facade.Flutter
import io.flutter.view.FlutterView

/**
 * Created by zhaopan on 2019/3/11.
 */

@Route(path = RouterPath.Flutter.VIEW, name = "Flutter模块的入口, 使用时, 需要指定route参数.")
class ZPFlutterActivity: BaseActivity() {

    companion object {
        @Deprecated("建议使用ARouter方式调用, 统一管理.")
        @JvmStatic
        fun open(context: Activity, route: String) {
            val intent = Intent(context, ZPFlutterActivity::class.java)
            intent.putExtra(RouterPath.Flutter.PARAM.ROUTE, route)
            context.startActivity(intent)
        }

        @JvmStatic
        fun open(route: String) {
            ARouter.getInstance().build(RouterPath.Flutter.VIEW)
                    .withString(RouterPath.Flutter.PARAM.ROUTE, route)
                    .navigation()
        }
    }

    @Autowired(name = RouterPath.Flutter.PARAM.ROUTE)
    @JvmField var route = ""
    private var flutterView: FlutterView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        if(TextUtils.isEmpty(route)){
            route =  intent.extras.getString(RouterPath.Flutter.PARAM.ROUTE, "")
        }
        val rootView = FrameLayout(this)
        setContentView(rootView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        flutterView = Flutter.createView(this, lifecycle, route).apply {
            ZPFlutterPlugin.registerWith(pluginRegistry.registrarFor(ZPFlutterPlugin.javaClass.name))
        }
        rootView.addView(flutterView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        flutterView?.pluginRegistry?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressedSupport() {
        if (null != flutterView) {
            flutterView?.popRoute()
        } else {
            super.onBackPressedSupport()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != flutterView) {
            flutterView = null
        }
    }
}