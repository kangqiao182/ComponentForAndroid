package com.zp.android.home

import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.model.HttpParams
import com.zp.android.api.BaseApi
import io.reactivex.Observable


/**
 * Created by zhaopan on 2018/9/18.
 */

object HomeApi: BaseApi() {

    /**
     * 获取轮播图
     * http://www.wanandroid.com/banner/json
     */
    // @GET("banner/json")
    // fun getBanners(): Observable<List<Banner>>>

    /**
     * 获取首页置顶文章列表
     * http://www.wanandroid.com/article/top/json
     */
    // @GET("article/top/json")
    // fun getTopArticles(): Observable<MutableList<Article>>


    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/0/json
     * @param pageNum
     */
    @JvmOverloads
    fun getArticles(pageNum: Int, cacheMode: CacheMode = CacheMode.NO_CACHE): Observable<ArticleResponseBody> {
        return get2("article/list/${pageNum}/json", null, cacheMode)
    }

}