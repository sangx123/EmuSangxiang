package com.xiang.one.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.view.View
import android.view.ViewTreeObserver
import java.io.File

class AndroidUtils(){
    companion object {
        /**
         * 获取android版本号
         */
        fun getAppVersion(context: Context): Int {
            try {
                val packageInfo = context.packageManager
                        .getPackageInfo(context.packageName, 0)
                return packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException("Could not get package name: $e")
            }

        }

        /**
         * 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径。
         * 前者获取到的就是 /sdcard/Android/data/<application package>/cache 这个路径，
         * 而后者获取到的是 /data/data/<application package>/cache 这个路径。
         */
        fun getDiskCacheDir(context: Context, uniqueName: String): File {
            val cachePath: String = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                context.externalCacheDir.path
            } else {
                context.cacheDir.path
            }
            return File(cachePath + File.separator + uniqueName)
        }

        fun <T : View> T.height(function: (Int) -> Unit) {
            if (height == 0)
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        function(height)
                    }
                })
            else function(height)
        }

    }
}
