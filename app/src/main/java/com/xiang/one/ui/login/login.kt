package com.xiang.one.ui.login

import com.google.gson.annotations.SerializedName

data class RegisterParam(
        @SerializedName("name") var name: String ?=null,
        @SerializedName("mobile") var mobile: String ?=null,
        @SerializedName("password") var password: String?=null
)