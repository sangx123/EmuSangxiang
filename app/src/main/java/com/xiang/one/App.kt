package com.xiang.one

import android.app.Application
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.support.multidex.MultiDexApplication
import android.util.Log
import android.widget.ImageView
import cn.jpush.android.api.JPushInterface
import com.xiang.one.network.model.MyObjectBox
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mob.MobSDK
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.LogInterceptor
import com.tencent.bugly.crashreport.CrashReport
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException

class App :Application() {

    private lateinit var rootFileDir: File
    private lateinit var cacheImage: File
    companion object {

        lateinit var mInstance: App
        lateinit var lastIp: File
        lateinit var requestLog: File
        lateinit var mBoxStore: BoxStore
        fun getInstance(): App {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        AndroidThreeTen.init(this)
        EventBus.builder().addIndex(EmucooEventBusIndex()).installDefaultEventBus()
        CrashReport.initCrashReport(this, "0ce6d37649", true)
        //初始化shareperferrence
        Hawk.init(this).setLogInterceptor { message -> Log.d("sangxiang", message) }.build()
        MobSDK.init(this);
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

//        val imagePicker = ImagePicker.getInstance()
//        imagePicker.imageLoader = GlideImageLoader()   //设置图片加载器
//        imagePicker.isShowCamera = false  //显示拍照按钮
//        imagePicker.isCrop = false        //允许裁剪（单选才有效）
//        imagePicker.isSaveRectangle = true //是否按矩形区域保存
//        imagePicker.selectLimit = 1    //选中数量限制
//        imagePicker.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
//        imagePicker.focusWidth = 800   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.focusHeight = 800  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.outPutX = 1000//保存文件的宽度。单位像素
//        imagePicker.outPutY = 1000//保存文件的高度。单位像素
//        imagePicker.isMultiMode = false

        //initMaterialDrawer()
        rootFileDir = File(Environment.getExternalStorageDirectory(), "ASangxiang")
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

        cacheImage = File(rootFileDir, "cacheImage")
        if (!cacheImage.exists()) {
            cacheImage.mkdirs()
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

//    fun initMaterialDrawer() {
//        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
//            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
//                Picasso.with().load(uri).placeholder(placeholder).into(imageView)
//            }
//
//            override fun cancel(imageView: ImageView) {
//                Picasso.with().cancelRequest(imageView)
//            }
//        })
//
//    }


}