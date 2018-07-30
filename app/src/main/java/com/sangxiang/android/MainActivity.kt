package com.sangxiang.android

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import com.sangxiang.android.demo.*
import com.sangxiang.android.utils.BitmapUtils
import com.sangxiang.android.utils.ResourcesUtils
import com.sangxiang.android.utils.appUpdate.CProgressDialogUtils
import com.sangxiang.android.utils.appUpdate.HProgressDialogUtils
import com.sangxiang.android.utils.appUpdate.UpdateAppHttpUtil
import com.sangxiang.android.utils.button_textview.setSolidTheme
import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager
import com.vector.update_app.UpdateCallback
import com.vector.update_app.service.DownloadService
import com.vector.update_app.utils.AppUpdateUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap

class MainActivity : BaseActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        eventbusBtn.onClick {
            startActivity<EventBusActivity>()
        }
        testFun.onClick {
            var str = "热门应用热门推荐热门活动热门应用热门推荐热11111"
            //str="热门"
            photoImage.setImageBitmap(BitmapUtils.getNameBitmap(str, dip(35f), dip(35f)))
        }
        testFun.setSolidTheme()
        update.onClick {
            val url = "https://yf.emucoo.net/cfb/download/emucoo_test.apk"
            diyUpdate(url,true,this@MainActivity)
        }
        recycleView.onClick {
            startActivity<RecycleViewActivity>()
        }

        nextBtn.onClick {
            startActivity<PermissionActivity>()
        }
        mTabLayoutViewPagerActivity.onClick {
            startActivity<TabLayoutViewPagerActivity>()
        }
        mSwitchButtonActivity.onClick {
            startActivity<SwitchButtonActivity>()
        }
    }

    private fun diyUpdate(mUpdateUrl: String,isShowDownloadProgress:Boolean,context:Activity) {
        val path = Environment.getExternalStorageDirectory().absolutePath

        val params = HashMap<String, String>()

        params["appKey"] = "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f"
        params["appVersion"] = AppUpdateUtils.getVersionName(this)
        params["key1"] = "value2"
        params["key2"] = "value3"
        UpdateAppManager.Builder()
                //必须设置，当前Activity
                .setActivity(this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(UpdateAppHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(mUpdateUrl)
                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
                //                .setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                .build()
                //检测是否有新版本
                .checkNewApp(object : UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    override fun parseJson(json: String): UpdateAppBean {
                        val updateAppBean = UpdateAppBean()
                        try {
                            val jsonObject = JSONObject(json)
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate("Yes")
                                    //（必须）新版本号，
                                    .setNewVersion("1.1.1")
                                    //（必须）下载地址
                                    .setApkFileUrl(mUpdateUrl)
                                    //（必须）更新内容
                                    .setUpdateLog("更新看看")
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize("10M")
                                    //是否强制更新，可以不设置
                                    .setConstraint(false)
                                    //.newMd5 = jsonObject.optString("new_md5")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        return updateAppBean
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    public override fun hasNewApp(updateApp: UpdateAppBean?, updateAppManager: UpdateAppManager) {
                        //自定义对话框
                        showDiyDialog(updateApp!!, updateAppManager,isShowDownloadProgress,context)
                    }

                    /**
                     * 网络请求之前
                     */
                    public override fun onBefore() {
                        CProgressDialogUtils.showProgressDialog(context)
                    }

                    /**
                     * 网路请求之后
                     */
                    public override fun onAfter() {
                        CProgressDialogUtils.cancelProgressDialog(context)
                    }

                    /**
                     * 没有新版本
                     */
                    public override fun noNewApp(error: String?) {
                        Toast.makeText(context, "没有新版本", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * 自定义对话框
     *
     * @param updateApp
     * @param updateAppManager
     */
    private fun showDiyDialog(updateApp: UpdateAppBean, updateAppManager: UpdateAppManager,isShowDownloadProgress:Boolean,context: Activity) {
        val targetSize = updateApp.targetSize
        val updateLog = updateApp.updateLog

        var msg = ""

        if (!TextUtils.isEmpty(targetSize)) {
            msg = "新版本大小：$targetSize\n\n"
        }

        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog
        }

        AlertDialog.Builder(this)
                .setTitle(String.format("是否升级到%s版本？", updateApp.newVersion))
                .setMessage(msg)
                .setPositiveButton("升级") { dialog, which ->
                    //显示下载进度
                    if (isShowDownloadProgress) {
                        updateAppManager.download(object : DownloadService.DownloadCallback {
                            override fun onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(context, "下载进度", false)
                            }

                            /**
                             * 进度
                             *
                             * @param progress  进度 0.00 -1.00 ，总大小
                             * @param totalSize 总大小 单位B
                             */
                            /**
                             * 进度
                             *
                             * @param progress  进度 0.00 -1.00 ，总大小
                             * @param totalSize 总大小 单位B
                             */
                            override fun onProgress(progress: Float, totalSize: Long) {
                                HProgressDialogUtils.setProgress(Math.round(progress * 100))
                            }

                            /**
                             *
                             * @param total 总大小 单位B
                             */
                            /**
                             *
                             * @param total 总大小 单位B
                             */
                            override fun setMax(total: Long) {

                            }


                            override fun onFinish(file: File): Boolean {
                                HProgressDialogUtils.cancel()
                                return true
                            }

                            override fun onError(msg: String) {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                HProgressDialogUtils.cancel()

                            }

                            override fun onInstallAppAndAppOnForeground(file: File): Boolean {
                                return false
                            }
                        })
                    } else {
                        //不显示下载进度
                        updateAppManager.download()
                    }


                    dialog.dismiss()
                }
                .setNegativeButton("暂不升级") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
    }

}
