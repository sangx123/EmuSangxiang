package com.xiang.one.network.param

import com.google.gson.annotations.SerializedName

data class LoginSubmit(
        @SerializedName("mobile") var mobile: String ?=null,
        @SerializedName("password") var password: String?=null
)

class SubmitableClass<T> {
    var pageNum: Int = 1
    var pageSize: Int = 1
    var sign: String = "fake sign"
    var data: T? = null
}
fun <T> Any.asSubmitableClass(): SubmitableClass<T> {
    val sub = SubmitableClass<T>()
    sub.data = this as T
    if (this is EmucooPageInfo) {
        sub.pageNum = pageNum
        sub.pageSize = pageSize
    }
    return sub
}

open class EmucooPageInfo {
    var pageNum: Int = 1
    var pageSize: Int = 10
}

