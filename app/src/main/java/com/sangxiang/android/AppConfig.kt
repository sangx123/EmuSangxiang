package com.sangxiang.android

import android.os.Environment
import java.io.File

 class AppConfig{
    companion object {
        // 默认存放文件下载的路径
        open val ROOT_PATH:String=(Environment
                .getExternalStorageDirectory().toString()
        + File.separator
        + "ASangxiang"
        + File.separator)

        @JvmField val APK_DOWNLOAD_PATH:String = (ROOT_PATH+
                "downloadApk" + File.separator)

        @JvmField val SUCCESS:String ="000"
    }
}