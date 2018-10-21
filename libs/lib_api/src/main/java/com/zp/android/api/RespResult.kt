package com.zp.android.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.lang.RuntimeException

/**
 * Created by zhaopan on 2018/10/19.
 */

abstract class RespResult<DATA> {
    internal var type: Class<DATA>

    init {
        this.type = javaClass as Class<DATA>
    }

    abstract fun isSuccess(): Boolean

    abstract fun toData(): DATA?

    companion object {
        const val RESP_TYPE_CMD = 1
        const val RESP_TYPE_EED = 2
        const val RESP_SUCCESS = 0
        const val RESP_FAIL = -1
        const val RESP_FAILED_FORMAT = -999
        const val RESP_FAILED_CUSTOM = -998
    }

}

open class RespException(
    val code: Int = RespResult.RESP_FAIL,
    val msg: String? = null
) : RuntimeException(msg), Serializable

open class CmdResult<DATA>(
    @JsonProperty("code") val code: Int = RespResult.RESP_FAIL,
    @JsonProperty("msg") val msg: String? = "",
    @JsonProperty("data") var data: DATA? = null
) : RespResult<DATA>(), Serializable {
    override fun isSuccess() = code == RespResult.RESP_SUCCESS

    override fun toData() = data

    override fun toString() = "CmdResponse{\tcode=${code},\tmsg=${msg},\tdata=${data} }"
}

open class EedResult<DATA>(
    @JsonProperty("errorCode") val errorCode: Int = RespResult.RESP_FAIL,
    @JsonProperty("errorMsg") val errorMsg: String? = "",
    @JsonProperty("data") val data: DATA? = null
) : RespResult<DATA>(), Serializable {
    override fun isSuccess() = errorCode == RespResult.RESP_SUCCESS

    override fun toData() = data

    override fun toString() = "CmdResponse{\terrorCode=${errorCode},\terrorMsg=${errorMsg},\tdata=${data} }"
}
