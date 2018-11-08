package com.zp.android.user

import com.zp.android.net.HttpResult
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by zhaopan on 2018/11/7.
 */

interface ServerAPI {

    /**
     * 登录
     * http://www.wanandroid.com/user/login
     * @param username
     * @param password
     */
    @POST("user/login")
    @FormUrlEncoded
    fun loginWanAndroid(@Field("username") username: String,
                        @Field("password") password: String): Observable<HttpResult<LoginData>>

    /**
     * 注册
     * http://www.wanandroid.com/user/register
     * @param username
     * @param password
     * @param repassword
     */
    @POST("user/register")
    @FormUrlEncoded
    fun registerWanAndroid(@Field("username") username: String,
                           @Field("password") password: String,
                           @Field("repassword") repassword: String): Observable<HttpResult<LoginData>>

    /**
     * 退出登录
     * http://www.wanandroid.com/user/logout/json
     */
    @GET("user/logout/json")
    fun logout(): Observable<HttpResult<Any>>
}