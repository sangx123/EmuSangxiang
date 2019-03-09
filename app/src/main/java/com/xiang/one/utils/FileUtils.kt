package com.xiang.one.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import android.os.Environment.MEDIA_MOUNTED



class FileUtils{
    companion object {
        /**
         * 保存图片
         */
        fun saveBitmap(path: String, name: String?, bitmap: Bitmap) {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }

            val _file = File(path + name!!)
            if (_file.exists()) {
                _file.delete()
            }
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(_file)
                if (name != null && "" != name) {
                    val index = name.lastIndexOf(".")
                    if (index != -1 && index + 1 < name.length) {
                        val extension = name.substring(index + 1).toLowerCase()
                        if ("png" == extension) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        } else if ("jpg" == extension || "jpeg" == extension) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }

    }

}
