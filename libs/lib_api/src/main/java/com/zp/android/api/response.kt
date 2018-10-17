package com.zp.android.api

import java.io.Serializable

/**
 * Created by zhaopan on 2018/4/14.
 */
open class CmdResponse<T> @JvmOverloads constructor(var code: Int = RESP_INIT, var msg: String = "", var data: T? = null) : Serializable {
    companion object {
        const val RESP_SUCCESS = 0
        const val RESP_INIT = -1
        const val RESP_FAILED_FORMAT = -999
        const val RESP_FAILED_CUSTOM = -998

        const val CODE = "code"
        const val MSG = "msg"
        const val DATA = "data"
    }

    val isSuccess: Boolean
        get() = code == RESP_SUCCESS

    fun setCodeMsg(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }

    override fun toString(): String {
        return "CmdResponse{\n" +//
                "\tcode=" + code + "\n" +//
                "\tmsg='" + msg + "\'\n" +//
                "\tdata=" + data + "\n" +//
                '}'.toString()
    }
}

class CmdException(val code: Int, val msg: String) : RuntimeException(msg), Serializable

class SimpleResponse @JvmOverloads constructor(var code: Int = 0, var msg: String = "") : Serializable {
    fun toLzyResponse() = CmdResponse<Void>(code, msg)
}