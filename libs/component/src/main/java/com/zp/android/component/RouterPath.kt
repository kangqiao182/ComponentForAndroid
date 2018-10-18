package com.zp.android.component

/**
 * Created by zhaopan on 2018/8/22.
 */

object RouterPath {
    const val SDK_SERVICE_REPLACE = "/sdk/service/replace"
    const val SDK_SERVICE_DEGRADE = "/sdk/service/degrade"

    interface Service {
        companion object {
            const val TEST = "/service/test"
        }
    }

    interface Base {
        companion object {
            const val WEB = "/base/web"
            const val WEB3 = "/base/web3"
        }
    }

    interface APP {
        companion object {
            const val MAIN = "/app/main"
            const val SPLASH = "/app/splash"
            const val DANGER = "/app/danger"
            const val DEBUG = "/app/debug"
        }
    }

    interface Home {
        companion object {
            const val MAIN = "/home/mian"
            const val HOME = "/home/home"
        }
    }

    interface TEST {
        companion object {
            const val MAIN = "/test/main"
            const val COMMIT_IDINFO = "/test/commit_idinfo"
            const val UPLOAD_IDCARD = "/test/upload_idcard"
        }
    }

}

interface RouterExtras {
    companion object {
        const val FLAG_LOGIN = 1 shl 10
    }
}