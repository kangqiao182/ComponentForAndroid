package com.zp.android.base.flutter

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseFragment
import com.zp.android.component.RouterPath
import io.flutter.facade.Flutter
import io.flutter.view.FlutterView
import me.yokeyword.fragmentation.*

/**
 * Created by zhaopan on 2019/2/12.
 */

@Route(path = RouterPath.Flutter.FRAGMENT, name = "Flutter形式的Fragment, 需要route参数指定Flutter View")
class ZPFlutterFragment : BaseFragment() {

    companion object {
        const val TAG = "ZPFlutterFragment"
        fun newInstance(route: String): ISupportFragment {
            return ZPFlutterFragment().also {
                it.arguments = Bundle().apply {
                    putString(RouterPath.Flutter.PARAM.ROUTE, route)
                }
            }
        }

        fun getInstance(route: String): ISupportFragment {
            return ARouter.getInstance().build(RouterPath.Flutter.FRAGMENT)
                .withString(RouterPath.Flutter.PARAM.ROUTE, route)
                .navigation() as SupportFragment
        }
    }

    @Autowired(name = RouterPath.Flutter.PARAM.ROUTE)
    @JvmField var route: String = ""
    private var flutterView: FlutterView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ARouter.getInstance().inject(this)
        val rootView = FrameLayout(_mActivity)
        flutterView = Flutter.createView(_mActivity, lifecycle, route)
        flutterView?.run {
            ZPFlutterPlugin.registerWith(this.pluginRegistry.registrarFor(ZPFlutterPlugin.javaClass.name))
        }
        rootView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        rootView.addView(flutterView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressedSupport(): Boolean {
        if (null != flutterView) {
            flutterView?.popRoute()
            return true
        } else {
            return super.onBackPressedSupport()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        flutterView?.pluginRegistry?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != flutterView) {
            flutterView = null
        }
    }
}
