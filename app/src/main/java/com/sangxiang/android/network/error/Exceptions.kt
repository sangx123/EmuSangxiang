package com.sangxiang.android.network.error

import java.lang.Exception

open class CustomException : Exception()

class ConnectFailedAlertDialogException : CustomException()

class TokenExpiredException : CustomException()