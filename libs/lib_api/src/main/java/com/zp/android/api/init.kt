package com.zp.android.api

import android.app.Application
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.lzy.okgo.model.HttpParams
import io.reactivex.functions.Function
import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager

/**
 * Created by zhaopan on 2018/2/5.
 */


const val KEY_TOKEN = "token"
const val KEY_CLOUD_TOKEN = "cloud_token"
const val KEY_UUID = "uuid"
const val KEY_USER_ID = "user_id"
const val KEY_KICK_OFF = "kick_off"
const val SETTING_DEBUG_API = "setting_debug_api"

lateinit var appContext: Application
lateinit var goOkHttpClient: OkHttpClient

fun initOkGoRequestApi(application: Application, signKey: String? = null){
    initOkGo(application)
    RequestParamUtil.signKey = signKey
}

private fun initOkGo(application: Application) {

    appContext = application

    val builder = OkHttpClient.Builder()
    //log相关
    val loggingInterceptor = com.lzy.okgo.interceptor.HttpLoggingInterceptor("OkGo")
    loggingInterceptor.setPrintLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)        //log打印级别，决定了log显示的详细程度
    loggingInterceptor.setColorLevel(Level.INFO)                               //log颜色级别，决定了log在控制台显示的颜色
    builder.addInterceptor(loggingInterceptor)                                 //添加OkGo默认debug日志
    //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
    //builder.addInterceptor(new ChuckInterceptor(this));

    //超时时间设置，默认60秒
    builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)      //全局的读取超时时间
    builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)     //全局的写入超时时间
    builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)   //全局的连接超时时间

    //自动管理cookie（或者叫session的保持），以下几种任选其一就行
    //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
    builder.cookieJar(CookieJarImpl(DBCookieStore(application)))              //使用数据库保持cookie，如果cookie不过期，则一直有效
    //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

    //https相关设置，以下几种方案根据需要自己设置
    //方法一：信任所有证书,不安全有风险
    val sslParams1 = HttpsUtils.getSslSocketFactory()

    //方法二：自定义信任规则，校验服务端证书
    //val sslParams2 = HttpsUtils.getSslSocketFactory(SafeTrustManager())

    //方法三：使用预埋证书，校验服务端证书（自签名证书）
    //var sslParams3 = HttpsUtils.getSslSocketFactory(chainApp.assets.open("xxx.cer"))
    //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
    //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));

    var sslParams = if (BuildConfig.DEBUG) HttpsUtils.getSslSocketFactory() else HttpsUtils.getSslSocketFactory(application.assets.open("zhaopan.cer"))
    builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
    //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
    //builder.hostnameVerifier(SafeHostnameVerifier())


    var headers = RequestParamUtil.setCommonHeaders(null)//header不支持中文，不允许有特殊字符
    val params = HttpParams()//param支持中文,直接传,不要自己编码

    goOkHttpClient = builder.build()
    // 其他统一的配置
    // 详细说明看GitHub文档：https://github.com/jeasonlzy/
    OkGo.getInstance().init(application)                           //必须调用初始化
            .setOkHttpClient(goOkHttpClient)               //建议设置OkHttpClient，不设置会使用默认的
            .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
            .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
            .addCommonHeaders(headers)                      //全局公共头
            .addCommonParams(params)                       //全局公共参数
}


/**
 * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
 */
private class SafeTrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return  arrayOfNulls<X509Certificate>(0)
    }

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        try {
            for (certificate in chain) {
                certificate.checkValidity() //检查证书是否过期，签名是否通过等
            }
        } catch (e: Exception) {
            throw CertificateException(e)
        }
    }
}

/**
 * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
 */
private class SafeHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String, session: SSLSession): Boolean {
        //验证主机名是否匹配
        //return hostname.equals("server.jeasonlzy.com");
        return true
    }
}