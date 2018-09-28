package com.xd.demi.bean

import java.io.Serializable

/**
 * Created by tong on 2018/1/19.
 */

/**
{
    "ret": "0",
    "msg": "操作成功",
    "info": {}
}
 */
open class HttpResp<out T>(val errorCode: Int, val errorMsg: String?, val data: T): Serializable

/**
{
    "ret": "0",
    "msg": "操作成功",
    "info": []
}
 */
open class HttpListResp<out T>(errorCode: Int, errorMsg: String?, data: List<T>) : HttpResp<List<T>>(errorCode, errorMsg, data)

/**
    {
    "ret": "0",
    "msg": "操作成功",
    "info": {
        "total": 0,
        "list": []
    }
}
 */
open class HttpListInfoResp<T>(errorCode: Int, errorMsg: String?, data: ListInfo<T>): HttpResp<ListInfo<T>>(errorCode, errorMsg, data)

/**
{
"total": 0,
"list": []
}
 */
class ListInfo<T>(val total: Int, val list: ArrayList<T>): Serializable

/**
{
"code": 1,
"msg": "成功"
}
 */
class XpushResp(val code: Int? = -1, val msg: String? = "") : Serializable {
    companion object {
        const val CODE_SUCCESS = 1
    }
}

/**
{
"code": 200,
"msg": "成功"
}
 */
open class CodeResp : Serializable {
    companion object {
        const val CODE_SUCCESS = "200"
    }
    var code: String? = "-1"
    var msg: String? = ""
}