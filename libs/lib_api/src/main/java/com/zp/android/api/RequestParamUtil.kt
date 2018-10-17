package com.zp.android.api

import android.text.TextUtils
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import org.spongycastle.crypto.digests.SHA1Digest
import org.spongycastle.crypto.params.KeyParameter
import java.util.*

/**
 * Created by zhaopan on 2018/5/4.
 */
object RequestParamUtil {

    var signKey: String? = null

    /**
     * 添加公共参数.
     *
     * @param headers
     * @return
     */
    fun setCommonHeaders(header: HttpHeaders?): HttpHeaders {
        var headers = if (null == header) HttpHeaders() else header
        headers.put("Build-CU", ApiUtils.getPackageVersionCode().toString()) //各版本对应的数字，随版本增加，例如：120
        headers.put("SysVersion-CU", ApiUtils.getSystemVersion())    //手机系统，例如：7.0
        headers.put("SysSDK-CU", ApiUtils.getSystemSDK()) //操作系统对应的版本，例如24
        headers.put("Mobile-Model-CU", ApiUtils.getSystemModel())  //手机型号，例如：SM-G9550
        headers.put("UUID-CU", "")  //APP唯一值，例如 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        headers.put("Platform-CU", "android")  //操作系统，例如 android、ios
        headers.put("Network-CU", ApiUtils.getNetworkType(appContext).toString())  //网络类型：例如wifi
        headers.put("Language", ApiUtils.getRequestLanguage())     //语言zh_CN,en_US
        headers.put("CHANNEL-CU", ApiUtils.getApplicationInfo()?.metaData?.getString("CHANNEL_CU")) //语言zh_CN,en_US
        headers.put("COMPANY-ID", ApiUtils.getApplicationInfo()?.metaData?.getInt("COMPANY_ID")?.toString())

        return headers
    }

    @JvmOverloads
    fun signParams(params: HttpParams, appsecret: String? = signKey): String {
        var appSecret = if (TextUtils.isEmpty(appsecret)) "zhaopan" else appsecret!!
        try {
            val sbParam = StringBuilder()
            val sortMap = TreeMap<String, List<String>>()

            //使用sortmap排序
            for ((key, value) in params.urlParamsMap) {
                if (!TextUtils.isEmpty(key)) {
                    sortMap[key] = value
                }
            }

            for ((key, list) in sortMap) {
                if (sbParam.length > 0) sbParam.append("&")
                if (null != list) {
                    if (list.size == 1) {
                        sbParam.append(key).append("=").append(list[0])
                    } else {
                        sbParam.append(key).append("=").append(list)
                    }
                } else {
                    sbParam.append(key).append("=").append("")
                }
            }

            val hmacSHA1Bytes = hmacSHA1Encrypt(sbParam.toString(), appSecret)
            return toHexString(hmacSHA1Bytes)
        } catch (var5: Exception) {
            var5.printStackTrace()
            return "error"
        }
    }

    private fun createHmacSha1Digest(key: ByteArray): HMac {
        val digest = SHA1Digest()
        val hMac = HMac(digest)
        hMac.init(KeyParameter(key))
        return hMac
    }

    private fun hmacSha1(hmacSha1: HMac, input: ByteArray): ByteArray {
        hmacSha1.reset()
        hmacSha1.update(input, 0, input.size)
        val out = ByteArray(hmacSha1.macSize)
        hmacSha1.doFinal(out, 0)
        return out
    }

    @Throws(Exception::class)
    private fun hmacSHA1Encrypt(encryptText: String, encryptKey: String): ByteArray {
        //String MAC_NAME = "HmacSHA1";
        val ENCODING = "UTF-8"
        val data = encryptKey.toByteArray(charset(ENCODING))
        //SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        val mac = createHmacSha1Digest(data)
        val text = encryptText.toByteArray(charset(ENCODING))
        return hmacSha1(mac, text)
    }

    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }


}