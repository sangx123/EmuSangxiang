package com.example.sangxiang

import android.os.Environment
import android.support.multidex.MultiDexApplication
import com.example.sangxiang.network.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import java.io.File
import java.io.IOException

class App : MultiDexApplication() {

    private lateinit var rootFileDir: File

    companion object {

        lateinit var mInstance: App
        lateinit var lastIp: File
        lateinit var requestLog: File
        lateinit var mBoxStore: BoxStore
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        rootFileDir = File(Environment.getExternalStorageDirectory(), "example")
        if (!rootFileDir.exists()) {
            rootFileDir.mkdir()
        }
        lastIp = File(rootFileDir, "lastIP")
        if (!lastIp.exists()) {
            try {
                lastIp.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        requestLog = File(rootFileDir, "requestLog")
        if (!requestLog.exists()) {
            try {
                requestLog.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            val delete = requestLog.delete()
            if (delete) {
                try {
                    requestLog.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {
                println("------can not delete request log------")
            }

        }


        mBoxStore = MyObjectBox.builder().androidContext(this@App).build()

        if (BuildConfig.DEBUG) {
             AndroidObjectBrowser(mBoxStore).start(this);
        }

    }

    fun getLastIp(): File {
        return lastIp
    }

     fun getInstance(): App {
        return mInstance
    }


}