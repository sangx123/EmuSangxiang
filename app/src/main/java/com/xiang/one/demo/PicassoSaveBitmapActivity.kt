package com.xiang.one.demo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.xiang.one.BaseActivity
import com.xiang.one.BuildConfig
import com.xiang.one.R
import com.xiang.one.utils.Utils
import com.xiang.one.utils.dialog.AlertDialogs
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileNotFoundException

class PicassoSaveBitmapActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picasso_save_bitmap)
        var mActivity=this
        var path="http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg"
        AlertDialogs(mActivity).setContent("是否保存图片到相册？").setRightClickListener { alertDialogs, _ ->
            val filePath = Environment.getExternalStorageDirectory().absolutePath
            showProgressView("图片保存中!", mActivity)
            Observable.create(ObservableOnSubscribe<String> { e ->
               Glide.with(mActivity).asBitmap().load(path).listener(object: RequestListener<Bitmap>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        toast("图片加载失败")
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val filenName = Utils.getMd5Hash(path) + ".png"
                        val file = File(filePath + filenName)
                        if (file.exists()) {
                            e.onNext(filePath + filenName)
                        } else {
                            Utils.saveBitmap(filePath, filenName, resource)
                            e.onNext(filePath + filenName)
                        }
                        return false
                    }

                }).submit()

            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { path ->
                        //通知图库更新
                        // 其次把文件插入到系统图库
                        if (!TextUtils.isEmpty(path)) {
                            val f = File(path)
                            try {
                                MediaStore.Images.Media.insertImage(mActivity.contentResolver,
                                        f.absolutePath, f.name, null)
                                mActivity.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)))
                                f.delete()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }

                        }
                        if (mProgressView != null)
                            mProgressView!!.dismiss()
                        alertDialogs.dismiss()
                    }
            null
        }.show()
    }

    private var mProgressView: ProgressDialog? = null
    private fun showProgressView(str: String, mActivity: Activity) {
        mProgressView = ProgressDialog(mActivity)
        mProgressView!!.setTitle(null)
        mProgressView!!.setMessage(str)
        mProgressView!!.isIndeterminate = true
        mProgressView!!.setCancelable(false)
        mProgressView!!.setOnCancelListener(null)
        mProgressView!!.show()
    }

    //系统相册选图返回的Uri是可以直接使用的，不需要也不能使用FileProvider进行转换
    fun getUri(context: Context,f:File):Uri{
      return   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", f)
        } else {
            Uri.fromFile(f)
        }
    }

}
