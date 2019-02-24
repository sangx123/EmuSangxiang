package com.sangxiang.android.ui.login

import com.google.gson.annotations.SerializedName

data class RegisterParam(
        @SerializedName("mobile") var mobile: String ?=null,
        @SerializedName("password") var password: String?=null
)