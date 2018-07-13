package com.emucoo.business_manager.work

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.emucoo.business_manager.BaseActivity
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.model.ContactsResult
import kotlinx.android.synthetic.main.activity_user_select.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class UserSelectActivity : BaseActivity(),AnkoLogger {
    var list:ArrayList<ContactsResult.UserItem> = ArrayList()
    lateinit var mAdapter:UserSelectAdapter
    companion object {
        val REQUEST_CODE_CHOSE_USER=110
        val RESULT_CODE=111
        val param_contact_type="param_contact_type"
    }
    var contactType=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_select)
        setStatusBarDefaultColor()
        contactType=intent.getIntExtra(param_contact_type,0)
        initView()
    }

    private fun initView() {
        kvl_info.onClick {
           OrganizationSelectActivity.toActivity(this@UserSelectActivity, list,false,contactType,REQUEST_CODE_CHOSE_USER)
        }
        kvl_info1.onClick {
            OrganizationSelectActivity.toActivity(this@UserSelectActivity,list, true,contactType,REQUEST_CODE_CHOSE_USER)
        }
        rl_search_bar.onClick {
            UserSearchActivity.toActivity(this@UserSelectActivity,list,true,et_search.text.toString() ,REQUEST_CODE_CHOSE_USER)
        }
        mAdapter= UserSelectAdapter()
        mAdapter.setModel(list)
        mRecyclerView.adapter=mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_CHOSE_USER){
            if(data != null){
                //如果数据存在的话就赋值
                  data.getSerializableExtra(OrganizationSelectActivity.EXTRA_RESULT_ITEMS)?.let {
                  list=it as ArrayList<ContactsResult.UserItem>
                      mAdapter.setModel(list)
                  if(resultCode==UserSearchActivity.RESULT_CODE){
                      data.getStringExtra(UserSearchActivity.EXTRA_RESULT_ITEMS_KEYWORDS)?.let {
                          et_search.text=it
                      }
                  }
               }
            }
        }

    }
}
