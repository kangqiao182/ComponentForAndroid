package com.zp.android.net

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

/**
 * Created by zhaopan on 2018/6/9.
 */

object NetUtils {
    const val TAG = "ApiUtils"
    private val file_name = "net_config"

    fun getContext(): Context {
        return appContext
    }
    
    private val sp by lazy { getContext().getSharedPreferences(file_name, Context.MODE_PRIVATE) }

    fun getString(resId: Int) = getContext().getString(resId)

    fun getConfig(key: String, default: String = "") = sp.getString(key, default)

    fun setConfig(key: String, value: String) = sp.edit().putString(key, value).apply()

    var token: String
        get() = getConfig(KEY_TOKEN)
        set(value) = setConfig(KEY_TOKEN, value)

    var cloudToken: String
        get() = getConfig(KEY_CLOUD_TOKEN)
        set(value) = setConfig(KEY_CLOUD_TOKEN, value)

    var uuid: String
        get() = getConfig(KEY_UUID, "none")
        set(value) = setConfig(KEY_UUID, value)

    var userId: String
        get() = getConfig(KEY_USER_ID, "none")
        set(value) = setConfig(KEY_USER_ID, value)

    val isLoginCloud: Boolean get() = cloudToken.isNotEmpty()
    val isLoginLocal: Boolean get() = token.isNotEmpty()
    val isLoginOne: Boolean get() = isLoginLocal || isLoginCloud
    val isLoginAll: Boolean get() = isLoginLocal && isLoginCloud

    fun getRequestUrl(baseUrl: String, url: String): String {
        if (TextUtils.isEmpty(baseUrl)
                || url.startsWith("http://")
                || url.startsWith("https://")
                || url.startsWith(baseUrl)) {
            return url
        } else {
            return baseUrl.trimEnd('/') + "/" + url.trimStart('/')
        }
    }

    //根据baseUrl和参数列表, 排列列表 生成缓存网络请求时的cacheKey
    fun createUrlFromParams(url: String, params: Map<String, List<String>>?, /*replaceParam: Map<String, String>? = null,*/ vararg excludes: String): String {
        try {
            val sb = StringBuilder(url)
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0)
                sb.append("&")
            else
                sb.append("?")
            if (null != params) {
                for ((key, urlValues) in params) {
                    if (-1 < excludes.indexOf(key)) continue  //如果存在排序列表中则不参与缓存key的计算.
                    for (value in urlValues) {
                        //val value = replaceParam?.get(key)?: value  //如是key需要替换, 则取replaceParam中的, 否则用原param参数列表中的.
                        val urlValue = URLEncoder.encode(value, "UTF-8")
                        sb.append(key).append("=").append(urlValue).append("&")
                    }
                }
            }
            sb.deleteCharAt(sb.length - 1)
            return sb.toString()
        } catch (e: UnsupportedEncodingException) {
            //OkLogger.printStackTrace(e)
        }

        return url
    }

    /**
     * 根据 包名获取 软件信息
     *
     * @param context
     * @param packageName
     * @param checkVC     是否检测versioncode,尝试从已安装目录下解析apk 信息
     * @return
     */
    fun getPackageInfo(context: Context?, packageName: String, checkVC: Boolean): PackageInfo? {
        if (null == context) {
            return null
        }
        var info: PackageInfo? = null
        val manager = context.packageManager
        // 根据packageName获取packageInfo
        try {
            info = manager.getPackageInfo(packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES)
            // 如果版本号取值异常，尝试从已安装目录下解析apk 信息
            if (checkVC && info != null && (info.versionCode <= 0 || info.versionCode == Integer.MAX_VALUE)) {
                val aInfo = info.applicationInfo
                if (aInfo != null && !TextUtils.isEmpty(aInfo.publicSourceDir)) {
                    val apkInfo = manager.getPackageArchiveInfo(aInfo.publicSourceDir, 0)
                    if (apkInfo != null) {
                        info.versionCode = apkInfo.versionCode
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message)
        }

        return info
    }

    /**
     * 根据packageName获取packageInfo
     */
    fun getPackageInfo(context: Context, packageName: String): PackageInfo? {
        return getPackageInfo(context, packageName, false)
    }

    fun getPackageInfo(): PackageInfo? {
        return getPackageInfo(getContext(), getContext().getPackageName(), true)
    }

    fun getPackageVersionCode(): Int {
        val pinfo = getPackageInfo()
        return if (null != pinfo) pinfo.versionCode else 0
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    fun getSystemVersion() = android.os.Build.VERSION.RELEASE

    fun getSystemSDK() = android.os.Build.VERSION.SDK_INT.toString()

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun getSystemModel() = android.os.Build.MODEL


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage() = Locale.getDefault().language

    fun getRequestLanguage(): String {
        val cur = getSystemLanguage()
        when (cur.toLowerCase()) {
            "zh", "zh_cn", "zh-cn" -> return "zh_CN"
            "en", "en_us", "en-us" -> return "en_US"
            else -> return ""
        }
    }

    fun getApplicationInfo(): ApplicationInfo? {
        var info: ApplicationInfo? = null
        // 根据packageName获取packageInfo
        try {
            info = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message)
        }
        return info
    }


    //===================================

    /**
     * check NetworkAvailable
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context = appContext): Boolean {
        val manager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isAvailable)
    }

    /**
     * 获取活动网络信息
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    enum class NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    const val NETWORK_TYPE_GSM = 16
    const val NETWORK_TYPE_TD_SCDMA = 17
    const val NETWORK_TYPE_IWLAN = 18

    /**
     * 获取当前网络类型
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return 网络类型
     *
     *  * [NetworkUtils.NetworkType.NETWORK_WIFI]
     *  * [NetworkUtils.NetworkType.NETWORK_4G]
     *  * [NetworkUtils.NetworkType.NETWORK_3G]
     *  * [NetworkUtils.NetworkType.NETWORK_2G]
     *  * [NetworkUtils.NetworkType.NETWORK_UNKNOWN]
     *  * [NetworkUtils.NetworkType.NETWORK_NO]
     *
     */
    fun getNetworkType(context: Context): NetworkType {
        var netType = NetworkType.NETWORK_NO
        val info = getActiveNetworkInfo(context)

        info?.apply {
            netType = when (type) {
                ConnectivityManager.TYPE_WIFI -> NetworkType.NETWORK_WIFI
                ConnectivityManager.TYPE_MOBILE -> {
                    when (subtype) {
                        NETWORK_TYPE_GSM,
                        TelephonyManager.NETWORK_TYPE_GPRS,
                        TelephonyManager.NETWORK_TYPE_CDMA,
                        TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN
                        -> NetworkType.NETWORK_2G

                        NETWORK_TYPE_TD_SCDMA,
                        TelephonyManager.NETWORK_TYPE_EVDO_A,
                        TelephonyManager.NETWORK_TYPE_UMTS,
                        TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA,
                        TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP
                        -> NetworkType.NETWORK_3G

                        NETWORK_TYPE_IWLAN,
                        TelephonyManager.NETWORK_TYPE_LTE
                        -> NetworkType.NETWORK_4G

                        else -> {
                            if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                    || subtypeName.equals("WCDMA", ignoreCase = true)
                                    || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                                NetworkType.NETWORK_3G
                            } else {
                                NetworkType.NETWORK_UNKNOWN
                            }
                        }
                    }
                }
                else -> NetworkType.NETWORK_UNKNOWN
            }
        }
        return netType
    }
}