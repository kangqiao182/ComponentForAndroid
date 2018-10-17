package com.zp.android.test

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Created by zhaopan on 2018/10/10.
 */

data class TestData(@JsonProperty("title") val title: String = "",
                    @JsonProperty("list") val list: List<Content>? = null) : Serializable {

}

data class Content(@JsonProperty("content") val content: String = ""): Serializable{

}