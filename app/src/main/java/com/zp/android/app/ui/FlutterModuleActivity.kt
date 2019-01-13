package com.zp.android.app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.zp.android.app.R
import io.flutter.facade.Flutter
import com.zp.android.base.BaseActivity

/**
 * Created by zhaopan on 2019/1/12.
 */

class FlutterModuleActivity : BaseActivity() {

    companion object {
        fun open(activity: Activity) {
            activity.startActivity(Intent(activity, FlutterModuleActivity::class.java).apply{

            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter)
        // 创建FlutterView，router地址为Page，此处很重要
        val content = Flutter.createView(this, lifecycle, "KYC")
        // 设置FlutterView宽高
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        // 添加FlutterView到Activity
        addContentView(content, layoutParams)
    }
}