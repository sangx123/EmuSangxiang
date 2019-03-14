package com.xiang.one

import android.os.Environment
import java.io.File

 class Config{
    companion object {
        // app文件存放路径
        open val ROOT_PATH:String=(Environment
                .getExternalStorageDirectory().toString()
        + File.separator
        + "one"
        + File.separator)
        @JvmField val APK_DOWNLOAD_PATH:String = (ROOT_PATH+
                "downloadApk" + File.separator)
        const val SUCCESS:String ="000"
        //腾讯bugly
        const val BuglyAppID="6811826898"
        //日志tag
        const val LogTag="sangxiang"
    }
}