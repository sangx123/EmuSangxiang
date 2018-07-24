package com.sangxiang.android.receiver

import android.app.Notification
import android.app.NotificationManager
import android.text.TextUtils
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.sangxiang.android.MainActivity
import com.sangxiang.android.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


/**
 * author : HLQ
 * e-mail : 925954424@qq.com
 * time   : 2018/01/25
 * desc   : 极光自定义接收器
 * version: 1.0
 */
class JPushReceiver : BroadcastReceiver(),AnkoLogger {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle = intent.extras
            error ("[MyReceiver] onReceive - " + intent.action + ", extras: " + printBundle(bundle!!))

            if (JPushInterface.ACTION_REGISTRATION_ID == intent.action) {
                val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                error("[MyReceiver] 接收Registration Id : " + regId!!)
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action) {
                error("[MyReceiver] 接收到推送下来的自定义消息: " + bundle!!.getString(JPushInterface.EXTRA_MESSAGE)!!)
                processCustomMessage(context, bundle)

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action) {
                error("[MyReceiver] 接收到推送下来的通知")
                val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                error("[MyReceiver] 接收到推送下来的通知的ID: $notifactionId")

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action) {
                error("[MyReceiver] 用户点击打开了通知")

                //打开自定义的Activity
                val i = Intent(context, MainActivity::class.java)
                i.putExtras(bundle!!)
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(i)

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.action) {
                error( "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle!!.getString(JPushInterface.EXTRA_EXTRA)!!)
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE == intent.action) {
                val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                error( "[MyReceiver]" + intent.action + " connected state change to " + connected)
            } else {
                error("[MyReceiver] Unhandled intent - " + intent.action!!)
            }
        } catch (e: Exception) {

        }


    }

    // 打印所有的 intent extra 数据
    private fun printBundle(bundle: Bundle): String {
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
            } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
            } else if (key == JPushInterface.EXTRA_EXTRA) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    error ("This message has no Extra data")
                    continue
                }

                try {
                    val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                    val it = json.keys()

                    while (it.hasNext()) {
                        val myKey = it.next().toString()
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]")
                    }
                } catch (e: JSONException) {
                    error ("Get message extra JSON error!")
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key))
            }
        }
        return sb.toString()
    }


    private fun processCustomMessage(context: Context, bundle: Bundle) {
        // get push message
        //val jgMessageId = bundle.getString(JPushInterface.EXTRA_MSG_ID)
        //val title = bundle.getString(JPushInterface.EXTRA_TITLE)
        //val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
        //val extra = bundle.getString(JPushInterface.EXTRA_EXTRA)
        // handle push message
        //mPushDomain.receive(context, jgMessageId, title, message, extra)
    }
}
