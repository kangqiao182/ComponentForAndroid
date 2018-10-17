package com.zp.android.base.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import android.webkit.WebView
import java.util.*
import android.annotation.TargetApi
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.component.RouterPath

/**
 * Created by zhaopan on 2018/6/25.
 */

enum class SupportLanguage(val id: Int, val desc: String, val locale: Locale) {
    CHINESE_SP(1, "中文", Locale.SIMPLIFIED_CHINESE),
    ENGLISH(2, "English", Locale.ENGLISH);
    //KOREAN(3, "한국어", Locale.KOREAN);

    companion object {
        val DEFAULT = SupportLanguage.CHINESE_SP
        fun valueOF(id: Int): SupportLanguage? {
            for (unit in SupportLanguage.values()) {
                if (unit.id.equals(id)) return unit
            }
            return null
        }

        //判断是否支持local语言设置, 若不支持, 用支持的默认语言代替.
        fun support(locale: Locale): SupportLanguage {
            for (unit in SupportLanguage.values()) {
                if (unit.locale.language.equals(locale.language)) return unit
            }
            return DEFAULT
        }
    }
}

object I18NUtil {
    const val TAG = "I18NUtil"
    const val APP_LOCALE_LANGUAGE = "locale_language"

    fun changeAppLanguage(context: Context, newLanguage: SupportLanguage, isReset: Boolean = false) {
        if (!isChange(context, newLanguage)) {
            setLocale(context, newLanguage.locale)
            saveSettingLanguage(newLanguage)
            if(isReset)restartMainActvity(context)
        }
    }

    fun changeAppLanguage(context: Context) {
        changeAppLanguage(context, getSupportLanguage())
    }

    fun attachBaseContext(context: Context): Context {
        // 8.0需要使用createConfigurationContext处理 https://blog.csdn.net/xiaoyu_93/article/details/79767042
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           return updateResources(context)
        } else {
            context
        }
    }

    //8.0需要为每个activity的context植入语言环境.
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context): Context {
        val resources = context.resources
        val locale = getSupportLanguage().locale
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.locales = LocaleList(locale)
        return context.createConfigurationContext(configuration)
    }

    fun setLocale(context: Context, locale: Locale) {
        // 解决webview所在的activity语言没有切换问题
        WebView(context).destroy()
        // 切换语言, 此处注意使用applicationContext环境.
        val resources = context.applicationContext.resources
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locale = locale
            configuration.locales = LocaleList(locale)
            //context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
        }

        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
        Zlog.d(TAG, "setLocale: " + locale.toString())
    }

    fun isChange(context: Context, language: SupportLanguage): Boolean {
        val appLocale = getAppLanguage(context)
        return appLocale.equals(language.locale)
    }

    fun getSupportLanguage(): SupportLanguage{
        //判断当前是否设置了语言, 没有就判断系统设置的语言是否支持. 若支持, 则用系统的, 否则, 选用SupportLanguage列表中默认DEFAULT语言.
        return getSettingLanguage()?: SupportLanguage.support(getSysLanguage())
    }

    fun getSysLanguage(): Locale {
        //https://www.jianshu.com/p/16efe98d4554  解决7.0以上系统存在的兼容问题
        //由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault()[0]
        } else {
            return Locale.getDefault()
        }
    }

    fun getAppLanguage(context: Context): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.resources.configuration.locales[0]
        } else {
            return context.resources.configuration.locale
        }
    }

    fun hasSetLanguage(): Boolean {
        return SPUtil.contains(APP_LOCALE_LANGUAGE)
    }

    fun saveSettingLanguage(language: SupportLanguage) {
        SPUtil.put(APP_LOCALE_LANGUAGE, language.id)
    }

    fun getSettingLanguage(): SupportLanguage? {
        return SupportLanguage.valueOF(SPUtil.getInt(APP_LOCALE_LANGUAGE, -1))
    }

    fun restartMainActvity(context: Context) {
        /*
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        */
        // 跳转首页 -> 采用ARouter方式
        ARouter.getInstance().build(RouterPath.APP.MAIN)
                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation()
        // 杀掉进程，如果是跨进程则杀掉当前进程
        //        android.os.Process.killProcess(android.os.Process.myPid());
        //        System.exit(0);
    }
}
