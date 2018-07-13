package com.emucoo.business_manager.work

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.emucoo.business_manager.BaseActivity
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.widgets.MFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_organization_select.*
import org.jetbrains.anko.startActivity

class OrganizationSelectActivity : BaseActivity() {
    var list:ArrayList<ContactsResult.UserItem>  = ArrayList()
    companion object {
        val REQUEST_CODE=112
        val RESULT_CODE=114
        val EXTRA_RESULT_ITEMS="EXTRA_RESULT_ITEMS"
        val param_data_multipleSelect="param_data_multipleSelect"
        val param_select_users="param_select_users"
        val param_contact_type="param_contact_type"
        fun toActivity(context: Context,list:ArrayList<ContactsResult.UserItem>,multipleSelect:Boolean,contactType:Int,requestCode:Int){
            var intent = Intent(context,OrganizationSelectActivity::class.java)
            intent.putExtra(param_data_multipleSelect,multipleSelect)
            intent.putExtra(param_select_users,list)
            intent.putExtra(param_contact_type,contactType)
            (context as BaseActivity).startActivityForResult(intent,requestCode)
        }

    }
    var multipleSelect=false
    var contactType=0
    //var a=Array<String>(5) { "it" }
    private val tabTitles= arrayOf("部门人员", "店长/门店")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_select)
        setStatusBarDefaultColor()
        multipleSelect=intent.getBooleanExtra(param_data_multipleSelect,false)
        contactType=intent.getIntExtra(param_contact_type,0)
        intent.getSerializableExtra(param_select_users)?.let {
           list= it as ArrayList<ContactsResult.UserItem>
        }
        var mAdapter=MFragmentStatePagerAdapter(supportFragmentManager, arrayOf(OrganizationDeptSelectFragment.newInstance(list,multipleSelect,contactType),OrganizationShopSelectFragment.newInstance(list,multipleSelect,contactType)).toMutableList() as List<Fragment>?,tabTitles)
        mViewPager.adapter=mAdapter
        mViewPager.setPageTransformer(false, ViewPager.PageTransformer { page, position ->
            when {
                position > 1 -> page.alpha = 1f
                position >= 0 -> page.alpha = 1 - position
                position > -1 -> page.alpha = 1 + position
                else -> page.alpha = 0f
            }
        })
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE){
            if(data!=null){
                //如果有数据就直接返回
                data.getSerializableExtra(OrganizationSelectActivity.EXTRA_RESULT_ITEMS)?.let {
                    list=it as ArrayList<ContactsResult.UserItem>
                    var intent=Intent()
                    intent.putExtra(OrganizationSelectActivity.EXTRA_RESULT_ITEMS,list)
                    setResult(RESULT_CODE,intent)
                    finish()
                }
            }
        }
    }
}
