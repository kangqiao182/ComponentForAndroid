package com.zp.android.component

/**
 * Created by zhaopan on 2018/8/29.
 */

object Config{

    class SYS{
        companion object {
            const val KEY_UUID = "uuid"
            const val KEY_USER_ID = "user_id"
            const val KEY_NEED_BACKUP = "need_backup"
            const val KEY_MNEMONIC_LANGUAGE = "mnemonic_language"
            const val KEY_ASSET_HIDEN = "asset_hiden"
            const val KEY_TOKEN = "token"
            const val KEY_CLIENT_ID = "key_client_id"
            const val KEY_LEGAL_TENDER = "legal_tender"
            const val KEY_CONFIG = "conifg"
            const val KEY_DECYPT_RETRY_TIME = "decypt_retry_time"
            const val KEY_CLOUD_TOKEN = "cloud_token"
            const val KEY_RED_DOT = "red_dot"
            const val KEY_PRE_BUILD = "pre_build"
            const val KEY_PRE_SCENE = "pre_scene"
        }
    }

    class Market{
        companion object {
            const val SUPPORT_CURRENCIES = "support_currencies"
            const val SYMBOLS_PRECISION = "symbols_precision"
        }
    }


    class Setting{
        companion object {
            const val SETTING_CUR_CURRENCY_LANGUAGE = "setting_cur_currency_language"
            const val SETTING_DEBUG_API = "setting_debug_api"
            const val SETTING_FINGERPRINT_ENABLE = "setting_fingerprint"
            const val SETTING_REMIND_FINGERPRINT = "setting_remind_fingerprint"

            //用户解锁应用时, 密码重试次数限制, 默认为5次.
            const val AUTH_PASSWORD_UNLOCK_LIMIT_RETRY_TIMES = "auth_password_unlock_limit_retry_times"
            const val AUTH_DELAY_EXEC_LOCK_SCREENT_TIME = "auth_delay_exec_lock_screent_time"
            const val AUTH_PIN_ENCRYPED = "auth_pin_encryped"
            //解锁倒计时.
            const val KEY_UNCLOCK_TIME = "key_unclock_time"
            const val SETTING_CUR_CURRENCY_UNIT = Config.SYS.KEY_LEGAL_TENDER
        }
    }


}