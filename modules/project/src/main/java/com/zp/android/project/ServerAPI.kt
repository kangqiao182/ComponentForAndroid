package com.zp.android.project

import com.zp.android.net.HttpResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by zhaopan on 2018/11/7.
 */

interface ServerAPI {
    /**
     * 项目数据
     * http://www.wanandroid.com/project/tree/json
     */
    @GET("project/tree/json")
    fun getProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>

    /**
     * 项目列表数据
     * http://www.wanandroid.com/project/list/1/json?cid=294
     * @param page
     * @param cid
     */
    @GET("project/list/{page}/json")
    fun getProjectList(@Path("page") page: Int, @Query("cid") cid: Int): Observable<HttpResult<ArticleResponseBody>>

}