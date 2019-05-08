package com.zp.android.component

/**
 * Created by zhaopan on 2018/8/22.
 */

object RouterPath {
    const val SDK_SERVICE_REPLACE = "/sdk/service/replace"
    const val SDK_SERVICE_DEGRADE = "/sdk/service/degrade"

    interface Service {
        companion object {
            const val JSON = "/service/JsonSerializationService"
        }
    }

    interface Base {
        companion object {
            const val WEB       = "/base/web"
            const val WEB3      = "/base/web3"
            const val AUTH      = "/base/auth"
        }

        interface Param {
            companion object {
                const val AUTH_REQUEST_CODE = 110
                const val AUTH_RESPONSE_RESULT = "response_result"
                const val AUTH_MODE = "auth_mode"
                const val AUTH_TYPE = "auth_type"
                const val KEY_ID = "id"
                const val KEY_URL = "url"
                const val KEY_TITLE = "title"
                const val KEY_IS_SHOW_TITLE = "isShowTitle"
            }
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
            const val MAIN      = "/home/mian"
            const val HOME      = "/home/home"
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
            const val SERVICE       = "/user/service"
            const val MAIN          = "/user/main"
            const val LOGIN         = "/user/login"
            const val REGISTER      = "/user/register"
            const val SETTINGS      = "/user/settings"
            const val COLLECT_LIST  = "/user/collect_list"
        }

        interface Param {
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

    interface Project {
        companion object {
            const val MAIN          = "/project/main"
            const val HOME          = "/project/category_list"
            const val PROJECT_LIST  = "/project/project_list"
        }

        interface PARAM {
            companion object {
                const val CID = "cid"
            }
        }
    }

    interface Flutter {
        companion object {
            const val VIEW          = "/flutter/view"
            const val FRAGMENT      = "/flutter/fragment"
        }

        interface PARAM {
            companion object {
                const val ROUTE     = "route"
            }
        }

        interface Router {
            companion object {
                const val MAIN      = "flutter.router.main"
                const val WeChat    = "wechat"
            }
        }
    }
}

interface RouterExtras {
    companion object {
        const val FLAG_LOGIN = 1 shl 10
    }
}