package com.xiang.one.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import com.xiang.one.BaseActivity
import com.xiang.one.R
import com.xiang.one.demo.*
import com.xiang.one.designpattern.DesignPatternActivity
import com.xiang.one.ui.jingdong.JingDongFragment
import com.xiang.one.ui.my.MyFragment
import com.xiang.one.ui.pingduoduo.PingDuoDuoFragment
import com.xiang.one.ui.taobao.TaoBaoFragment
import com.xiang.one.utils.BitmapUtils
import com.xiang.one.utils.SharePerferenceConfig
import com.xiang.one.utils.StringUtils
import com.xiang.one.utils.appUpdate.CProgressDialogUtils
import com.xiang.one.utils.appUpdate.HProgressDialogUtils
import com.xiang.one.utils.appUpdate.UpdateAppHttpUtil
import com.xiang.one.utils.button_textview.setSolidTheme
import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager
import com.vector.update_app.UpdateCallback
import com.vector.update_app.service.DownloadService
import com.vector.update_app.utils.AppUpdateUtils
import com.xiang.one.ui.maker_task.MakerFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap

class MainActivity : BaseActivity(), AnkoLogger {
    private var  currentFragment:Fragment?=null
    private var  nav1Fragment:Fragment=HomeFragment()
    private var  nav3Fragment:Fragment=JingDongFragment()
    private var  nav4Fragment:Fragment=MyFragment()
    private var  nav2Fragment:Fragment=MakerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNavBar.active=1
        //startActivity<MainActivity2>()
    }

    fun  switchFragment(position:Int) : FragmentTransaction {
        var targetFragment: Fragment?=null
         when(position){
             1->targetFragment=nav1Fragment
             2->targetFragment=nav2Fragment
             3->targetFragment=nav3Fragment
             4->targetFragment=nav4Fragment
         }
         var transaction = supportFragmentManager.beginTransaction()
         if (!targetFragment!!.isAdded) {
             //第一次使用switchFragment()时currentFragment为null，所以要判断一下        
             if (currentFragment != null) {
                 transaction.hide(currentFragment)
             }
             transaction.add(R.id.mainContainer, targetFragment,targetFragment.javaClass.name)
         }
         else {
             transaction.hide(currentFragment).show(targetFragment)
         }
          transaction.commitAllowingStateLoss()
          currentFragment = targetFragment
          return transaction
    }



}
