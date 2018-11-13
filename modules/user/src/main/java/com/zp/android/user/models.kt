package com.zp.android.user

import android.text.Html
import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Created by zhaopan on 2018/10/10.
 */

// 登录数据
data class LoginData(
    @Json(name = "collectIds") val collectIds: List<Any> = listOf(),
    @Json(name = "email") val email: String = "",
    @Json(name = "icon") val icon: String = "",
    @Json(name = "id") val id: Int = -1,
    @Json(name = "password") val password: String = "",
    @Json(name = "type") val type: Int = -1,
    @Json(name = "username") val username: String = ""
): Serializable

//收藏网站
data class CollectionWebsite(
    @Json(name = "desc") val desc: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "id") val id: Int,
    @Json(name = "link") var link: String,
    @Json(name = "name") var name: String,
    @Json(name = "order") val order: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "visible") val visible: Int
): Serializable


data class CollectionResponseBody<T>(
    @Json(name = "curPage") val curPage: Int,
    @Json(name = "datas") val datas: List<T>,
    @Json(name = "offset") val offset: Int,
    @Json(name = "over") val over: Boolean,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "total") val total: Int
): Serializable

data class CollectionArticle(
    @Json(name = "author") val author: String,
    @Json(name = "chapterId") val chapterId: Int,
    @Json(name = "chapterName") val chapterName: String,
    @Json(name = "courseId") val courseId: Int,
    @Json(name = "desc") val desc: String,
    @Json(name = "envelopePic") val envelopePic: String,
    @Json(name = "id") val id: Int,
    @Json(name = "link") val link: String,
    @Json(name = "niceDate") val niceDate: String,
    @Json(name = "origin") val origin: String,
    @Json(name = "originId") val originId: Int,
    @Json(name = "publishTime") val publishTime: Long,
    @Json(name = "title") val title: String,
    @Json(name = "userId") val userId: Int,
    @Json(name = "visible") val visible: Int,
    @Json(name = "zan") val zan: Int
): Serializable {
    val titleHtml get() = Html.fromHtml(title)
    val likeIcon get() = R.drawable.ic_like  // if (collect) R.drawable.ic_like else R.drawable.ic_like_not
}