package com.zp.android.home

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Created by zhaopan on 2018/10/10.
 */

//文章
data class ArticleResponseBody(
    @JsonProperty("curPage") val curPage: Int = 0,
    @JsonProperty("datas") var datas: MutableList<Article>? = null,
    @JsonProperty("offset") val offset: Int,
    @JsonProperty("over") val over: Boolean,
    @JsonProperty("pageCount") val pageCount: Int,
    @JsonProperty("size") val size: Int,
    @JsonProperty("total") val total: Int
): Serializable

//文章
data class Article(
    @JsonProperty("apkLink") val apkLink: String = "",
    @JsonProperty("author") val author: String = "",
    @JsonProperty("chapterId") val chapterId: Int,
    @JsonProperty("chapterName") val chapterName: String = "",
    @JsonProperty("collect") var collect: Boolean,
    @JsonProperty("courseId") val courseId: Int,
    @JsonProperty("desc") val desc: String = "",
    @JsonProperty("envelopePic") val envelopePic: String = "",
    @JsonProperty("fresh") val fresh: Boolean,
    @JsonProperty("id") val id: Int,
    @JsonProperty("link") val link: String = "",
    @JsonProperty("niceDate") val niceDate: String = "",
    @JsonProperty("origin") val origin: String = "",
    @JsonProperty("projectLink") val projectLink: String = "",
    @JsonProperty("publishTime") val publishTime: Long,
    @JsonProperty("superChapterId") val superChapterId: Int,
    @JsonProperty("superChapterName") val superChapterName: String = "",
    @JsonProperty("tags") val tags: MutableList<Tag>? = null,
    @JsonProperty("title") val title: String = "",
    @JsonProperty("type") val type: Int,
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("visible") val visible: Int,
    @JsonProperty("zan") val zan: Int,
    @JsonProperty("top") var top: String = ""
): Serializable

data class Tag(
    @JsonProperty("name") val name: String = "",
    @JsonProperty("url") val url: String = ""
): Serializable

//轮播图
data class Banner(
    @JsonProperty("desc") val desc: String = "",
    @JsonProperty("id") val id: Int,
    @JsonProperty("imagePath") val imagePath: String = "",
    @JsonProperty("isVisible") val isVisible: Int,
    @JsonProperty("order") val order: Int,
    @JsonProperty("title") val title: String = "",
    @JsonProperty("type") val type: Int,
    @JsonProperty("url") val url: String = ""
): Serializable

data class HotKey(
    @JsonProperty("id") val id: Int,
    @JsonProperty("link") val link: String = "",
    @JsonProperty("name") val name: String = "",
    @JsonProperty("order") val order: Int,
    @JsonProperty("visible") val visible: Int
): Serializable

//常用网站
data class Friend(
    @JsonProperty("icon") val icon: String = "",
    @JsonProperty("id") val id: Int,
    @JsonProperty("link") val link: String = "",
    @JsonProperty("name") val name: String = "",
    @JsonProperty("order") val order: Int,
    @JsonProperty("visible") val visible: Int
): Serializable