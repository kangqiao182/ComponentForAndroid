package com.zp.android.user

import com.zp.android.net.HttpResult
import io.reactivex.Observable
import retrofit2.http.*

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

    /**
     *  获取收藏列表
     *  http://www.wanandroid.com/lg/collect/list/0/json
     *  @param page
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>>

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * @param id article id
     */
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 收藏站外文章
     * http://www.wanandroid.com/lg/collect/add/json
     * @param title
     * @param author
     * @param link
     */
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    fun addCoolectOutsideArticle(@Field("title") title: String,
                                 @Field("author") author: String,
                                 @Field("link") link: String): Observable<HttpResult<Any>>

    /**
     * 文章列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     * @param id
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 收藏列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect/2805/json
     * @param id
     * @param originId
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun removeCollectArticle(@Path("id") id: Int,
                             @Field("originId") originId: Int = -1): Observable<HttpResult<Any>>

}