package com.zp.android.base.flutter

/**
 * Created by zhaopan on 2019/3/25.
 */

interface FlutterRoute {
    companion object {
        const val WEB   = "webActivity"
        const val LOGIN = "loginActivity"
    }

    interface PARAM {
        companion object {
            const val URL   = "url"
            const val TITLE = "title"
            const val ID    = "id"
        }
    }

}