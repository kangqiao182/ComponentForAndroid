package com.zp.android.knowledge

import com.zp.android.net.HttpResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by zhaopan on 2018/9/18.
 */

interface ServerAPI {

    /**
     * 获取知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    fun getKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page
     * @param cid
     */
    @GET("article/list/{page}/json")
    fun getKnowledgeList(@Path("page") page: Int, @Query("cid") cid: Int): Observable<HttpResult<ArticleResponseBody>>

}