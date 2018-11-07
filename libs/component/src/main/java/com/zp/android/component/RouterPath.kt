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
            const val WEB   = "/base/web"
            const val WEB3  = "/base/web3"
        }
    }

    interface APP {
        companion object {
            const val MAIN      = "/app/main"
            const val SEARCH    = "/app/search"
            const val LOGIN     = "/app/login"
            const val SPLASH    = "/app/splash"
            const val DANGER    = "/app/danger"
            const val DEBUG     = "/app/debug"
        }
    }

    interface Home {
        companion object {
            const val MAIN = "/home/mian"
            const val HOME = "/home/home"
        }
    }

    interface Knowledge {
        companion object {
            const val MAIN      = "/knowledge/mian"
            const val HOME      = "/knowledge/home"
            const val LIST      = "/knowledge/list"
            const val DETAIL    = "/knowledge/detail"
        }

        interface PARAM {
            companion object {
                const val LIST_TITLE = "title"
                const val LIST_CONTENT_DATA = "content_data"
                const val DETAIL_CID = "cid"
            }
        }
    }

    interface User {
        companion object {
            const val MAIN      = "/user/main"
            const val LOGIN     = "/user/login"
            const val SETTINGS  = "/user/settings"
        }

        interface PARAM {
            companion object {

            }
        }
    }

    interface TEST {
        companion object {
            const val MAIN          = "/test/main"
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